package himedia.project.highfourm.service.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import himedia.project.highfourm.dto.user.UserAddDTO;
import himedia.project.highfourm.entity.EmailToken;
import himedia.project.highfourm.entity.User;
import himedia.project.highfourm.repository.EmailTokenRepository;
import himedia.project.highfourm.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

/**
 * @author 한혜림
 */
@Service
public class GmailService {

    private static final String APPLICATION_NAME = "highfourm";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final String USER_ID = "me";

    private static final String FROM_EMAIL = "secondoneHH3@gmail.com";

    private final Gmail service;
    private final UserService userService;
    private final EmailTokenRepository emailTokenRepository;

    public GmailService(UserService userService, EmailTokenRepository emailTokenRepository) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
		this.userService = userService;
		this.emailTokenRepository = emailTokenRepository;
    }

    /**
     * Gmail Open API 인증 정보
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singletonList(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * 이메일 작성, 전송 및 사용자 저장/토큰 생성
     */
    public void sendEmail(UserAddDTO newUser, Authentication authentication) throws MessagingException, IOException {
    	User savedUser = userService.save(newUser, authentication);
    	EmailToken emailToken = EmailToken.createEmailToken(savedUser);
    	emailTokenRepository.save(emailToken);
    	
    	String subject = "하이포엠 회원가입 사용자 인증 이메일입니다";
        String body = "<div style='text-align:center; border: 1px solid; padding: 30px'>"
				+ "<h1> 하이포엠 사용자 회원가입 인증 이메일 </h1>"
				+ "<br>"
				+ "<p style='font-size:16px;'>아래 링크를 클릭하면 하이포엠 회원가입 화면으로 이동됩니다.</p>"
				+ "<a href='http://43.201.16.255:9999/confirm-email?token=" + emailToken.getId()
				+"&empNo=" + emailToken.getUser().getEmpNo().toString() + "' style='font-size:large;'>인증 링크</a>"
				+"</div>";
    	
    	MimeMessage email = createEmail(savedUser.getEmail(), subject, body);
        Message message = createMessageWithEmail(email);
        service.users().messages().send(USER_ID, message).execute();
    }

    /**
     * MimeMessage 생성, 이메일 보낼 세션 생성
     */
    private MimeMessage createEmail(String toEmail, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(FROM_EMAIL));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(toEmail));
        email.setSubject(subject);
        email.setContent(body, "text/html; charset=utf-8");

        return email;
    }

    /**
     * MimeMessage을 받아 Message 객체 생성
     */
    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}
