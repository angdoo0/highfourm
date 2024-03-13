package himedia.project.highfourm.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogincController {
	
	//로그인 페이지
	@GetMapping("/")
	public String login() {
		return "login";
	}
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
}




