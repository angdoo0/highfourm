package himedia.project.highfourm.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "accessDenied";
	}

}
