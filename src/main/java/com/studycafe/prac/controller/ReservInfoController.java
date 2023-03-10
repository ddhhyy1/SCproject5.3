package com.studycafe.prac.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studycafe.prac.dao.MemberDao;
import com.studycafe.prac.dao.TodayTicketDao;
import com.studycafe.prac.dto.SubscriptionTicketDto;
import com.studycafe.prac.dto.memberDto;
import com.studycafe.prac.dto.seatDto;

@Controller
public class ReservInfoController {

	
	@Autowired
	private SqlSession sqlSession;
	
	
	@RequestMapping(value="/checkReservInfo")
	public String checkReservInfo(Model model,HttpSession session) {
		
		String sessionId = (String) session.getAttribute("userId");
		
		MemberDao mdao = sqlSession.getMapper(MemberDao.class);
		TodayTicketDao tdao = sqlSession.getMapper(TodayTicketDao.class);
		
		memberDto memberDto = mdao.getMemberInfo(sessionId);
		SubscriptionTicketDto stDto = tdao.getSTicketInfo(sessionId);
		seatDto sDto = tdao.getReservInfo(sessionId);
		
		if(sDto == null) {
			
			model.addAttribute("memberDto", memberDto);
			model.addAttribute("stDto", stDto);
			return "Ticket/checkReservInfo";
		}else {
		String selectedDate = sDto.getSelectedDate();
		
		String year= selectedDate.substring(0, 4);
		String month= selectedDate.substring(4, 6);
		String day= selectedDate.substring(6, 8);
		
		model.addAttribute("selectedDate",selectedDate);
		model.addAttribute("day",day);
		model.addAttribute("month",month);
		model.addAttribute("year",year);
		model.addAttribute("memberDto", memberDto);
		model.addAttribute("stDto", stDto);
		model.addAttribute("sDto",sDto);
		return "Ticket/checkReservInfo";
		}
	}
	
	@RequestMapping(value="/cancelReserv")
	public String cancelReserv2(HttpSession session,HttpServletRequest request) {
		
		String sessionId = (String) session.getAttribute("userId");
		
		TodayTicketDao tdao = sqlSession.getMapper(TodayTicketDao.class);
		MemberDao mdao = sqlSession.getMapper(MemberDao.class);
		seatDto sDto = tdao.getReservInfo(sessionId);
		
		
		memberDto memberDto = mdao.getMemberInfo(sessionId);
		int userPoint = Integer.parseInt(memberDto.getUserPoint().toString());//?????? ?????? ???????????? ??????????????? ?????? int?????????
		
		int usingTicket = Integer.parseInt(memberDto.getUsingTicket().toString());//???????????? ?????? ????????? ???????????????.
		int intUsingTicket = usingTicket;//????????? ????????? ?????? 0?????? ??????????????? ???????????? ?????? ?????? ???????????? ??????
		String strUsingTicket = Integer.toString(usingTicket);// ??????????????? ???????????? ?????? str??? ??????
		usingTicket = 0; //????????? ???????????? ???????????? 0?????? ??????
		String zeroUsingTicket = Integer.toString(usingTicket); //0?????? ????????? int??? ?????? str??? ???????????? ??????
		String salesNo = sDto.getSalesNo();//??????????????? ?????? ????????????
		
		String tempUsingNo = request.getParameter("tempUsingNo");
		String seatNo = request.getParameter("seatNo");
		String selectedDate = request.getParameter("selectedDate");
		
			if(intUsingTicket>0 && intUsingTicket<50) { //????????? ?????????
				
		
			//???????????? ????????? ????????? ????????????		
				int t;
				String[] TodayPrice = {"2,000","3,000","5,000","6,000","7,000"};
				String[] TodayTime = {"1","2","4","6","8"};
				for(t=0;t<5;t++) {
					if(TodayTime[t].equals(strUsingTicket)) {//str??? ????????? ??????????????? TodayTime??? ????????? ?????? ??????????????? ????????? ??????
						String returnPoint = TodayPrice[t];
						String rPoint = returnPoint.replaceAll(",", "");//????????? ???????????? , ?????? ??? ??????
						int intRPoint = Integer.parseInt(rPoint); //????????? ?????? int????????? ??????
						String finalUserRPoint = Integer.toString(userPoint+intRPoint);//????????? ?????? ?????? ???????????? ????????? ????????? ??? ?????? str???????????????
						tdao.returnUserPointTicket(sessionId, finalUserRPoint ,zeroUsingTicket);//???????????? ????????? ???????????? ??????
					}
				}
		
				
				
				
		
				tdao.cancelReservSaleTbl(salesNo); //?????? ????????? ?????? ??????
		
				tdao.cancelReservSeatTbl(tempUsingNo); //??????????????? ????????? ?????? ??????
				tdao.cancelReservResvTbl(sessionId, seatNo, selectedDate); //????????? ????????? ??? ?????? ??????
		
				System.out.println("??????");
			}else {
				
				
				
				tdao.cancelReservSeatTbl(tempUsingNo); //??????????????? ????????? ?????? ??????
				tdao.cancelReservResvTbl(sessionId, seatNo, selectedDate); //????????? ????????? ??? ?????? ??????
				
				
			}
		
		return "index";
	}
}
