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
	
	// ��ü �Խñ��� ������ �����ϴ� �޼ҵ�
	public int getAllCount() {
		// �Խñ� ��ü ���� �����ϴ� ����
		int count = 0;
		getConn();
		
		try {
			// �����غ�
			String sql = "SELECT COUNT(*) FROM BOARD";
			// ������ ������ ��ü ����
			pstmt = con.prepareStatement(sql);
			// ���� ���� �� ����� ����
			rs = pstmt.executeQuery();
			if(rs.next()) { // �����Ͱ� �ִٸ�
				count = rs.getInt(1); // ��ü �Խñ� ��
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	// ���(ȭ�鿡 ������ �����͸� 10���� �����ؼ� �����ϴ� �޼ҵ�
	public ArrayList<BoardBean> getAllBoard(int startRow, int endRow) {
		ArrayList<BoardBean> list = new ArrayList<BoardBean>();
		
		getConn();
		
		try {
			// ���� �ۼ� (�ֽű� 10��)
			String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (SELECT * FROM BOARD ORDER BY REF DESC, RE_STEP ASC) A)"
						+ " WHERE RNUM >= ? AND RNUM <= ?";
			// ���� ������ ��ü ����
			pstmt = con.prepareStatement(sql);
			// ? �� ����
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			// ������ ������ ����� �𸣱⿡ �ݺ����� �̿��Ͽ� �����͸� ����
			while(rs.next()) {
				// �����͸� ��Ű¡(���� = BoardBean Ŭ������ �̿�) ����
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
				// ��Ű¡�� �����͸� ArrayList�� ����
				list.add(bean);
			}
			con.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// �ϳ��� �Խñ��� �����ϴ� �޼ҵ� ȣ�� REF �� �׷�
	public void insertBoard(BoardBean bean) {
		
		getConn();
		// ��Ŭ������ �Ѿ���� �ʾҴ� �����͵��� �ʱ�ȭ ���־���Ѵ�.
		int ref = 0;	// �۱׷��� �ǹ� = ������ ������Ѽ� ���� ū ref ���� ������ �� +1�� �����ָ� ��
		int reStep = 1; // �� ���̱⿡ = �θ���̱⿡
		int reLevel = 1; // �� ��
		try {
			// ���� �ۼ�
			String refsql = "SELECT MAX(REF) FROM BOARD";
			// ���� ���� ��ü
			pstmt = con.prepareStatement(refsql);
			// ���� ���� �� ����� ����
			rs = pstmt.executeQuery();
			if(rs.next()) { // ������� �ִٸ�
				ref = rs.getInt(1) + 1; // ���� ū ���� 1�� ������
			}
			// �����͸� �����ϴ� ���� ������ ���
			String sql = "INSERT INTO BOARD VALUES(BOARD_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,0,?)";
			// ? ����
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
	
	// �ϳ��� �Խñ��� �о���̴� �޼ҵ� �ۼ� -> �Խñ��� Ŭ���ϴ� ���� ��ȸ���� �ö�
	public BoardBean getOneBoard(int num) {
		getConn();
		BoardBean bean = null;
		
		try {
			// �ϳ��� �Խñ��� �о��ٴ� ��ȸ�� ����
			String countsql = "UPDATE BOARD SET READCOUNT = READCOUNT + 1 WHERE NUM = ?";
			pstmt = con.prepareStatement(countsql);
			pstmt.setInt(1, num);
			// ���� ����
			pstmt.executeUpdate();
			
			// �� �Խñۿ� ���� ������ �������ִ� ������ �ۼ�
			String sql = "SELECT * FROM BOARD WHERE NUM = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// ���� ���� �� ����� ����
			rs = pstmt.executeQuery();
			if(rs.next()) { // �ϳ��� �Խñ��� �����Ѵٸ�
				// �����͸� ��Ű¡(���� = BoardBean Ŭ������ �̿�) ����
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
	// �亯���� �����ϴ� �޼ҵ�
	public void reInsertBoard(BoardBean bean) {
		getConn();
		int ref = bean.getRef(); // ������ ������ ������
		int reStep = bean.getReStep();
		int reLevel = bean.getReLevel();
		try {
			////////// �ٽ� �ڵ�
			String levelsql = "UPDATE BOARD SET RE_LEVEL = RE_LEVEL + 1 WHERE REF=? AND RE_LEVEL > ?"; // ���� �۱׷� �� �ۺ��� ū ����
			// ���� ���� ��ü
			pstmt = con.prepareStatement(levelsql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, reLevel);
			// ���� ���� �� ����� ����
			pstmt.executeUpdate();
			// �����͸� �����ϴ� ���� ������ ���
			String sql = "INSERT INTO BOARD VALUES(BOARD_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,0,?)";
			// ? ����
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, reStep+1); // ���� �θ���� ���ܺ��� 1�� ����
			pstmt.setInt(7, reLevel); // ���� �θ���� ���ܺ��� 1�� ����
			pstmt.setString(8, bean.getContent());
			
			pstmt.executeUpdate();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ��ȸ���� �������� �ʴ� �ϳ��� �Խñ��� �����ϴ� �޼ҵ�
	public BoardBean getOneUpdateBoard(int num) {
		getConn();
		BoardBean bean = null;
		
		try {
						// �� �Խñۿ� ���� ������ �������ִ� ������ �ۼ�
			String sql = "SELECT * FROM BOARD WHERE NUM = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// ���� ���� �� ����� ����
			rs = pstmt.executeQuery();
			if(rs.next()) { // �ϳ��� �Խñ��� �����Ѵٸ�
				// �����͸� ��Ű¡(���� = BoardBean Ŭ������ �̿�) ����
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
	
	// �ϳ��� �Խñ��� �����ϴ� �޼���
	public void updateBoard(int num, String subject, String content) {
		// ������ ���̽� ����
		getConn();
		try {
			// ���� �غ� ���� ������ ��ü ����
			String sql = "UPDATE BOARD SET SUBJECT = ?, CONTENT = ? WHERE NUM = ?";
			pstmt = con.prepareStatement(sql);
			// ? �� ����
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setInt(3, num);
			// ���� ����
			pstmt.executeUpdate();
			// �ڿ� �ݳ�
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// �ϳ��� �Խñ��� �����ϴ� �޼ҵ�
	public void deleteBoard(int num) {
		getConn();
		
		try {
			// �����غ� 
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
