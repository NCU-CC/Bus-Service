package tw.edu.ncu.cc.bus.server.helper.http

public class HttpInfo {

    def String url
    def String content
    def String[] variables
    def Map< String, String > parameters
    def Map< String, String > headers

    public HttpInfo() {
        headers = new HashMap<>()
        parameters = new HashMap<>()
    }

    public void addParameter( String key, String value ) {
        parameters.put( key, value )
    }

    public void addHeader( String key, String value ) {
        headers.put( key, value )
    }

}
