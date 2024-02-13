package himedia.project.highfourm.service.email;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import himedia.project.highfourm.entity.EmailToken;
import himedia.project.highfourm.entity.User;
import himedia.project.highfourm.repository.EmailTokenRepository;
import himedia.project.highfourm.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

	private final UserRepository repository;
	private final EmailTokenRepository emailTokenRepository;
	
	// 유효한 토큰 가져오기
	public EmailToken findByUserNoAndExpirationDateAfterAndExpired(String emailTokenId) throws Exception {
		Optional<EmailToken> emailToken = emailTokenRepository
				.findByIdAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false);
		
		return emailToken.orElseThrow(() -> new BadRequestException("토큰을 찾을 수 없습니다."));
				
	}
	
	public void confirmEmail(String token) throws Exception {
		EmailToken findEmailToken = findByUserNoAndExpirationDateAfterAndExpired(token);
		
		Optional<User> findUser = repository.findById(findEmailToken.getUser().getUserNo());
		findEmailToken.setTokenToUsed();
		
        if (findUser.isPresent()) {
           findUser.get();
        } else {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
	}
}