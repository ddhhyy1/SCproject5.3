package com.studycafe.prac.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studycafe.prac.dao.MemberDao;
import com.studycafe.prac.dao.TodayTicketDao;
import com.studycafe.prac.dto.ScSalesDto;
import com.studycafe.prac.dto.ScreservDto;
import com.studycafe.prac.dto.memberDto;
import com.studycafe.prac.dto.seatDto;


@Controller
public class TodayTicketController {

	@Autowired
	private SqlSession sqlSession;
	
	
	@RequestMapping(value="/testpage")//이용금액표
	public String testpage() {
		
		
		return "testpage";
	}
	@RequestMapping(value="/testpage2")//이용금액표
	public String testpage2(Model model) {
		
		String [] testarray = {"0","1","0","1","0","0","1","0","0","1"};
		
		model.addAttribute("test",testarray);
		
		return "testpage2";
	}
	@RequestMapping(value="/tabletest")//이용금액표
	public String talbetest() {
		
		
		return "test/tabletest";
	}
	
	@RequestMapping(value="/loginpage")//이용금액표
	public String loginpage() {
		
		
		return "loginpage";
	}
	
	@RequestMapping(value="/TicketPrice")//이용금액표
	public String TicketPrice() {
		
		
		return "Ticket/TicketPrice";
	}
	
	@RequestMapping(value="/ChooseTicket")//이용권선택
	public String chooseTicket() {
		
		
		return "Ticket/ChooseTicket";
	}
	
	@RequestMapping(value="/TodayTicketView")
	public String TodayTicketView(HttpServletResponse response,HttpSession session) {
		
		String sessionId = (String) session.getAttribute("userId");
		MemberDao dao2 = sqlSession.getMapper(MemberDao.class);
		
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
		memberDto userP = dao2.getMemberInfo(sessionId);
		String usingTicket = userP.getUsingTicket();
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
		
			
			}	
		}
		
		
			
		
		
		return "Ticket/TodayTicketView";
	}
	
	@RequestMapping(value="/searchSeat")
	public String searchSeat(HttpServletRequest request, Model model) {
		
		TodayTicketDao dao = sqlSession.getMapper(TodayTicketDao.class);
		
		String userId = request.getParameter("userId");
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
	    
	
		String strSeatNo = String.valueOf(seatNo);	
		model.addAttribute("userId", userId);
		model.addAttribute("selectedDate", selectedDate);
		model.addAttribute("seatNo", strSeatNo);
		model.addAttribute("opTimes", OccupiedTimes);
		
		return "Ticket/TodayTicketView2";
	}

	@RequestMapping(value="/TodayTicketView2")
	public String TodayTicketView2() {
			
	
			
			
		
		return "Ticket/TodayTicketView2";
	}
	
	@RequestMapping(value="/registToday")
	public String regist(HttpServletRequest request,HttpServletResponse response,Model model) {
		
		String seatNo = request.getParameter("seatNo");
		String ticketName = request.getParameter("ticketName");
		String selectedDate = request.getParameter("selectedDate");
		String [] selectedTime = request.getParameterValues("selectedTime");
		for(int i=0;i<selectedTime.length;i++) {
			System.out.println(selectedTime[i]);
			}
		
		//넘어온 체크박스값 정렬 후, 첫번째 값부터 마지막값까지 추출후 새 배열에 넣음
		Arrays.sort(selectedTime);
		int i;
		String [] selectedTimes= new String[selectedTime.length];
		for(i=0;i<selectedTime.length;i++) {	
		String number=selectedTime[i];
		selectedTimes[i]=number;
		}
		
		
		int intticketName= Integer.parseInt(ticketName);//ticketname을 int로 변환 체크박스 갯수를 알아내기위해
			
		if(intticketName==selectedTime.length) {//체크박스의 갯수와 ticketname의 숫자를 비교해서 일치하지 않으면 뒤로돌려보냄
				
				//-----------------방금 들어간 회원정보들을 결제 전 체크페이지에 전달	---------------------------
//				ArrayList<seatDto> seatDto= dao.registTodayConfirm();
//				
//				seatDto fseatDto = seatDto.get(0);
//				
				int t;
				String[] TodayPrice = {"2,000","3,000","5,000","6,000","7,000"};
				String[] TodayTime = {"1","2","4","6","8"};
				for(t=0;t<5;t++) {
					if(TodayTime[t].equals(ticketName)) {
						
						model.addAttribute("PayingPoint",TodayPrice[t]);//지불할 포인트 계산
					}
				}
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
				  
				  model.addAttribute("selectedDate",selectedDate);
				  model.addAttribute("startTime",startTime);
				  model.addAttribute("endTime",endTime);
				  model.addAttribute("seatNo",seatNo );
				  model.addAttribute("ticketName",ticketName);
				  model.addAttribute("selectedTime",selectedTime);
				  
				//-----------------방금 들어간 회원정보들을 결제 전 체크페이지에 전달 끝	---------------------------
				return "Ticket/registTodayConfirm";
			}else {
			try {
				response.setContentType("text/html; charset=UTF-8");      
		        PrintWriter out;
				out = response.getWriter();
				out.println("<script>alert('이용시간과 선택한 지정시간이 일치하지 않습니다.'); history.go(-1);</script>");
			    out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "Ticket/TodayTicketView2";
			}
		
		
		
		
	
	}	
	
	
	@RequestMapping(value="/registTodayConfirm")
	public String confirm(Model model) {

		
		
		return "Ticket/registTodayConfirm";
	}
	@RequestMapping(value="/todayPay")
	public String todayPay(HttpServletRequest request,HttpServletResponse response,Model model
			,HttpSession session) {

		TodayTicketDao tdao = sqlSession.getMapper(TodayTicketDao.class);
		MemberDao mdao = sqlSession.getMapper(MemberDao.class);
		String sessionId = (String) session.getAttribute("userId");
		memberDto dto=new memberDto();
		seatDto seatdto=new seatDto();
		
		
		//받아온 파라미터값들 request 객체로 받아 저장
		int seatNo = Integer.parseInt(request.getParameter("seatNo").toString());	
		String ticketName = request.getParameter("ticketName");
		String selectedDate = request.getParameter("selectedDate");
		String [] selectedTime = request.getParameterValues("selectedTime");
		String PayingPoint = request.getParameter("PayingPoint");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
				//넘어온 체크박스값 정렬 후, 첫번째 값부터 마지막값까지 추출후 새 배열에 넣음
				Arrays.sort(selectedTime);
				int i;
				String [] selectedTimes= new String[selectedTime.length];
				for(i=0;i<selectedTime.length;i++) {	
				String number=selectedTime[i];
				selectedTimes[i]=number;
				}
				for(i=0;i<selectedTimes.length;i++) {
				System.out.println(selectedTimes[i]);
				}
				
				
				//userpoint를 받아옴
				memberDto userP = mdao.getMemberInfo(sessionId);
				String UserP = userP.getUserPoint();
				
				int intUserP= Integer.parseInt(UserP);
				
				String newPayingPoint = PayingPoint.replaceAll(",", ""); //지불할 포인트의 ,를 없앰
				int intPayingPoint=Integer.parseInt(newPayingPoint); //인티저로 변환
				
				if (intUserP>=intPayingPoint) { //보유한 포인트가 지불할 포인트보다 많을 경우만 통과
					int intUserP2=intUserP-intPayingPoint;
					String UserPoAfterPaying=String.valueOf(intUserP2);
				
					tdao.addSalesInfo(sessionId, newPayingPoint);//매출 테이블에 등록
					mdao.updateUticketPoint(sessionId, UserPoAfterPaying, ticketName);//결제후 회원포인트와 이용권이름 갱신
					List<ScSalesDto> salesDto = tdao.getSalesNo(sessionId);//매출 테이블의 해당아이디의 최신 결제 번호
					int intSalesNo = salesDto.get(0).getSalesNo();
					String salesNo = Integer.toString(intSalesNo);
					
					tdao.regist(seatNo, sessionId, ticketName, selectedDate,startTime,endTime,salesNo);//정보들을 scseatTbl에 먼저저장
						for(int n=0;n<selectedTime.length;n++) {//ST[i]배열의 값을 각각 체크박스 갯수만큼 데이타베이스(선택시간)에 넣음 
							tdao.makeReservation(seatNo, sessionId, selectedDate, selectedTimes[n]);//예약테이블에 체크박스 횟수만큼 저장
							}
					
						return "Ticket/todayPayOk";
				
				}
				else {
					try {
						response.setContentType("text/html; charset=UTF-8");      
				        PrintWriter out;
						out = response.getWriter();
						out.println("<script>alert('포인트가 부족합니다.'); history.go(-1);</script>");
					    out.flush();
						} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
						return "Ticket/registTodayConfirm";
					}	
				}
	@RequestMapping(value="/todayPayOk")
	public String todayPayOk() {
			
	
			
			
		
		return "Ticket/todayPayOk";
	}
	
}
