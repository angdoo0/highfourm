package himedia.project.highfourm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import himedia.project.highfourm.dto.user.UserAddDTO;
import himedia.project.highfourm.dto.user.UserDTO;
import himedia.project.highfourm.dto.user.UserEditDTO;
import himedia.project.highfourm.entity.Company;
import himedia.project.highfourm.entity.User;
import himedia.project.highfourm.repository.CompanyRepository;
import himedia.project.highfourm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 한혜림
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;
	private final CompanyRepository companyRepository;
	private final EntityManager em;
	
	/**
	 * 총 관리자의 회사 코드 조회
	 */
	public Company findCompanyId(Authentication authentication) {
		User user = repository.findByUserId(authentication.getName());
		return companyRepository.findById(user.getCompany().getCompanyId()).get();
	}

	/**
	 * 로그인한 총관리자와 동일한 회사 코드를 가진 전체 사용자 조회
	 */
	public List<UserDTO> findAllUsers(Authentication authentication) {
		Company company = findCompanyId(authentication);
		List<User> userlist = repository.findAll(company.getCompanyId());

		List<UserDTO> result = userlist.stream()
				.map(user -> user.toDTO(company)).toList();

		return result;
	}
	
	/**
	 * 사용자 사원명/사번/이메일별 검색
	 */
	public List<UserDTO> search(String searchType, String search, Authentication authentication) {
		Company company = findCompanyId(authentication);
		List<User> userlist = new ArrayList<User>();
		
		if(searchType.equals("사원명")) {
			userlist = repository.findByAllUserName(search, company.getCompanyId());
		} else if(searchType.equals("사번")) {
			userlist = repository.findByAllEmpNo(Long.parseLong(search), company.getCompanyId());
		} else if(searchType.equals("이메일")) {
			userlist = repository.findByAllEmail(search, company.getCompanyId());
		}
		
		return userlist.stream()
				.map(user -> user.toDTO(company)).toList();
	}

	/**
	 * 수정 기능을 위한 사번 조회
	 */
	public UserEditDTO findByEmpNoforEdit(Long empNo, Authentication authentication) {
		Company company = findCompanyId(authentication);
		User user = repository.findByEmpNo(empNo);

		UserDTO userDTO = user.toDTO(company);

		return UserEditDTO.builder()
				.userNo(userDTO.getUserNo())
				.userName(userDTO.getUserName())
				.empNo(userDTO.getEmpNo())
				.position(userDTO.getPosition())
				.birth(userDTO.getBirth())
				.email(userDTO.getEmail())
				.company(userDTO.getCompany())
				.registerState(userDTO.getRegisterState())
				.role(userDTO.getRole())
				.build();
	}

	/**
	 * DB에 동일한 이메일이 존재하는지 확인
	 */
	public boolean isEmailUnique(String email) {
		return repository.findByUserEmail(email) == null;
	}

	/**
	 * DB에 동일한 사번이 존재하는지 확인
	 */
	public boolean isEmpNoUnique(Long empNo) {
		return repository.findByEmpNo(empNo) == null;
	}

	/**
	 * 사용자 등록
	 */
	public User save(UserAddDTO user, Authentication authentication) {
		Company company = findCompanyId(authentication);

		User userEntity = user.toEntity(company);

		User savedUser = repository.save(userEntity);
		return savedUser;
	}

	/**
	 * 사용자 수정 (이름, 직급, 생년월일만 수정 가능)
	 */
	@Transactional
	public UserDTO updateUser(UserEditDTO userEdit) {
		User existingUser = repository.findById(userEdit.getUserNo()).get();

		User updatedUser = User.builder()
				.userNo(existingUser.getUserNo())
				.userId(existingUser.getUserId())
				.password(existingUser.getPassword())
				.userName(userEdit.getUserName())
				.empNo(existingUser.getEmpNo())
				.position(userEdit.getPosition())
				.birth(userEdit.getBirth())
				.email(existingUser.getEmail())
				.company(existingUser.getCompany())
				.registerState(existingUser.getRegisterState()).role(existingUser.getRole()).build();

		User mergedUser = em.merge(updatedUser);

		return mergedUser.toDTO(mergedUser.getCompany());
	}

	/**
	 * 사용자 삭제
	 */
	public void delete(Long userNo) {
		repository.deleteById(userNo);
	}

}
