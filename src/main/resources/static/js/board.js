/**
 * 
 */
 
function boardCheck() {
   
   if(document.board_frm.btitle.value.length == 0) {
      alert("제목은 필수입력 사항입니다.");
      return;
   }
   
   if(document.board_frm.bcontent.value.length == 0) {
      alert("질문은 필수입력 사항입니다.");
      return;
   }
   
   document.board_frm.submit();
} 
 
