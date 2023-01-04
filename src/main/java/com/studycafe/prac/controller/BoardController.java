package com.studycafe.prac.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studycafe.prac.dao.BoardDao;
import com.studycafe.prac.dao.MemberDao;
import com.studycafe.prac.dao.TodayTicketDao;
import com.studycafe.prac.dto.BoardDto;
import com.studycafe.prac.dto.Criteria;
import com.studycafe.prac.dto.PageDto;
import com.studycafe.prac.dto.memberDto;
import com.studycafe.prac.dto.replyDto;
import com.studycafe.prac.dto.seatDto;


@Controller
public class BoardController {
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "/question")
	public String question(HttpSession session, Model model) {
			
		String sessionId = (String) session.getAttribute("userId");
		
		if(sessionId == null) {//로그인 상태 확인
			model.addAttribute("userId", "GUEST");
		} else {
			model.addAttribute("userId", sessionId);
		}
		
		return "question";
	}
	
	@RequestMapping(value = "/questionOk")
	public String questionOk(HttpServletRequest request) {
		
		String btitle = request.getParameter("btitle");//질문 제목
		String bcontent = request.getParameter("bcontent");//질문 내용
		String buserid = request.getParameter("buserid");//글쓴유저 아이디
		
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		
		dao.writeQuestion(btitle, bcontent, buserid);
		
		return "redirect:list";
	}
	
	@RequestMapping(value = "list")
	public String list(Model model, Criteria cri, HttpServletRequest request) {
		
		int pageNumInt = 0;
		if(request.getParameter("pageNum") == null) {
			pageNumInt = 1;
			cri.setPageNum(pageNumInt);
			
		} else {
			pageNumInt = Integer.parseInt(request.getParameter("pageNum"));
			cri.setPageNum(pageNumInt);
		}
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		
		int totalRecord = dao.boardAllCount();
		
		//cri.setPageNum();
		
		cri.setStartNum(cri.getPageNum()-1 * cri.getAmount());//해당 페이지의 시작번호를 설정
		
		PageDto pageDto = new PageDto(cri, totalRecord);
		
		List<BoardDto> qboardDtos = dao.questionList(cri);
		
		model.addAttribute("pageMaker", pageDto);
		model.addAttribute("qdtos", qboardDtos);
		model.addAttribute("currPage", pageNumInt);
		
		return "questionList";
	}
	
	@RequestMapping(value = "/questionView")
	public String questionView(HttpServletRequest request, Model model, HttpSession session) {
		
		String bnum = request.getParameter("bnum");
		
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		
		BoardDto rfboardDto = dao.rfboardView(bnum);
		ArrayList<replyDto> replyDtos = dao.rlist(bnum);
		
		model.addAttribute("qdto", rfboardDto);
		model.addAttribute("replylist", replyDtos);
		model.addAttribute("buserid", rfboardDto.getBuserid());//글쓴 유저의 id값 전송
		
		
		return "questionView";
	}
	
	@RequestMapping(value = "/questionModify")
	public String questionModify(HttpServletRequest request, Model model) {
		
		String bnum = request.getParameter("bnum");
		
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		
		BoardDto qBoardDto = dao.questionView(bnum);
		
		model.addAttribute("qdto", qBoardDto);
		
		
		return "questionModify";
	}
	
	@RequestMapping(value = "/questionModifyOk")
	public String questionModifyOk(HttpServletRequest request) {
		
		String bnum = request.getParameter("bnum");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		String buserid = request.getParameter("buserid");
		
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		
		dao.questionModify(bnum, btitle, bcontent, buserid);
		
		return "redirect:list";
	}
	
	@RequestMapping(value = "/questionDelete")
	public String questionDelete(HttpServletRequest request) {
		
		String bnum= request.getParameter("bnum");
		
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		
		dao.questionDelete(bnum);
		
		return "redirect:list";
	}
	
	@RequestMapping(value = "replyOk")
	public String replyOk(HttpServletResponse response, HttpServletRequest request, HttpSession session, Model model ) {
		
		String rorinum = request.getParameter("bnum");//댓글이 달린 원글의 번호
		String rcontent = request.getParameter("rcontent");//댓글 내용
		
		String sessionId = (String) session.getAttribute("userId");//현재 로그인한 유저의 아이디
		
		if(sessionId == null) {//참이면 로그인이 안된 상태
			PrintWriter out;
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('로그인하지 않으면 댓글을 쓰실수 없습니다!');history.go(-1);</script>");
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			
			BoardDao dao = sqlSession.getMapper(BoardDao.class);
			dao.rwrite(rorinum, sessionId, rcontent);//댓글 쓰기
			dao.brcount(rorinum);//해당글의 댓글 총 개수 증가
			
			
			BoardDto rfboardDto = dao.rfboardView(rorinum);
			ArrayList<replyDto> replyDtos =  dao.rlist(rorinum);
			
			model.addAttribute("qdto", rfboardDto);//원글의 게시글 내용 전부
			model.addAttribute("replylist", replyDtos);//해당 글에 달린 댓글 리스트
			model.addAttribute("bnum",rorinum);
		}
		
		return "questionView";
	}
	
	@RequestMapping(value = "replyDelete")
	public String replyDelete(HttpServletRequest request, Model model) {
		
		String rnum = request.getParameter("rnum");//댓글 고유번호
		String rorinum = request.getParameter("bnum");//댓글이 달린 원글의 고유번호
		
		BoardDao dao = sqlSession.getMapper(BoardDao.class);
		
		dao.rdelete(rnum);//댓글 삭제
		dao.brcountMinus(rorinum);//해당 글의 댓글 갯수 1감소
		
		BoardDto rfboardDto = dao.rfboardView(rorinum);
		ArrayList<replyDto> replyDtos = dao.rlist(rorinum);
		
		model.addAttribute("qdto", rfboardDto);//원글의 게시글 내용 전부
		model.addAttribute("replylist", replyDtos);//해당 글에 달린 댓글 리스트
		
		return "questionView";
	}
}

