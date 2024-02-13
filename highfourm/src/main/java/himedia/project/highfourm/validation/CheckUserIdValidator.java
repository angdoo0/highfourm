package himedia.project.highfourm.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import himedia.project.highfourm.dto.user.UserJoinFormDTO;
import himedia.project.highfourm.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CheckUserIdValidator extends AbstractValidator<UserJoinFormDTO>{
	
	private final UserRepository userRepository;
	
	@Override
	protected void doValidate(UserJoinFormDTO dto, Errors errors) {
		if(userRepository.existsByUserId(dto.getUserId())) {
			errors.rejectValue("userId","아이디 중복 오류", "이미 사용 중인 아이디입니다.");
		}
	}

}
