package himedia.project.highfourm.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.user.CustomUserDetails;
import himedia.project.highfourm.entity.User;
import himedia.project.highfourm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
/*
 * 왜 굳이 Service계층을 따로 관리하는가?
 * 로그인 처리를 빼앗겼다 - 제어권이 스프링에게 있음 - IoC - 제어역전, 역제어
 * 스프링 시큐리티 - filter chain - 
 * 보안상 세션관리 따로 진행함
 * Authentication은 UserDetails타입만 받아 준다
 * <bean class="구현체클래스" type="추상클래스 or 인터페이스"/>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userRepository.findByUserId(userId);
		
		if(user != null) {
			return new CustomUserDetails(user);
		}
		throw new UsernameNotFoundException(userId);
	}

}
