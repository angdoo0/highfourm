package himedia.project.highfourm.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import himedia.project.highfourm.dto.user.UserJoinFormDTO;
import himedia.project.highfourm.entity.User;
import himedia.project.highfourm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// 회원가입
	public void joinProcess(UserJoinFormDTO joinDTO, Long empNo) {
		String userId = joinDTO.getUserId();
		String password = joinDTO.getPassword();
		String encodedPassword = bCryptPasswordEncoder.encode(password);
		
		Boolean isExist = userRepository.existsByUserId(userId);
		
		if (isExist) {
			return;
		}
		
		User user = userRepository.findByEmpNo(empNo);
		
		user.joinUser(userId, encodedPassword);
		
		userRepository.save(user);
	}
	
    // 회원가입 시, 유효성 체크
    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }
	
	@Transactional
	public Boolean checkUserId(String userId) {
		return userRepository.existsByUserId(userId);
	}
	
	public UserJoinFormDTO findByEmpNO(Long empNo) {
		User user = userRepository.findByEmpNo(empNo);
		
		return UserJoinFormDTO.builder()
							.empNo(user.getEmpNo())
							.userName(user.getUserName())
							.userId(null)
							.password(null)
							.birth(user.getBirth())
							.build();
	}
}
