package himedia.project.highfourm.dto.user;

import java.time.LocalDate;

import himedia.project.highfourm.entity.Company;
import himedia.project.highfourm.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 한혜림
 * 사용자 수정용 DTO
 */
@Builder
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {
	
	@NotNull
	private Long userNo;
	
	@NotBlank(message = "이름을 입력해주세요")
	private String userName;
	
	@NotNull
	private Long empNo;
	
	@NotBlank(message = "직급을 입력해주세요")
	private String position;
	
	@Past
	@NotNull(message = "생년월일을 입력해주세요")
	private LocalDate birth;
	
	@Email
	@NotBlank
	private String email;
	
	private Company company;
	
	private String registerState;
	
	private String role;
	
	
	public User toEntity(Company company) {
		return User.builder()
				.userNo(userNo)
				.userName(userName)
				.empNo(empNo)
				.position(position)
				.birth(birth)
				.email(email)
				.company(company)
				.build();
	}
	
}