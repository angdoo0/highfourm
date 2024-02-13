package himedia.project.highfourm.service;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;
	private final CompanyRepository companyRepository;
	private final EntityManager em;
	
	public Long findCompanyId(Authentication authentication) {
		User user = repository.findByUserId(authentication.getName());
		return user.getCompany().getCompanyId();
	}

	public List<UserDTO> findAllUsers(Authentication authentication) {
		Long companyId = findCompanyId(authentication);
		Company company = companyRepository.findById(companyId).get();
		List<User> userlist = repository.findAll(companyId);

		List<UserDTO> result = userlist.stream()
				.map(user -> user.toDTO(company))
				.collect(Collectors.toList());

		return result;
	}

	public UserDTO findByUserNo(Long userNo, Authentication authentication) {
		User user = repository.findById(userNo).get();
		Long companyId = findCompanyId(authentication);
		Company company = companyRepository.findById(companyId).get();
		return user.toDTO(company);
	}

	public List<UserDTO> findByEmpNo(Long empNo, Authentication authentication) {
		List<User> userlist = repository.findByAllEmpNo(empNo);
		Long companyId = findCompanyId(authentication);
		Company company = companyRepository.findById(companyId).get();

		return userlist.stream()
				.map(user -> user.toDTO(company))
				.collect(Collectors.toList());
	}

	public List<UserDTO> findByUserName(String name, Authentication authentication) {
		List<User> userlist = repository.findByAllUserName(name);
		Long companyId = findCompanyId(authentication);
		Company company = companyRepository.findById(companyId).get();

		return userlist.stream()
				.map(user -> user.toDTO(company))
				.collect(Collectors.toList());
	}

	public List<UserDTO> findByEmail(String email, Authentication authentication) {
		List<User> userlist = repository.findByAllEmail(email);
		Long companyId = findCompanyId(authentication);
		Company company = companyRepository.findById(companyId).get();

		return userlist.stream()
				.map(user -> user.toDTO(company))
				.collect(Collectors.toList());
	}

	public UserEditDTO findByUserNoforEdit(Long userNo, Authentication authentication) {
		User user = repository.findById(userNo).get();
		Long companyId = findCompanyId(authentication);
		Company company = companyRepository.findById(companyId).get();

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

	public boolean isEmailUnique(String email) {
		return repository.findByUserEmail(email) == null;
	}

	public boolean isEmpNoUnique(Long empNo) {
		return repository.findByEmpNo(empNo) == null;
	}

	public User save(UserAddDTO user, Authentication authentication) {
		Long companyId = findCompanyId(authentication);
		Company company = companyRepository.findById(companyId).get();

		User userEntity = user.toEntity(company);

		User savedUser = repository.save(userEntity);
		return savedUser;
	}

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

	public void delete(Long userNo) {
		repository.deleteById(userNo);
	}

}
