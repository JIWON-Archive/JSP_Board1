package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BoardDAO;

@WebServlet("/BoardUpdateProcCon.do")
public class BoardUpdateProcCon extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
	}
	
	protected void reqPro(HttpServletRequest request ,HttpServletResponse response) throws ServletException, IOException {
		// 한글 처리
		request.setCharacterEncoding("UTF-8");
		// 폼에서 넘어온 데이터를 받아줌
		int num = Integer.parseInt(request.getParameter("num"));
		String password = request.getParameter("password"); // 사용자로부터 입력 받은 패스워드
		String pass = request.getParameter("pass"); // 실제 데이터베이스에 저장되어 있는 패스워드 값인
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		// password 값과 pass 값을 비교
		if(password.equals(pass)) { // 패스워드가 같다면 데이터를 수정
			BoardDAO bdao = new BoardDAO(); // 객체 생성 이유 -> 멤버 변수, 멤버변수를 호출하려고
			bdao.updateBoard(num, subject, content);
			// 수정이 완료되었다면 전체 게시글 보기로 이동
//			request.setAttribute("msg", "수정이 완료되었습니다.");
			RequestDispatcher dis = request.getRequestDispatcher("BoardListContoller.do");
			dis.forward(request, response); 
	
		} else {
			// 비밀번호가 틀렸다면 이전 페이지로 이동
			request.setAttribute("msg", "1");
			RequestDispatcher dis = request.getRequestDispatcher("BoardListContoller.do");
			dis.forward(request, response); 
	
		}
	}
}
