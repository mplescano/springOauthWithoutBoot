package pe.mplescano.webapp.oauth2.app;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author mplescano
 * @see https://github.com/Baeldung/reddit-app
 * @see http://www.baeldung.com/spring-security-oauth2-authentication-with-reddit
 */
public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}
 
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}