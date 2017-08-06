package tw.edu.ncu.cc.bus.server.interceptor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import java.util.HashMap
import java.util.Map
import java.text.SimpleDateFormat
import java.util.Date
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Value

@Component
public class ApplicationInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

    @Value( '${log-api.uri}' )
    private String logApiUri

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        logger.info(
                String.format( "[%s] [%s] [%s]",
                        request.method,
                        request.remoteAddr,
                        getRequestPath( request )
                )
        )
        return true
    }

    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView ) throws Exception {
        def message = String.format(
                "`%s: %s %s %s %s`",
                "bus_service",
                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() ),
                request.remoteAddr,
                request.method,
                getRequestPath( request )
        )
        if( !callLogAPI( message, logApiUri ) ) {
            logger.error( "Unable to call log api properly" )
        }
    }

    private static boolean callLogAPI( String message, String logApiUri ) {

        // Headers
        def headers = new HttpHeaders()
        headers.setContentType( MediaType.APPLICATION_FORM_URLENCODED )

        // POST parameters
        def map = new LinkedMultiValueMap<String, String>()
        map.add( "application", "bus_service" )
        map.add( "message", message )

        // Request
        def request = new HttpEntity<MultiValueMap<String, String>>( map, headers )

        try {
            // Make a request
            def response = new RestTemplate().postForEntity( logApiUri, request, String.class )
            return true

        } catch ( Exception e ) {
            return false
        }
    }

    private static String getRequestPath( HttpServletRequest request ) {
        return request.getRequestURI().substring( request.getContextPath().length() )
    }

}
