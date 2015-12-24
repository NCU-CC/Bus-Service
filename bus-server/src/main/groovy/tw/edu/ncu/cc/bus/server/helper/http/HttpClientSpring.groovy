package tw.edu.ncu.cc.bus.server.helper.http

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException

public class HttpClientSpring {

    private HttpClient httpClient

    private HttpClientSpring( HttpClient httpClient ) {
        this.httpClient = httpClient
    }

    public static HttpClientSpring connect( String url ) {
        return new HttpClientSpring(
                HttpClient
                        .connect( url )
                        .header( "Content-Type", "application/json;charset=UTF-8 " )
        )
    }

    public HttpClientSpring variables( String...variables ) {
        httpClient.variables( variables )
        this
    }

    public HttpClientSpring parameter( String key, String value ) {
        httpClient.parameter( key, value )
        this
    }

    public HttpClientSpring header( String key, String value ) {
        httpClient.header( key, value )
        this
    }

    public HttpClientSpring content( String content ) {
        httpClient.content( content )
        this
    }

    public String get() {
        try {
            return httpClient.get()
        } catch ( HttpException e ) {
            throw new HttpServerErrorException( HttpStatus.valueOf( e.getCode() ), "Remote " + e.getMessage() )
        }
    }

    public < T > T get( Class< T > responseType ) {
        try {
            return httpClient.get( responseType )
        } catch ( HttpException e ) {
            throw new HttpServerErrorException( HttpStatus.valueOf( e.getCode() ), "Remote " + e.getMessage() )
        }
    }
}
