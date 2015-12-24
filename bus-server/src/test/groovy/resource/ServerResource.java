package resource;

import org.junit.rules.ExternalResource;
import org.mockserver.integration.ClientAndServer;

public class ServerResource extends ExternalResource {

    private int httpPort;
    private int httpsPort;
    private ClientAndServer mockServer;

    public ServerResource( int httpPort, int httpsPort ) {
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
    }

    @Override
    protected void before() throws Throwable {
        mockServer = ClientAndServer.startClientAndServer( httpPort, httpsPort );
    }

    @Override
    protected void after() {
        mockServer.stop();
    }

    public ClientAndServer mockServer() {
        return mockServer;
    }

}
