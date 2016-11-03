package pe.mplescano.webapp.oauth2.controllers;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mplescano
 * @see http://stackoverflow.com/questions/31338742/spring-security-how-to-get-oauth2-userinfo-during-sso-logged-in
 */
@RestController
public class FacebookController {
	

	@RequestMapping("/user")
	public Authentication user(Principal principal) {
		Authentication userAuthentication = null;
		if (principal instanceof OAuth2Authentication) {
			userAuthentication = ((OAuth2Authentication) principal).getUserAuthentication();
		}
		return userAuthentication;
	}
	
}