package tw.edu.ncu.cc.bus.server.helper.http

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.util.UriTemplate

public class HttpClient {

    private HttpInfo httpInfo;

    private HttpClient( String url ) {
        httpInfo = new HttpInfo();
        httpInfo.setUrl( url );
    }

    public static HttpClient connect( String url ) {
        return new HttpClient( url );
    }

    public HttpClient variables( String...variables ) {
        httpInfo.setVariables( variables );
        return this;
    }

    public HttpClient parameter( String key, String value ) {
        httpInfo.addParameter( key, value );
        return this;
    }

    public HttpClient header( String key, String value ) {
        httpInfo.addHeader( key, value );
        return this;
    }

    public HttpClient content( String content ) {
        httpInfo.setContent( content );
        return this;
    }

    public String get() {
        try {
            HttpURLConnection response = sendRequest( "GET" );
            if ( response.getResponseCode() == 200 ) {
                return getStringFromResponse( response );
            } else {
                throw new HttpException( response.getResponseCode(), response.getResponseMessage() );
            }
        } catch ( IOException e ) {
            throw new HttpException( 500, e.getMessage() );
        }
    }

    public < T > T get( Class< T > responseType ) {
        try {
            return convert( get(), responseType );
        } catch ( IOException e ) {
            throw new HttpException( 500, e.getMessage() );
        }
    }

    private HttpURLConnection sendRequest( String method ) throws IOException {

        HttpURLConnection connection = ( HttpURLConnection ) buildUrl().openConnection();

        connection.setRequestMethod( method );
        connection.setDoOutput( true );
        connection.setDoInput( true );

        for( Map.Entry< String, String > header : httpInfo.getHeaders().entrySet() ) {
            connection.setRequestProperty( header.getKey(), header.getValue() );
        }

        if ( httpInfo.getContent() != null && httpInfo.getContent().length() > 0 ) {
            writeContent( connection );
        }
        connection.connect();

        return connection;
    }

    private void writeContent( HttpURLConnection connection ) throws IOException {
        connection.outputStream.withStream {
            DataOutputStream dataStream = new DataOutputStream( it );
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( dataStream, "UTF-8" ) );
            writer.write( httpInfo.getContent() );
            writer.flush();
        }
    }

    private static String getStringFromResponse( HttpURLConnection response ) throws IOException {

        response.inputStream.withStream { InputStream inputStream ->
            new BufferedReader( new InputStreamReader( inputStream, "UTF-8" ) ).withReader { Reader reader ->
                StringBuilder body = new StringBuilder( 2048 );
                String inputLine;
                while ( ( inputLine = reader.readLine() ) != null )
                    body.append( inputLine ).append( "\n" );
                return body.toString();
            }
        }
    }

    private static < T > T convert( String message, Class<T> type ) throws IOException {
        return new ObjectMapper().readValue( message, type );
    }

    private URL buildUrl() throws MalformedURLException {
        String url = httpInfo.getUrl();
        if( httpInfo.getParameters().size() != 0 ) {
            StringBuilder builder = new StringBuilder( httpInfo.getUrl() + "?" );
            for( Map.Entry< String, String > parameter : httpInfo.getParameters().entrySet() ) {
                if( parameter.getValue() != null ) {
                    builder.append( parameter.getKey() ).append( "=" ).append( parameter.getValue() ).append( "&" );
                }
            }
            url = builder.deleteCharAt( builder.length()-1 ).toString();
        }

        if( httpInfo.getVariables() == null ) {
            return new URL( url );
        } else {
            return new UriTemplate( url ).expand( ( Object[] ) httpInfo.getVariables() ).toURL();
        }
    }

}

