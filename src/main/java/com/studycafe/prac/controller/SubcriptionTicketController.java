package com.studycafe.prac.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studycafe.prac.dao.MemberDao;
import com.studycafe.prac.dao.TodayTicketDao;
import com.studycafe.prac.dto.ScSalesDto;
import com.studycafe.prac.dto.ScreservDto;
import com.studycafe.prac.dto.SubscriptionTicketDto;
import com.studycafe.prac.dto.memberDto;
import com.studycafe.prac.dto.seatDto;

@Controller
public class SubcriptionTicketController {

	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value="/SubscriptionTicketBuy")//시간제(구독)이용권 구매페이지
	public String STicketBuy(HttpServletResponse response,HttpSession session,Model model) {
		
		String sessionId = (String) session.getAttribute("userId");
		MemberDao dao = sqlSession.getMapper(MemberDao.class);
		
		
		if(sessionId == null) {
			try {
				response.setContentType("text/html; charset=UTF-8");      
		        PrintWriter out;
				out = response.getWriter();
				out.println("<script>alert('로그인이 필요한 서비스입니다.'); history.go(-1);</script>");
			    out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			}
		
		if(sessionId !=null) {
		memberDto memberdto = dao.getMemberInfo(sessionId);
		String usingTicket = memberdto.getUsingTicket();
		int uTicket = Integer.parseInt(usingTicket);
			if(uTicket>=1) {
				try {
					response.setContentType("text/html; charset=UTF-8");      
			        PrintWriter out;
					out = response.getWriter();
					out.println("<script>alert('중복예약은 불가능합니다'); history.go(-1);</script>");
				    out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}	else {
				model.addAttribute("memberdto",memberdto);
				return "Ticket/SubscriptionTicketBuy";
			}
		}
		return "Ticket/SubscriptionTicketBuy";
	}
	@RequestMapping(value="/SubscriptionTicketView")//구독이용권좌석선택
	public String STicketView(HttpSession session,HttpServletResponse response) {
		
		String sessionId = (String) session.getAttribute("userId");
		TodayTicketDao dao = sqlSession.getMapper(TodayTicketDao.class);

		if(sessionId == null) {
			try {
				response.setContentType("text/html; charset=UTF-8");      
		        PrintWriter out;
				out = response.getWriter();
				out.println("<script>alert('로그인이 필요한 서비스입니다.'); history.go(-1);</script>");
			    out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			}
		if(sessionId!= null) {
			
			int subCount= dao.getSubscrCount(sessionId);
			
				if(subCount == 1) {
					return "Ticket/SubscriptionTicketView";
					} else {
						try {
							
							response.setContentType("text/html; charset=UTF-8");      
							PrintWriter out;
							out = response.getWriter();
							out.println("<script>alert('시간권을 먼저 구매해주세요.'); history.go(-1);</script>");
							out.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}	
		
		
		return "Ticket/SubscriptionTicketView";
	}
	@RequestMapping(value="/BuySubscription")//시간제(구독)이용권구매
	public String BuySTicket(HttpServletRequest request, Model model,HttpSession session) {
			
		TodayTicketDao dao = sqlSession.getMapper(TodayTicketDao.class);
		String sessionId = (String) session.getAttribute("userId");
		
		String sticketName = request.getParameter("sticketName");
		String afterPayingPoint = request.getParameter("afterPayingPoint");
		
		String newPayingPoint = sticketName.replaceAll(",", "");
		//포인트를 고르면 해당하는 시간이 db에 저장됨
		String[] SubPrice = {"65,000","120,000","160,000","200,000"};
		String[] SubTime = {"50","100","150","200"};
		for(int i=0;i<4;i++) {
			if(SubPrice[i].equals(sticketName)) {
				dao.BuySTicket(sticketName, sessionId, SubTime[i]);//subscriptiontbl 시간 저장
				dao.updateUsingTicketPointM(sessionId, afterPayingPoint, SubTime[i]);//membertbl 유저보유 포인트와 티켓 갱신
				dao.addSalesInfo(sessionId,newPayingPoint);//scsalesTBl에 매출올림
			}
		
		model.addAttribute("sticketName",sticketName);
		}
		return "Ticket/SubscriptionTicketView";
	}
	@RequestMapping(value="/searchsTicketSeat")
	public String searchsTicketSeat(HttpServletRequest request, Model model,HttpSession session) {
		
		TodayTicketDao dao = sqlSession.getMapper(TodayTicketDao.class);
		String sessionId = (String) session.getAttribute("userId");
		
		String selectedDate = request.getParameter("selectedDate");
		int seatNo = Integer.parseInt(request.getParameter("seatNo").toString());
	
		//날짜와 좌석을 기반으로 정보를 모두 가져옴
				List<ScreservDto> reservInfo = dao.searchReservation(seatNo, selectedDate);
				
				//받아온 db정보들을 String 리스트에 저장
				List<String> intselectedTime = new ArrayList<String>();
				for(int i=0;i<reservInfo.size();i++) {
					String times = reservInfo.get(i).getSelectedTime();//reservinfo에서 원하는 정보인 selectedTime만 저장
					intselectedTime.add(times); //새 String 리스트에 하나씩 저장
				}
				
			
				//인트 리스트로 변환
				int n,f;
				 List<Integer> IntDBselectedTimes = new ArrayList<Integer>();
			        for (n = 0; n < intselectedTime.size(); n++) {
			        	IntDBselectedTimes.add(Integer.parseInt(intselectedTime.get(n)));
			        }
			        
			    List<String> OccupiedTimes = new ArrayList<String>();
			    for(f = 1; f<=17;f++) {
			    	if(IntDBselectedTimes.contains(f)) {
			    		OccupiedTimes.add("1");
			    	}else {
			    		OccupiedTimes.add("0");
			    	}
			    }
		model.addAttribute("selectedDate", selectedDate);
		model.addAttribute("seatNo", seatNo);
		model.addAttribute("opTimes",OccupiedTimes);
		
		return "Ticket/SubscriptionTicketView2";
	
	}
	@RequestMapping(value="/SubscriptionTicketView2")
	public String SubscriptionTicketView2() {
		
		
		return "Ticket/SubscriptionTicketView2";
	}
	@RequestMapping(value="/registsTicketConfirm")
	public String registsTicketConfirm(HttpServletRequest request, Model model,HttpServletResponse response,HttpSession session) {
		
		TodayTicketDao dao = sqlSession.getMapper(TodayTicketDao.class);
		String sessionId = (String) session.getAttribute("userId");
		
		SubscriptionTicketDto sticketDto= dao.getSTicketInfo(sessionId);
		String remainTime = sticketDto.getSremainTime();//유저 이용권의 남은시간 불러오기
		
		
		String seatNo = request.getParameter("seatNo");	
		String selectedDate = request.getParameter("selectedDate");
		String [] selectedTime = request.getParameterValues("selectedTime");
		
		
		
		//넘어온 체크박스값 정렬 후, 첫번째 값부터 마지막값까지 추출후 새 배열에 넣음
				Arrays.sort(selectedTime);//먼저 배열들 순서 정리
				int i;
				String [] selectedTimes= new String[selectedTime.length];//새배열 생성
				for(i=0;i<selectedTime.length;i++) {	
				String number=selectedTime[i];
				selectedTimes[i]=number;//새로 생성한 배열에 selectedTime의 체크박스값들 저장
				}
				
				int bticketName = selectedTime.length;//이용시간을 selectedTime 체크박스 갯수를 통해 저장
				
				int intremainTime = Integer.parseInt(remainTime);
				int afterRemaintime= intremainTime - bticketName;
				
				String remainTime2 = String.valueOf(afterRemaintime);
		    
						
							
						//방금 들어간 회원정보들을 결제 전 체크페이지에 전달
						
				//-------시작시간 종료시간 뽑아내기---------
				int[] Times= new int[17];//8부터 24까지 문자배열생성
				for(int j=0;j<16;j++) {
				Times[j]=j+8;	
				}
				  int[] intstTimes= new int[selectedTimes.length]; //체크박스 지정된 갯수만큼의 사이즈의 배열 생성
				  for (int k = 0; k < selectedTimes.length; k++) {
			            intstTimes[k] = Integer.parseInt(selectedTimes[k]);//체크박스 값들을 정수배열로 변환
			        }
				  int[] savedTimes= new int[intstTimes.length];//체크박스의 값들을 시간으로 치환해줄 배열생성
				  for (i=0;i < intstTimes.length;i++) {
					  savedTimes[i]=Times[intstTimes[i]];//체크박스값 배열들을 시간으로 치환하여 저장
				  }
				  String startTime = String.valueOf(savedTimes[0]); //시작시간을 저장
				  int lastindex = savedTimes[savedTimes.length-1];
				  lastindex = lastindex + 1 ;
				  String endTime = String.valueOf(lastindex); //종료시간 저장
				//-------시작시간 종료시간 뽑아내기 끝---------		 
						  
						  model.addAttribute("selectedDate",selectedDate);
						  model.addAttribute("startTime",startTime);
						  model.addAttribute("endTime",endTime);
						  model.addAttribute("seatNo",seatNo );
						  model.addAttribute("totalHour",bticketName);
						  model.addAttribute("remainTime",remainTime2);
						  model.addAttribute("selectedTime",selectedTime);
						
					
					
					return "Ticket/registsTicketConfirm";
					

		
	}
	@RequestMapping(value="/sTicketReservComplete")
	public String sTicketReservComplete(HttpServletRequest request,HttpServletResponse response,Model model
			,HttpSession session) {
		TodayTicketDao tdao = sqlSession.getMapper(TodayTicketDao.class);
		
		String sessionId = (String) session.getAttribute("userId");
		String selectedDate = request.getParameter("selectedDate");
		int seatNo = Integer.parseInt(request.getParameter("seatNo"));
		String [] selectedTime = request.getParameterValues("selectedTime");
		String remainTime = request.getParameter("remainTime");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
 		
		int bticketName = selectedTime.length;
		int intRemainTime = Integer.parseInt(remainTime);
		
		//넘어온 체크박스값 정렬 후, 첫번째 값부터 마지막값까지 추출후 새 배열에 넣음
		Arrays.sort(selectedTime);//먼저 배열들 순서 정리
		int i;
		String [] selectedTimes= new String[selectedTime.length];//새배열 생성
		for(i=0;i<selectedTime.length;i++) {	
		String number=selectedTime[i];
		selectedTimes[i]=number;//새로 생성한 배열에 selectedTime의 체크박스값들 저장
		}
		
		
		if(intRemainTime<0) {//보유시간이 예약시간보다 적을시 예약못함
			try {
				response.setContentType("text/html; charset=UTF-8");      
		        PrintWriter out;
				out = response.getWriter();
				out.println("<script>alert('예약시간이 사용가능한 시간을 초과했습니다. 시간을 충전해주세요.'); history.go(-1);</script>");
			    out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}	else {//보유시간이 예약시간보다 많을 경우 예약가능
			String sticketName = String.valueOf(bticketName);
			
			List<ScSalesDto> salesDto = tdao.getSalesNo(sessionId);//매출 테이블의 해당 아이디로 된 가장 최신매출건의 번호 가져오기 
			int intSalesNo = salesDto.get(0).getSalesNo();
			String salesNo = Integer.toString(intSalesNo);
			
			tdao.regist(seatNo, sessionId, sticketName, selectedDate,startTime,endTime,salesNo);
			tdao.updateRemainTime(sessionId, remainTime);
				for(int n=1;n<=selectedTime.length;n++) {//ST[i]배열의 값을 각각 체크박스 갯수만큼 데이타베이스(선택시간)에 넣음 
					tdao.makeReservation(seatNo, sessionId, selectedDate, selectedTimes[n-1]);
				}
	
			
			
		}
		
		
		
		return "Ticket/sTicketReservComplete";
	}
	
	
	
	
}
