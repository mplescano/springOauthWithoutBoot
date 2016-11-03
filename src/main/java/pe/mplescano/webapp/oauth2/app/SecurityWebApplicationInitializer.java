package pe.mplescano.webapp.oauth2.app;

import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @author mplescano
 * @see http://www.programcreek.com/java-api-examples/index.php?api=org.springframework.web.context.request.RequestContextListener
 * @see http://stackoverflow.com/questions/35875098/protecting-rest-api-with-oauth2-error-creating-bean-with-name-scopedtarget-oau
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	@Override
	protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
		servletContext.addListener(new RequestContextListener());
	}

	
	
	
}
