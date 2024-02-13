//package himedia.project.highfourm.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import himedia.project.highfourm.jwt.AuthenticationFilter;
//import himedia.project.highfourm.jwt.JWTFilter;
//import himedia.project.highfourm.jwt.JWTUtil;
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class HttpSecurityConfig {
//	
//	private final AuthenticationConfiguration authenticationConfiguration;
//	private final JWTUtil jwtUtil;
//	
//	@Bean
//	protected AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//		return configuration.getAuthenticationManager();
//	}
//	
//	@Bean
//	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		
//		http
//		.csrf(csrf -> csrf.disable());
//
//		http
//				.httpBasic(httpBasic -> httpBasic.disable());
//		
////		http
////				.formLogin(formLogin -> formLogin.disable());
//		
//		http
//				.authorizeHttpRequests(authorizeRequest -> 
//					authorizeRequest
//					.requestMatchers("/", "/notice", "/users/join").permitAll()
//					.requestMatchers("/users/**").hasRole("ADMIN")
//					.anyRequest().authenticated()
//					);
//		
//		//필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
//		http
//		.addFilterBefore(new JWTFilter(jwtUtil), AuthenticationFilter.class)
//		.addFilterAt(new AuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
//		http
//				.sessionManagement(session -> session
//						.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//		//http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
//		http
//			.formLogin(formlogin -> 
//				formlogin
//				//.loginPage("/")
//				.defaultSuccessUrl("http://localhost:3000/orders")
//				.loginProcessingUrl("/login")
//				.usernameParameter("userId")
//				.passwordParameter("password")
//			);
////		httpSecurity
////			.logout(logout -> logout
////					.logoutUrl("/logout")
////					.logoutSuccessUrl("/"
////							)
////					.invalidateHttpSession(true)
////					);
//		
//		return http.build();
//	}
//	
//	@Bean
//	protected BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//}
