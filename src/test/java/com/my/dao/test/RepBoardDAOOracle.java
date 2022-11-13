package com.my.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.my.dao.RepBoardDAO;
import com.my.exception.FindException;
import com.my.vo.RepBoard;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })

public class RepBoardDAOOracle {
		
	@Autowired
	@Qualifier("oracle")
	private RepBoardDAO dao;
	// private RepBoardDAO dao = new com.my.dao.RepBoardDAOOracle();

	@Test 
	public void selectByBoard_no() {
				
		int board_no =1;
		int expParent_no = 0;
		String expBoard_title = "벌써졸리네";
		String expBoard_writer = "sangwoo";
		
		try {
			RepBoard b = dao.selectByBoard_no(board_no);
			assertEquals(expBoard_title, b.getBoard_title());
			assertTrue(expParent_no == b.getParent_no());
		} catch (FindException e) {
			
			e.printStackTrace();
		}
		//fail("Not yet implemented");
	}
	
	
	//@Test
	public void selectAll() throws FindException {
		
		List<RepBoard> list= dao.selectAll();
		int expListSize = 15;
		assertTrue(expListSize == list.size());
	}
	

}
