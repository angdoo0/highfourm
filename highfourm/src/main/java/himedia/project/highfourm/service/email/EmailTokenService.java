package himedia.project.highfourm.service.email;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import himedia.project.highfourm.dto.user.UserAddDTO;
import himedia.project.highfourm.entity.EmailToken;
import himedia.project.highfourm.entity.User;
import himedia.project.highfourm.repository.EmailTokenRepository;
import himedia.project.highfourm.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTokenService {
	
	private final EmailTokenRepository emailTokenRepository;
	private final EmailSenderService emailSenderService;
	private final JavaMailSender javaMailSender;
	private final UserService userService;
	
	public String createEmail(UserAddDTO newUser, Authentication authentication) {
	    User savedUser = userService.save(newUser, authentication);

	    EmailToken emailToken = EmailToken.createEmailToken(savedUser);
		emailTokenRepository.save(emailToken);
		
		String body = "<div style='text-align:center; border: 1px solid; padding: 30px'>"
				+ "<h1> 하이포엠 사용자 회원가입 인증 이메일 </h1>"
				+ "<br>"
				+ "<p style='font-size:16px;'>아래 링크를 클릭하면 하이포엠 회원가입 화면으로 이동됩니다.</p>"
				+ "<a href='http://localhost:8080/confirm-email?token=" + emailToken.getId()
				+"&userNo=" + emailToken.getUser().getUserNo().toString() + "' style='font-size:large;'>인증 링크</a>"
				+"</div>";
		System.out.println(emailToken.getId() + "/" + emailToken.getUser().getUserNo().toString());
		
		MimeMessage mailMessage = javaMailSender.createMimeMessage();
		
		 try {
	            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
	            helper.setTo(savedUser.getEmail());
	            helper.setSubject("하이포엠 회원가입 사용자 인증 이메일입니다");
	            helper.setText(body, true);
	            emailSenderService.sendEmail(mailMessage);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
		
		return emailToken.getId();
	}
	
	// 유효한 토큰 가져오기
	public EmailToken findByUserNoAndExpirationDateAfterAndExpired(String emailTokenId) throws Exception {
		Optional<EmailToken> emailToken = emailTokenRepository
				.findByIdAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false);
		
		return emailToken.orElseThrow(() -> new BadRequestException("토큰을 찾을 수 없습니다."));
				
	}
}
