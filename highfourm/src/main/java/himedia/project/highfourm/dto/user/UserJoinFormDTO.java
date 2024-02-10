package himedia.project.highfourm.dto.user;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자 계정 생성

@Builder
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinFormDTO {
	
	private Long empNo;
	
	private String userName;
	
	@NotBlank(message ="아이디를 입력해주세요")
	@Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자 숫자만 사용하여 4~20자리여야 합니다.")
	private String userId;
	
	@NotBlank(message = "비밀번호을 입력해주세요")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 1개 이상 포함, 8~16자리수여야 합니다. ")
	private String password;
	
	@NotBlank(message = "비밀번호 확인을 해주세요")
	private String passwordCheck;
	
	private LocalDate birth;
	
//	public User toEntity(Company company) {
//		return User.builder()
//				.userNo(userNo)
//				.userId(userId)
//				.userName(userName)
//				.empNo(empNo)
//				.birth(birth)
//				.email(email)
//				.registerState("y")
//				.role("USER")
//				.build();
//	}
}
