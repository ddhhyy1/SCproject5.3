package com.studycafe.prac.dao;


import java.util.ArrayList;
import java.util.List;


import com.studycafe.prac.dto.BoardDto;
import com.studycafe.prac.dto.Criteria;
import com.studycafe.prac.dto.replyDto;

public interface BoardDao {
	
	//게시판 관련
	public void writeQuestion(String btitle, String bcontent, String buserid);//질문하기 insert
	public List<BoardDto> questionList(Criteria cri);//질문게시판 리스트 가져오기 select
	public BoardDto questionView(String bnum);//선택한 글 번호의 정보 가져오기 select
	public BoardDto rfboardView(String bnum);
	public void questionModify(String bnum, String btitle, String bcontent, String buserid);//해당 글번호로 조회하여 질문 수정 update
	public void questionDelete(String bnum);//글 삭제 delete
	public int boardAllCount();//게시판 총 글의 개수 가져오기
	
	//게시판 댓글 관련
	public void rwrite(String rorinum, String rid, String rcontent);//새 댓글 입력 insert
	public ArrayList<replyDto> rlist(String rorinum);//해당글의 댓글 리스트 select
	public void brcount(String rorinum);//댓글 등록시 해당글의 댓글갯수 1증가 
	public void rdelete(String rnum);//댓글 삭제
	public void brcountMinus(String rorinum);//댓글 삭제시 해당글의 댓글갯수 1감소
}
