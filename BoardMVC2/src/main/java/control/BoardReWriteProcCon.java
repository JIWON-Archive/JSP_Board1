package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BoardBean;
import model.BoardDAO;

@WebServlet("/BoardReWriteProcCon.do")
public class BoardReWriteProcCon extends HttpServlet {
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	reqPro(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	reqPro(request, response);

	}
	
    protected void reqPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//한글 처리
    	request.setCharacterEncoding("UTF-8");
    	// bean에 넘어온 데이터 저장 -> 게시글 읽기에서 답변 글쓰기를 클릭하면 넘겨주는 데이터를 받아줌
    	BoardBean bean = new BoardBean();
    	bean.setWriter(request.getParameter("writer"));
    	bean.setSubject(request.getParameter("subject"));
    	bean.setEmail(request.getParameter("email"));
    	bean.setPassword(request.getParameter("password"));
    	bean.setContent(request.getParameter("content"));
    	
    	bean.setRef(Integer.parseInt(request.getParameter("ref")));
    	bean.setReStep(Integer.parseInt(request.getParameter("reStep")));
    	bean.setReLevel(Integer.parseInt(request.getParameter("reLevel")));
    	
    	BoardDAO bdao = new BoardDAO();
    	bdao.reInsertBoard(bean);
    	
    	RequestDispatcher dis = request.getRequestDispatcher("BoardListContoller.do");
    	dis.forward(request, response);
    }
}
