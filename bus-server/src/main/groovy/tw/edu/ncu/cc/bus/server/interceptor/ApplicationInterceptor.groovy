package tw.edu.ncu.cc.bus.server.interceptor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
public class ApplicationInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

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

    private static String getRequestPath( HttpServletRequest request ) {
        return request.getRequestURI().substring( request.getContextPath().length() )
    }

}
