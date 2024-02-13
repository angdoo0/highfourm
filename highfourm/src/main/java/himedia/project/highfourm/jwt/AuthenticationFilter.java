//package himedia.project.highfourm.jwt;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import himedia.project.highfourm.dto.user.CustomUserDetails;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//	
//	private final AuthenticationManager authenticationManager;
//	private final JWTUtil jwtUtil;
//	
//	@Override
//	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
//		String userId = request.getParameter("userId");
//		String password = request.getParameter("password");
//		
//		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);
//		// Allow subclasses to set the "details" property
//		setDetails(request, authToken);
//		
//		Authentication authentication = this.getAuthenticationManager().authenticate(authToken);
//		
//		//successfulAuthentication(request, response, getFilterChain(), authentication);
//		
//		return authenticationManager.authenticate(authToken);
//	}
//	
//	//로그인 성공 시 (JWT를 발급)
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//
//        String username = customUserDetails.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();
//
//        String token = jwtUtil.createJwt(username, role, 60*60*10L);
//
//        response.addHeader("Authorization", "Bearer " + token);
//    	
//    }
//
//	//로그인 실패 시
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
//    	response.setStatus(401);
//    }
//	
//}
