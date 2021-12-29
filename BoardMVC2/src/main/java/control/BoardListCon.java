package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BoardBean;
import model.BoardDAO;

// ★★ 서블릿 클래스를 가장 먼저 실행 ★★
@WebServlet("/BoardListContoller.do")
public class BoardListCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);

	}

	protected void reqPro(HttpServletRequest reqeust, HttpServletResponse response) throws ServletException, IOException {
		// 화면에 보여질 게시글의 개수를 지정
		int pageSize = 10;
		// 현재 보여지고 있는 페이지의 넘버값을 읽어들임
		// 현재 카운터를 클릭한 번호 값을 읽어옴
		String pageNum = reqeust.getParameter("pageNum");
		// 만약 처음 boardList.jsp를 클릭하거나 수정 삭제 등 다른 게시글에서 이 페이지로 넘어오면
		// pageNum 값이 없기에 null 값 처리
		if(pageNum == null) {
			pageNum = "1";
		}
		// 전체 게시글의 갯수
		int count = 0;
		// jsp 페이지 내에서 보여질 넘버링 숫자를 저장하는 변수
		int number = 0;
		
		// 현재 보여지고 있는 페이지 문자를 숫자로 변환
		int currentPage = Integer.parseInt(pageNum);
		// 전체 게시글의 갯수를 가져와야 하기에 데이터베이스 객체 생성
		BoardDAO bdao = new BoardDAO();
		// 전체 게시글의 개수를 읽어 들인 메소드 호출
		count = bdao.getAllCount();
		
		// 현재 보여질 페이지 시작 번호를 설정
		int startRow = (currentPage-1) * pageSize+1;
		int endRow = currentPage * pageSize;
		
		// 최신글 10개를 기준으로 게시글을 리턴 받아주는 메소드 호출
		// 여러개의 물건(데이터)을 가방(BoardBean)에 넣고 박스(Vector)에 담아서 여러개의 데이터를 리턴
		ArrayList<BoardBean> list = bdao.getAllBoard(startRow, endRow); 
		number = count - (currentPage - 1) * pageSize;
		
		///// 수정 삭제시 비밀번호가 틀렸다면
		String msg = (String) reqeust.getAttribute("msg");
		/// boardList.jsp 쪽으로 request 객체에 담아서 넘겨줌
		// ArrayList에 최신글 10개를 기준으로 받는다.
		reqeust.setAttribute("list", list);
		reqeust.setAttribute("number", number);
		reqeust.setAttribute("pageSize", pageSize);
		reqeust.setAttribute("count", count);
		reqeust.setAttribute("currentPage", currentPage);
		reqeust.setAttribute("msg", msg);
		
		
		RequestDispatcher dis = reqeust.getRequestDispatcher("boardList.jsp");
		dis.forward(reqeust, response);
		
	}
		
}
