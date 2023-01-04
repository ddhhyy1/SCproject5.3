package com.studycafe.prac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
	
	private int bnum;//게시글 넘버
	private String btitle;
	private String bcontent;
	private String buserid;//글쓴이의 아이디
	private int rcount;//첨부된 댓글 개수
	private String bdate;
	
}
