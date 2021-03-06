package insteadSubj;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import departr.DepartrDAO;
import departr.DepartrDTO;
import userClass.userClassDAO;
import userClass.userClassDTO;

/**
 * Servlet implementation class InsteadCommitServlet
 */

public class InsteadCommitServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
       
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      String userID = request.getParameter("userID3");
      String BeReplaceSubjCode = request.getParameter("insteadselect"); //대체되는 과목코드
      String ReplaceSubjCode = request.getParameter("selecInstead");  //대체 과목코드
      
      //대체 과목의 점수, 수강한 년도, 학기 뽑기 ->ArrayList로 안해도 되는데 ... 바꾸기 귀찮.
      ArrayList<userClassDTO> userInsteadClass = new userClassDAO().getInsteadClass(userID, ReplaceSubjCode);
      //String insteadSubjYear = userInsteadClass.get(0).getSubjYear();
      //String insteadSubjSemester = userInsteadClass.get(0).getSubjSemester();
      String insteadScore = userInsteadClass.get(0).getScore();
      
     
      
      String beInsteadSubjMain=null;  //대체될 과목의 이수구분
      String beInsteadSubjName=null;  //대체돌 과목의 이름
      String beInsteadSubjYear=null;  //대체될 과목의 년도
      String beInsteadSubjSemester=null; //대체될 과목의 학기
      String beInsteadSubjGrade=null;  //대체될 과목의 학년
      
      //대체될 과목의 이수구분을 알귀위해
      int subMainexist = new userClassDAO().existClassSubj(BeReplaceSubjCode, userID);
      
      if(subMainexist==1) {
         ArrayList<userClassDTO> userClass = new DepartrDAO().getDepartList(BeReplaceSubjCode, userID);
         beInsteadSubjMain = userClass.get(0).getSubjMain();  //이수구분
         beInsteadSubjName = userClass.get(0).getSubjName();
         beInsteadSubjYear = userClass.get(0).getSubjYear();
         beInsteadSubjSemester = userClass.get(0).getSubjSemester();
      }else {
         ArrayList<DepartrDTO> depart = new DepartrDAO().getDepartList(BeReplaceSubjCode);
          beInsteadSubjMain = depart.get(0).getSubjDivision();  //이수구분
          beInsteadSubjName = depart.get(0).getSubjName();
          beInsteadSubjYear = depart.get(0).getUserAdmission();
          beInsteadSubjSemester = depart.get(0).getSubjSemester();
          beInsteadSubjGrade = depart.get(0).getSubjGrade();
      }
      
     
      int exist = new userClassDAO().existClassSubj(BeReplaceSubjCode, userID); //수강 table에 존재하는지 안하는지 알아보기 위해
      if(exist==1) { //본인 수강 table에 과목이 있을 경우, 즉 재수강일 경우인지
         new userClassDAO().upAndDelInstead(BeReplaceSubjCode, ReplaceSubjCode, insteadScore, userID);
         PrintWriter script=response.getWriter();
         script.println("<script>");
         script.println("alert('대체과목 등록이 완료되었습니다.');");
         script.println("location.href='Graduate.jsp';");
         script.println("</script>");
         script.close();
         return;
      }else {
         new userClassDAO().inAndDelInstead(BeReplaceSubjCode, beInsteadSubjYear, beInsteadSubjSemester, beInsteadSubjName,beInsteadSubjMain, insteadScore, ReplaceSubjCode, beInsteadSubjGrade, userID);
         PrintWriter script=response.getWriter();
         script.println("<script>");
         script.println("alert('대체과목 등록이 완료되었습니다.');");
         script.println("location.href='Graduate.jsp';");
         script.println("</script>");
         script.close();
         return;
      }
      
   }

}