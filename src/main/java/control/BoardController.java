package control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.service.RepBoardService;
import com.my.vo.RepBoard;

import lombok.extern.log4j.Log4j;

@CrossOrigin("*")
@Controller
@Log4j
public class BoardController {
	
	@Autowired
	private RepBoardService service;
	// 컨텍스트이름이 springmvc인데 브라우저에 a라고 해야 요청이된다.
	@RequestMapping("/detail")
	@ResponseBody
	public Map<String, Object> detail(@RequestParam(required = false, defaultValue = "1") int board_no) throws Exception {		
		Map<String, Object> map = new HashMap<String, Object>();
		try {	
			// 2. 비즈니스로직 호출
			RepBoard board = service.findByBoard_no(board_no);	
			map.put("status", 1);
			map.put("board", board);	
		} catch (FindException e) {
			e.printStackTrace();
			map.put("status", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@GetMapping(value="/list")
	@ResponseBody
	public Map<String, Object> list(String word) throws Exception {
		
		// String word = request.getParameter("word");
		log.info("검색어: " + word);
		List<RepBoard> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(word == null) {
				list = service.findAll();
			} else {
				list = service.findByBoard_titleORBoard_writer(word);
			}
			map.put("list", list);
			map.put("status", 1);			
		} catch(FindException e) {
			log.info(e.getMessage());
			map.put("status", -1);
			map.put("msg", e.getMessage());
		}
		return map;		
	}
	
	@RequestMapping("/modify")
	@ResponseBody // @ResponseBody어노테이션이 있어야, 프론트화면이 없이도 브라우저에서 요청가능??
	public Map<String, Object> modify(RepBoard board, String certify_board_pwd) throws Exception {

		Map<String, Object> map = new HashMap<>();
		try {
			service.modify(board, certify_board_pwd);
			map.put("status", 1);
			
		} catch (ModifyException e) {
			map.put("status", -1);
			map.put("msg", e.getMessage());
		}
		
		return map;
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public Map<String, Object> remove(@RequestParam(required = false, defaultValue = "1") int board_no, String certify_board_pwd) throws Exception {

		Map<String, Object> map = new HashMap<>();
		
		try {
			service.remove(board_no, certify_board_pwd);
			map.put("status", 1);
		} catch (RemoveException e) {
			map.put("status", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@RequestMapping("/reply")
	@ResponseBody
	public Map<String, Object> reply(RepBoard board) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			service.writeReply(board);
			map.put("status", 1);
			
		} catch (AddException e) {
			e.printStackTrace();
			map.put("status", -1);
			map.put("msg", e.getMessage());
		}
		return map;
		
	}
	
	@RequestMapping("/write")
	@ResponseBody // json타입으로 반환하겠다는 뜻
	// 매개변수가 RepBoard타입인데, 자동으로 set메소드가 실행되어 주입이 됨. 어떻게 이게 가능한거지??
	public Map<String, Object> write(RepBoard board) throws Exception {
				
		Map<String, Object> map = new HashMap<>();
		
		try {
			// 비즈니스 호출
			service.writeBoard(board);
			map.put("status", 1);
		} catch (AddException e) {
			e.printStackTrace();
			map.put("status", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}

}
