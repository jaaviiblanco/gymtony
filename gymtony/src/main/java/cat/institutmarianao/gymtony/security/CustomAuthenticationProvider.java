package cat.institutmarianao.gymtony.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cat.institutmarianao.gymtony.model.Usuario;

@Component
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:application.properties")
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Value("${webService.host}")
	private String webServiceHost;

	@Value("${webService.port}")
	private String webServicePort;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${exception.badCredentials}")
	private String badCredentialsMessage;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String uri = webServiceHost + ":" + webServicePort + "/users/authenticate";

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> postBody = new HashMap<>();
		postBody.put("username", username);
		postBody.put("passwd", password);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(postBody, headers);

		try {
			Usuario userDetails = restTemplate.postForObject(uri, request, Usuario.class);
			if (userDetails != null) {
				return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
			}
		} catch (Exception e) {
			// Do nothing
		}
		throw new BadCredentialsException(badCredentialsMessage);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
