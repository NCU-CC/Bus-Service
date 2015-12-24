package tw.edu.ncu.cc.bus.server.web.api.v1

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import resource.ServerResource
import specification.IntegrationSpecification
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static tw.edu.ncu.cc.oauth.resource.test.ApiAuthMockMvcRequestPostProcessors.apiToken


class BusControllerTest extends IntegrationSpecification {

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/providers" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/xml" ) )
                        .withBody(
                        '''
                        <?xml version="1.0" encoding="utf-8" ?>
                        <BusDynInfo>
                          <EssentialInfo>
                            <Location>
                              <name>TYBUS</name>
                              <CenterName>Maxwin</CenterName>
                            </Location>
                            <UpdateTime>2015-12-16 15:56:15</UpdateTime>
                            <CoordinateSystem></CoordinateSystem>
                          </EssentialInfo>
                          <BusInfo>
                            <Provider ID="1" nameZh="test-bus" type="0"/>
                          </BusInfo>
                        </BusDynInfo>
                        '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/routes/132/buses" )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/xml" ) )
                        .withBody(
                        '''
                        <?xml version="1.0" encoding="UTF-8"?>
                        <BusDynInfo>
                            <EssentialInfo>
                                <Location>
                                    <name>Maxwin</name>
                                    <CenterName/>
                                    <UpdateTime>2015-12-16 16:52:13</UpdateTime>
                                    <CoordinateSystem/>
                                </Location>
                            </EssentialInfo>
                            <BusInfo>
                                <BusData BusID="920-FP_o" ProviderID="1" DutyStatus="0" BusStatus="98" RouteID="3220" GoBack="2" Longitude="121.278129" Latitude="24.983570" Speed="3" Azimuth="24" DataTime="2015-12-16 16:51:58" ledstate="0" sections="1"/>
                            </BusInfo>
                        </BusDynInfo>
                        '''
                )
        )
    }

    def "it can provide all bus providers"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/providers" )
                                    .with( apiToken() )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.BusDynInfo.EssentialInfo.Location.name == "TYBUS"
    }

    def "it can provide bus info by route id"() {
        when:
            def response = JSON(
                    server().perform(
                            get( "/v1/routes/132/buses" )
                                    .with( apiToken() )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.BusDynInfo.BusInfo.BusData.RouteID == 3220
    }

}
