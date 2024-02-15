package himedia.project.highfourm.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import himedia.project.highfourm.dto.user.UserDTO;
import himedia.project.highfourm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author 한혜림
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserListController {
	
	private final UserService service;
	
	/**
	 * 사용자 관리 페이지
	 */
	@GetMapping("/api/users")
	public ResponseEntity<List<UserDTO>> selectUserList(Authentication authentication) {
	    List<UserDTO> users = service.findAllUsers(authentication);
	    return ResponseEntity.ok(users);
	}
	
	/**
	 * 사용자 검색
	 */
	@GetMapping("/api/users/search")
	public ResponseEntity<List<UserDTO>> searchUserList(@RequestParam(value = "searchType") String searchType, @RequestParam(value = "search") String search
			, Authentication authentication) {
	    List<UserDTO> result = service.search(searchType, search, authentication);

	    return ResponseEntity.ok(result);
	}
	
	/**
	 * 사용자 삭제
	 */
	@DeleteMapping("/api/users/delete/{deleteUserNo}")
	public String deleteByUserNo(@PathVariable(value = "deleteUserNo") String deleteUserNo) {
		Long userNo = Long.parseLong(deleteUserNo);
		service.delete(userNo);
		
		return "redirect:/users";
	}
}
