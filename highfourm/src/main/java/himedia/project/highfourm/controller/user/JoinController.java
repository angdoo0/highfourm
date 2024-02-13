package himedia.project.highfourm.controller.user;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import himedia.project.highfourm.dto.user.UserJoinFormDTO;
import himedia.project.highfourm.service.JoinService;
import himedia.project.highfourm.validation.CheckPasswordValidator;
import himedia.project.highfourm.validation.CheckUserIdValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinController {
	
	private final JoinService joinService;
	private final CheckUserIdValidator checkUserIdValidator;
	private final CheckPasswordValidator checkPasswordValidator;
	
	@InitBinder
	public void validatorBinder(WebDataBinder binder) {
		binder.addValidators(checkUserIdValidator);
		binder.addValidators(checkPasswordValidator);
	}
	
	//회원가입 폼
	@GetMapping("/users/join/{empNo}")
	public String signUp(@PathVariable("empNo") Long empNo, Model model) {
		UserJoinFormDTO userJoinFormDTO = joinService.findByEmpNO(empNo);
		model.addAttribute("userJoinFormDTO", userJoinFormDTO);
		return "join";
	}
	
	//회원가입 처리
	@PutMapping("/users/join/")
	public String signUpProcess(@PathVariable("empNo") Long empNo,
			@Valid UserJoinFormDTO joinDTO, BindingResult bindingResult,  Model model) {
		
		if(bindingResult.hasErrors()) {
			// 회원가입 실패 시 데이터 유지
			model.addAttribute("userJoinFormDTO", joinDTO);
			
            Map<String, String> validatorResult = joinService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
			
			return "join";
		}
		
//		if(!joinDTO.getPassword().equals(joinDTO.getPasswordCheck())) {
//			model.addAttribute("userJoinFormDTO", joinDTO);
//			bindingResult.rejectValue("password", "passwordInCorrect",
//					"2개의 패스워드가 일치하지 않습니다.");
//			return "join";
//		}
		
		joinService.joinProcess(joinDTO, empNo);
		return "redirect:/";
	}
	
}
