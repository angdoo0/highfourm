package himedia.project.highfourm.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogincController {
	
	//로그인 페이지
	@GetMapping("/")
	public String login() {
		return "login";
	}
	
}




