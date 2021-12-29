package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BoardDAO {
	String id = "HUEKA";
	String pw = "0814";
	String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
	String driver = "oracle.jdbc.driver.OracleDriver";
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public void getConn() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 전체 게시글의 개수를 리턴하는 메소드
	public int getAllCount() {
		// 게시글 전체 수를 저장하는 변수
		int count = 0;
		getConn();
		
		try {
			// 쿼리준비
			String sql = "SELECT COUNT(*) FROM BOARD";
			// 쿼리를 실행할 객체 선언
			pstmt = con.prepareStatement(sql);
			// 쿼리 실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if(rs.next()) { // 데이터가 있다면
				count = rs.getInt(1); // 전체 게시글 수
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	// 모든(화면에 보여질 데이터를 10개씩 추출해서 리턴하는 메소드
	public ArrayList<BoardBean> getAllBoard(int startRow, int endRow) {
		ArrayList<BoardBean> list = new ArrayList<BoardBean>();
		
		getConn();
		
		try {
			// 쿼리 작성 (최신글 10개)
			String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT * FROM BOARD ORDER BY REF DESC, RE_STEP ASC) A)"
						+ " WHERE RNUM >= ? AND RNUM <= ?";
			// 쿼리 실행할 객체 선언
			pstmt = con.prepareStatement(sql);
			// ? 값 대입
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			// 데이터 개수가 몇개인지 모르기에 반복문을 이용하여 데이터를 추출
			while(rs.next()) {
				// 데이터를 패키징(가방 = BoardBean 클래스를 이용) 해줌
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setRegDate(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setReStep(rs.getInt(8));
				bean.setReLevel(rs.getInt(9));
				bean.setReadCount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				// 패키징한 데이터를 ArrayList에 저장
				list.add(bean);
			}
			con.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 하나의 게시글을 저장하는 메소드 호출 REF 글 그룹
	public void insertBoard(BoardBean bean) {
		
		getConn();
		// 빈클래스에 넘어오지 않았던 데이터들을 초기화 해주어야한다.
		int ref = 0;	// 글그룹을 의미 = 쿼리를 실행시켜서 가장 큰 ref 값을 가져온 후 +1을 더해주면 됨
		int reStep = 1; // 새 글이기에 = 부모글이기에
		int reLevel = 1; // 새 글
		try {
			// 쿼리 작성
			String refsql = "SELECT MAX(REF) FROM BOARD";
			// 쿼리 실행 객체
			pstmt = con.prepareStatement(refsql);
			// 쿼리 실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if(rs.next()) { // 결과값이 있다면
				ref = rs.getInt(1) + 1; // 가장 큰 값에 1을 더해줌
			}
			// 데이터를 삽입하는 쿼리 시퀀스 사용
			String sql = "INSERT INTO BOARD VALUES(BOARD_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,0,?)";
			// ? 매핑
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, reStep);
			pstmt.setInt(7, reLevel);
			pstmt.setString(8, bean.getContent());
			
			pstmt.executeUpdate();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 하나의 게시글을 읽어들이는 메소드 작성 -> 게시글을 클릭하는 순간 조회수도 올라감
	public BoardBean getOneBoard(int num) {
		getConn();
		BoardBean bean = null;
		
		try {
			// 하나의 게시글을 읽었다는 조회수 증가
			String countsql = "UPDATE BOARD SET READCOUNT = READCOUNT + 1 WHERE NUM = ?";
			pstmt = con.prepareStatement(countsql);
			pstmt.setInt(1, num);
			// 쿼리 실행
			pstmt.executeUpdate();
			
			// 한 게시글에 대한 정보를 리턴해주는 쿼리를 작성
			String sql = "SELECT * FROM BOARD WHERE NUM = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// 쿼리 실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if(rs.next()) { // 하나의 게시글이 존재한다면
				// 데이터를 패키징(가방 = BoardBean 클래스를 이용) 해줌
				bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setRegDate(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setReStep(rs.getInt(8));
				bean.setReLevel(rs.getInt(9));
				bean.setReadCount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	// 답변글을 저장하는 메소드
	public void reInsertBoard(BoardBean bean) {
		getConn();
		int ref = bean.getRef(); // 기존의 데이터 가져옴
		int reStep = bean.getReStep();
		int reLevel = bean.getReLevel();
		try {
			////////// 핵심 코드
			String levelsql = "UPDATE BOARD SET RE_LEVEL = RE_LEVEL + 1 WHERE REF=? AND RE_LEVEL > ?"; // 같은 글그룹 내 글보다 큰 레벨
			// 쿼리 실행 객체
			pstmt = con.prepareStatement(levelsql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, reLevel);
			// 쿼리 실행 후 결과를 리턴
			pstmt.executeUpdate();
			// 데이터를 삽입하는 쿼리 시퀀스 사용
			String sql = "INSERT INTO BOARD VALUES(BOARD_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,0,?)";
			// ? 매핑
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, reStep+1); // 기존 부모글의 스텝보다 1을 증가
			pstmt.setInt(7, reLevel); // 기존 부모글의 스텝보다 1을 증가
			pstmt.setString(8, bean.getContent());
			
			pstmt.executeUpdate();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 조회수를 증가하지 않는 하나의 게시글을 리턴하는 메소드
	public BoardBean getOneUpdateBoard(int num) {
		getConn();
		BoardBean bean = null;
		
		try {
						// 한 게시글에 대한 정보를 리턴해주는 쿼리를 작성
			String sql = "SELECT * FROM BOARD WHERE NUM = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// 쿼리 실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if(rs.next()) { // 하나의 게시글이 존재한다면
				// 데이터를 패키징(가방 = BoardBean 클래스를 이용) 해줌
				bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setRegDate(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setReStep(rs.getInt(8));
				bean.setReLevel(rs.getInt(9));
				bean.setReadCount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	// 하나의 게시글을 수정하는 메서드
	public void updateBoard(int num, String subject, String content) {
		// 데이터 베이스 연결
		getConn();
		try {
			// 쿼리 준비 쿼리 실행할 객체 선언
			String sql = "UPDATE BOARD SET SUBJECT = ?, CONTENT = ? WHERE NUM = ?";
			pstmt = con.prepareStatement(sql);
			// ? 값 대입
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setInt(3, num);
			// 쿼리 실행
			pstmt.executeUpdate();
			// 자원 반납
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 하나의 게시글을 삭제하는 메소드
	public void deleteBoard(int num) {
		getConn();
		
		try {
			// 쿼리준비 
			String sql = "DELETE FROM BOARD WHERE NUM = ?";
			pstmt = con.prepareStatement(sql);
			// ?
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			con.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
