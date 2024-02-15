package himedia.project.highfourm.dto.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import himedia.project.highfourm.entity.Company;
import himedia.project.highfourm.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return "ROLE_" + user.getRole();
			}
		});
		return collection;
	}
	
	public Company getCompany() {
		return user.getCompany();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserId();
	}
	
	//계정이 만료되지 않았는지 리턴한다. ( true : 만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	//계정이 감져있지 않았는지 리턴한다. ( true : 잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	//비밀번호가 만료되지 않았는지 리턴한다. ( true : 만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	//계정 활성화(사용가능)인지 리턴한다. ( true : 활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}

}
