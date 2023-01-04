package com.studycafe.prac.dao;

import java.util.ArrayList;
import java.util.List;

import com.studycafe.prac.dto.ScSalesDto;
import com.studycafe.prac.dto.ScreservDto;
import com.studycafe.prac.dto.SubscriptionTicketDto;
import com.studycafe.prac.dto.seatDto;

public interface TodayTicketDao {
	
	
	//당일권 관련
	public void regist(int seatNo, String userId, String ticketName, String selectedDate,String startTime, String endTime, String salesNo);//예약좌석정보넣기
	public ArrayList<seatDto> registTodayConfirm();//
	public ArrayList<seatDto> searchSeat(String seatNo,String selectedDate);//
	public ArrayList<ScreservDto> searchReservation(int seatNo, String selectedDate);//해당날짜,좌석으로 예약된시간대 전부 가져오기
	public void makeReservation(int seatNo,String userId, String selectedDate, String selectedTime);//당일권 예약 최종결제
	
	
	//시간제(정액권) 관련
	public void BuySTicket(String sticketName, String suserId, String sremainTime);//처음 시간권 구매
	public void updateRemainTime(String suserId, String sremainTime);//결제시 시간권에서 남은시간 갱신
	public SubscriptionTicketDto getSTicketInfo(String suserId);//
	public void updateUsingTicketPointM(String suserId, String userPoint, String usingTicket);//결제시 포인트 지불
	public int getSubscrCount(String suserId); //이용권 선택에서 시간권 사용중인지 아닌지 조회하기
	
	//매출관련
	public void addSalesInfo(String userId, String payingPoint); //당일권,시간권 결제시 매출테이블에추가
	public List<ScSalesDto> getChartInfo(); //월별로 그룹화 하여 매출액 총합 가져오기
	public List<ScSalesDto> getSalesNo(String userId);//아이디 그룹화하여 가장 최신에 등록된 매출번호 가져오기
	
	//내예약정보확인관련
	public seatDto getReservInfo(String userId); //내 예약정보 확인
	public void cancelReservSeatTbl(String tempUsingNo); //seatTbl의 예약 취소
	public void cancelReservResvTbl(String userId, String seatNo, String selectedDate);//reservTbl(좌석및 시간대) 예약 취소
	public void cancelReservSaleTbl(String salesNo);
	public void returnUserPointTicket(String userId, String userPoint, String usingTicket);
}
