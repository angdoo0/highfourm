package himedia.project.highfourm.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogincController {
	
	//로그인 페이지
	@GetMapping("/login")
	public String login(Model model) {
		
		return "login";
	}
	@GetMapping("/")
	public String loginTwo() {
		return "login";
	}
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
}




