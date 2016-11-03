package pe.mplescano.webapp.oauth2.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import pe.mplescano.webapp.oauth2.utils.UserInfoTokenServices;

/**
 * @author mplescano
 * @see http://stackoverflow.com/questions/32201870/handle-userredirectrequiredexception-a-redirect-is-required-to-get-the-users-ap
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;
 
    @Value("${security.oauth2.client.userAuthorizationUri}")
    private String userAuthorizationUri;
 
    @Value("${security.oauth2.client.clientId}")
    private String clientID;
 
    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;
    
    @Value("${security.oauth2.client.tokenName}")
    private String tokenName;
    
    @Value("${security.oauth2.client.authenticationScheme}")
    private String authenticationScheme;
    
    @Value("${security.oauth2.client.clientAuthenticationScheme}")
    private String clientAuthenticationScheme;

	
    @Value("${security.oauth2.resource.userInfoUri}")
    private String userInfoUri;
    
	  @Autowired
	  OAuth2ClientContext oauth2ClientContext;
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication();
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.antMatcher("/**")
            .csrf().disable()
            .authorizeRequests()
            	.antMatchers("/home.html").hasRole("USER")
            	.antMatchers("/", "/login**").permitAll()
			.anyRequest().authenticated()
            .and()
            	.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
        		.addFilterAfter(oauth2Filter(), SecurityContextPersistenceFilter.class);
    }
 
    @Bean
    public OAuth2ClientContextFilter oauth2Filter() {
    	OAuth2ClientContextFilter oauth2Filter = new OAuth2ClientContextFilter();
    	return oauth2Filter;
    }
    
    @Bean
    public OAuth2ClientAuthenticationProcessingFilter ssoFilter() {
		  OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
		  facebookFilter.setRestTemplate(facebookRestTemplate());
		  facebookFilter.setTokenServices(inMemoryTokenServices());
		  SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
		  handler.setDefaultTargetUrl("/home.html");
		  facebookFilter.setAuthenticationSuccessHandler(handler);
		  
		  return facebookFilter;
	}

    private ResourceServerTokenServices inMemoryTokenServices() {
        /*InMemoryTokenStore tok = new InMemoryTokenStore();
        DefaultTokenServices tokenService = new DefaultTokenServices();
        tokenService.setTokenStore(tok);
        return tokenService;*/
        return new UserInfoTokenServices(userInfoUri, facebook().getClientId());
    }
    
    @Bean
    /*@Scope("session")*/
    public AuthorizationCodeResourceDetails facebook() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        /*details.setId("reddit");*/
        /*details.setId("facebook");*/
        details.setClientId(clientID);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setTokenName(tokenName);
        /*details.setScope(Arrays.asList("identity"));*/
        details.setAuthenticationScheme(AuthenticationScheme.valueOf(authenticationScheme));
        details.setClientAuthenticationScheme(AuthenticationScheme.valueOf(clientAuthenticationScheme));
        //details.setPreEstablishedRedirectUri("http://localhost:8080/home.html");
        /*details.setUseCurrentUri(false);*/
        return details;
    }
 
    @Bean
    /*@Scope(value = "session", proxyMode = ScopedProxyMode.DEFAULT)*/
    public OAuth2RestTemplate facebookRestTemplate() {
    	OAuth2RestTemplate template = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
    	return template;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
      YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
      yaml.setResources(new ClassPathResource("application.yml"));
      yaml.afterPropertiesSet();
      propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
      propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
      return propertySourcesPlaceholderConfigurer;
    }
}