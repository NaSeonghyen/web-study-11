package com.saeyan.dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.DBManager;

public class BoardDAO {
	private BoardDAO(){
		super();
	}
	
	private static BoardDAO instance = new BoardDAO();
	
	public static BoardDAO getInstance() {
		return instance;
	}
	
	public List<BoardVO> selectAllBoards() {
		String sql = "SELECT * FROM board ORDER BY num DESC";
		List<BoardVO> list = new ArrayList<BoardVO>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				BoardVO bVo = new BoardVO();
				bVo.setNum(rs.getInt("num"));
				bVo.setName(rs.getString("name"));
				bVo.setEmail(rs.getString("email"));
				bVo.setPass(rs.getString("pass"));
				bVo.setTitle(rs.getString("title"));
				bVo.setContent(rs.getString("content"));
				bVo.setReadcount(rs.getInt("readcount"));
				bVo.setWritedate(rs.getTimestamp("writedate"));
				list.add(bVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(rs, stmt, conn);
		}
		return list;
	}
	
	public void insertBoard(BoardVO bVo) {
		String sql = "INSERT INTO Board("
				+ "num, name, email, pass, title, content)"
				+ " VALUES(Board_SEQ.NEXTVAL,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			conn = DBManager.getConnection();
			psmt = conn.prepareStatement(sql);

			psmt.setString(1 ,bVo.getName());
			psmt.setString(2 ,bVo.getEmail());
			psmt.setString(3, bVo.getPass());
			psmt.setString(4, bVo.getTitle());
			psmt.setString(5, bVo.getContent());
			
			psmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(psmt, conn);
		}
	}
	
	public void updateReadCount(String num) {
		String sql ="UPDATE board SET readcount=readcount + 1  WHERE num=?";
		
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			conn = DBManager.getConnection();
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1 ,num);
			
			psmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(psmt, conn);
		}
	}
	
	//게시판 글 상세 내용 보기 : 글번호로 찾아온다. : 실패 null,
	public BoardVO selectOneBoardByNum(String num) {
		String sql ="SELECT * FROM Board WHERE num=?";
		
		BoardVO bVo = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBManager.getConnection();
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, num);			
			
			rs = psmt.executeQuery();
			
			if(rs.next()){
				bVo = new BoardVO();
				
				bVo.setNum(rs.getInt("num"));
				bVo.setName(rs.getString("name"));
				bVo.setPass(rs.getString("pass"));
				bVo.setEmail(rs.getString("email"));
				bVo.setTitle(rs.getString("title"));
				bVo.setContent(rs.getString("content"));
				bVo.setWritedate(rs.getTimestamp("writedate"));
				bVo.setReadcount(rs.getInt("readcount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(rs, psmt, conn);
		}
		return bVo;
	}
	
	public void updateBoard(BoardVO bVo) {
		String sql ="UPDATE board SET name=?,email=?,pass=?,title=?,content=? WHERE num=?";
		
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			conn = DBManager.getConnection();
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1 ,bVo.getName());
			psmt.setString(2 ,bVo.getEmail());
			psmt.setString(3, bVo.getPass());
			psmt.setString(4, bVo.getTitle());
			psmt.setString(5, bVo.getContent());
			psmt.setInt(6, bVo.getNum());
			
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(psmt, conn);
		}
	}
	
	public BoardVO checkPassWord(String pass, String num) {
		String sql ="select * from board where pass=? and num=?";
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		BoardVO bVo = null;
		try {
			conn = DBManager.getConnection();
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, pass);	
			psmt.setString(2, num);	
			
			psmt.executeUpdate();
			
			if(rs.next()){
				bVo = new BoardVO();
				
				bVo.setNum(rs.getInt("num"));
				bVo.setName(rs.getString("name"));
				bVo.setPass(rs.getString("pass"));
				bVo.setEmail(rs.getString("email"));
				bVo.setTitle(rs.getString("title"));
				bVo.setContent(rs.getString("content"));
				bVo.setReadcount(rs.getInt("readcount"));
				bVo.setWritedate(rs.getTimestamp("writedate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return bVo;
	}
	
	public void deleteBoard(String num) {
		String sql ="DELETE FROM Board WHERE num=?";
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			conn = DBManager.getConnection();
			psmt = conn.prepareStatement(sql);
			//?바인딩 변수에 값 매핑
			psmt.setString(1, num);	
			
			psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(psmt, conn);
		}
	}
}
