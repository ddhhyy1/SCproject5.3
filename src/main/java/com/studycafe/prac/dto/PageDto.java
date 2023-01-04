package com.studycafe.prac.dto;





import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
	
	private int startPage; //현재 화면에서 보여질 시작 페이지 번호
	private int endPage;// 현재 화면에서 보여질 마지막 페이지 번호
	private boolean prev;
	private boolean next;
	private int total;//전체 페이지 개수

	private Criteria cri;
	
	public PageDto(Criteria cri,int total) {
		
		this.cri = cri;
		this.total = total;
		
		this.endPage= (int)Math.ceil((cri.getPageNum()/8.0)) * 8;//math.ceil -> 올림함수
		// 총 57개글 존재한다고 했을때
		//1 2 3 4 5 next
		// prev 6 7 8 9 10 next
		// prev 11 12 
		//즉 Math.ceil((cri.getPageNum()/5.0))에서 5.0은 밑에 보여질 페이지가 5개인걸로 한정하겠다는 것.
		this.startPage = this.endPage - 7;
		int realEnd = (int) Math.ceil(total*1.0/cri.getAmount()); //이렇게 하면 amount초기값을 바꿀때마다 같이 바뀐다.
		//total은 정수 amount도 정수이다 정수/정수 해버리면 정수가 나와버리기 때문에 올림을 해도 수가 1 늘어나지 않는다.
		//고로 실수/정수 로 바꾸기 위해 total에 1.0을 곱해준것

		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}	
			
		this.prev = this.startPage > 1;
		this.next = this.endPage < realEnd;
		//이전 다음 페이지 출력 여부 결정 불링값
		}
	
}
