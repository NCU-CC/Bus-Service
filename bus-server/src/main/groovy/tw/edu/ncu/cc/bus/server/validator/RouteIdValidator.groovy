package tw.edu.ncu.cc.bus.server.validator

import org.springframework.http.HttpStatus
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.client.HttpClientErrorException


class RouteIdValidator implements Validator {

    @Override
    boolean supports( Class<?> clazz ) {
        return String.class.equals( clazz )
    }

    @Override
    void validate( Object target, Errors errors ) {
        String routeId = ( String ) target
        switch ( routeId ){
            case "3220":
            case "3222":
            case "3221":
            case "133":
                break
            default:
                throw new HttpClientErrorException( HttpStatus.BAD_REQUEST, "routeId can only be 133, 3220, 3221, 3222" )
        }
    }

}
