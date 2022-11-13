package com.my.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.RepBoard;

import lombok.extern.log4j.Log4j;

@Repository
@Qualifier(value = "oracle")
@Log4j
public class RepBoardDAOOracle implements RepBoardDAO {
	
	@Autowired
	private DataSource ds;  //DataSource인터페이스를 구현한 클래스가 SimpleDriverDataSource
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	public void delete(int board_no, String board_pwd) throws RemoveException {	
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();  // 컴파일 에러 발생하지 않고 실행 시 에러 발생 (openSession() 메소드)
			Map<String, Object> map = new HashMap<>();
			map.put("board_no", board_no);
			map.put("board_pwd", board_pwd);
			int rowcnt = session.delete("mybatis.RepBoardMapper.delete", map);
			if(rowcnt == 0) {
				throw new RemoveException("글번호가 없거나 비밀번호가 다릅니다.");
			}
			session.commit();
		} catch(Exception e) {
			throw new RemoveException(e.getMessage());
		} finally {
			if(session != null) session.close();  // close()하지 않으면 hicaricp 타임아웃 발생
		}	
	}
	
	public void update(RepBoard board, String board_pwd) throws ModifyException{
		if(board.getBoard_title() == null && board.getBoard_pwd() == null) {
			throw new ModifyException("수정 할 내용이 없습니다.");
		}
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("board", board);
			map.put("board_pwd", board_pwd);
			int rowcnt = session.update("mybatis.RepBoardMapper.update", map);
			if(rowcnt == 0) {
				throw new ModifyException("게시글이 없습니다.");
			}
			session.commit();
		} catch(Exception e) {
			throw new ModifyException(e.getMessage());
		} finally {
			if(session != null) session.close();  // close()하지 않으면 hicaricp 타임아웃 발생
		}
	}
	
	public void updateBoardCnt(int board_no) throws ModifyException{
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			int rowcnt = session.update("mybatis.RepBoardMapper.updateBoardCnt", board_no);
			if(rowcnt == 0) {
				throw new ModifyException("게시글이 없습니다");
			}
			session.commit();
		} catch(Exception e) {
			throw new ModifyException(e.getMessage());
		} finally {
			if(session != null) session.close();
		}
	}
	
	public RepBoard selectByBoard_no(int board_no) throws FindException{
		
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			RepBoard b = session.selectOne("mybatis.RepBoardMapper.selectByBoard_no", board_no);
			if(b == null) {
				throw new FindException("게시글이 없습니다.");
			}
			return b;
		} catch (Exception e) {
			// TODO: handle exception
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) session.close();
		}
		

	}
	
	public List<RepBoard> selectByBoard_titleORBoard_writer(String word) throws FindException{
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("word", word);
			map.put("o", "board_no DESC");
			
			List<RepBoard> list = session.selectList("mybatis.RepBoardMapper.selectByBoard_titleORBoard_writer", map);
			if(list.size() == 0) {
				throw new FindException("게시글이 없습니다.");
			}
			return list;
		} catch(Exception e) {
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) session.close();
		}
	}
	
	public List<RepBoard> selectAll() throws FindException{
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			List<RepBoard> list = session.selectList("mybatis.RepBoardMapper.selectAll");
			if(list.size() == 0) {
				throw new FindException("게시글이 없습니다.");
			}
			return list;
		} catch(Exception e) {
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) session.close();
		}
	
	}
	
	public void insert(RepBoard board) throws AddException{
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			session.insert("mybatis.RepBoardMapper.insert", board);
			session.commit();
		} catch (Exception e) {
			throw new AddException(e.getMessage());
		} finally {
			if(session != null) session.close();
		}
	}
}
