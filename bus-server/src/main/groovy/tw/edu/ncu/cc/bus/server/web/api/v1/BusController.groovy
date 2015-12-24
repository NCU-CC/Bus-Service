package tw.edu.ncu.cc.bus.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import tw.edu.ncu.cc.bus.server.service.BusService

@RestController
@RequestMapping( value = "v1", method = RequestMethod.GET, produces = "application/json; charset=UTF-8" )
class BusController {

    @Autowired
    def BusService busService

    @RequestMapping( value = "providers" )
    public String findAllProviders() {
        busService.findAllProviders()
    }

    @RequestMapping( value = "routes" )
    public String findAllRoutes() {
        busService.findAllRoutes()
    }

    @RequestMapping( value = "routes/{routeId}/buses" )
    public String findBusesByRouteId( @PathVariable( "routeId" ) String routeId ) {
        busService.findBusesByRouteId( routeIdFilter( routeId ) )
    }

    @RequestMapping( value = "routes/{routeId}/estimate_times" )
    public String findEstimateTimesByRouteId( @PathVariable( "routeId" ) String routeId ) {
        busService.findEstimateTimesByRouteId( routeIdFilter( routeId ) )
    }

    @RequestMapping( value = "routes/{routeId}/stops" )
    public String findStopsByRouteId( @PathVariable( "routeId" ) String routeId ) {
        busService.findStopsByRouteId( routeIdFilter( routeId ) )
    }

    private static String routeIdFilter( String routeId ){
        switch ( routeId ){
            case "3220":
            case "3222":
            case "3221":
            case "133":
                return routeId;
            default:
                throw new HttpClientErrorException( HttpStatus.BAD_REQUEST, "routeId can only be 133, 3220, 3221, 3222" )
        }
    }

}
