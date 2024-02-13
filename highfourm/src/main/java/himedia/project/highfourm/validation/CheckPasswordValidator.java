package himedia.project.highfourm.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import himedia.project.highfourm.dto.user.UserJoinFormDTO;

@Component
public class CheckPasswordValidator extends AbstractValidator<UserJoinFormDTO>{

	@Override
	protected void doValidate(UserJoinFormDTO dto, Errors errors) {
		if(!dto.getPassword().equals(dto.getPasswordCheck())) {
			errors.rejectValue("passwordCheck", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
		}
	}

}
