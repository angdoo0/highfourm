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

/**
 * @author 한혜림
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

	private final UserRepository repository;
	private final EmailTokenRepository emailTokenRepository;
	
	/**
	 * 유효한 토큰 가져오기
	 */
	public EmailToken findByUserNoAndExpirationDateAfterAndExpired(String token) throws Exception {
		Optional<EmailToken> emailToken = emailTokenRepository
				.findByIdAndExpirationDateAfterAndExpired(token, LocalDateTime.now(), false);
		
		return emailToken.orElseThrow(() -> new BadRequestException("토큰을 찾을 수 없습니다."));
	}
	
	public EmailToken findByToken(String token) throws Exception {
		Optional<EmailToken> emailToken = emailTokenRepository
				.findById(token);
		
		return emailToken.orElseThrow(() -> new BadRequestException("토큰을 찾을 수 없습니다."));
	}
	
	/**
	 * 토큰이 유효하면 사용 완료 처리
	 */
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