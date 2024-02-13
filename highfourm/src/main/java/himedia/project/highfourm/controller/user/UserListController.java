package himedia.project.highfourm.controller.user;

import java.util.ArrayList;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserListController {
	
	private final UserService service;
	
	@GetMapping("/api/users")
	public ResponseEntity<List<UserDTO>> selectUserList(Authentication authentication) {
	    List<UserDTO> users = service.findAllUsers(authentication);
	    return ResponseEntity.ok(users);
	}
	
	@GetMapping("/api/users/search")
	public ResponseEntity<List<UserDTO>> searchUserList(@RequestParam(value = "searchType") String searchType, @RequestParam(value = "search") String search
			, Authentication authentication) {
	    List<UserDTO> result = null;
		
			if(searchType.equals("사원명")) {
				result = service.findByUserName(search, authentication);
			} else if(searchType.equals("사번")) {
				result = service.findByEmpNo(Long.parseLong(search), authentication);
			} else if(searchType.equals("이메일")) {
				result = service.findByEmail(search, authentication);
			}
			if (result == null) {
				result = new ArrayList<UserDTO>();
			}
			
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping("/api/users/delete/{deleteUserNo}")
	public String deleteByUserNo(@PathVariable(value = "deleteUserNo") String deleteUserNo) {
		Long userNo = Long.parseLong(deleteUserNo);
		service.delete(userNo);
		
		return "redirect:/users";
	}
}
