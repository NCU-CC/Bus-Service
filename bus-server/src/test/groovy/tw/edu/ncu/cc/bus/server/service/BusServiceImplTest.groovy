package tw.edu.ncu.cc.bus.server.service

import groovy.json.JsonSlurper
import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.Parameter
import org.springframework.beans.factory.annotation.Autowired
import resource.ServerResource
import specification.SpringSpecification
import spock.lang.Shared

class BusServiceImplTest extends SpringSpecification {

    @Autowired
    BusService busService

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898, 8899 )

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/StaticData/GetProvider.xml" )
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
                        .withPath( "/GetBusData.xml" )
                        .withQueryStringParameter( new Parameter( "routeIds", "132" ) )
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

    def "it can read bus providers from remote server"() {
        when:
            def response = new JsonSlurper().parseText( busService.findAllProviders() as String )
        then:
            response.BusDynInfo.EssentialInfo.Location.name == "TYBUS"
    }

    def "it can read bus data from remote server"() {
        when:
            def response = new JsonSlurper().parseText( busService.findBusesByRouteId( "132" ) as String )
        then:
            response.BusDynInfo.BusInfo.BusData.RouteID == 3220
    }


}
