package tw.edu.ncu.cc.bus.server.web.api.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.bus.server.service.BusService
import tw.edu.ncu.cc.bus.server.validator.RouteIdValidator

@RestController
@RequestMapping( value = "v1", method = RequestMethod.GET, produces = "application/json; charset=UTF-8" )
class BusController {

    @Autowired
    def BusService busService

    @InitBinder
    private static void initBinder( WebDataBinder binder ) {
        binder.setValidator( new RouteIdValidator() );
    }

    @RequestMapping( value = "providers" )
    public String findAllProviders() {
        busService.findAllProviders()
    }

    @RequestMapping( value = "routes" )
    public String findAllRoutes() {
        busService.findAllRoutes()
    }

    @RequestMapping( value = "routes/{routeId}/buses" )
    public String findBusesByRouteId( @Validated( RouteIdValidator ) @PathVariable( "routeId" ) String routeId ) {
        busService.findBusesByRouteId( routeId )
    }

    @RequestMapping( value = "routes/{routeId}/estimate_times" )
    public String findEstimateTimesByRouteId( @Validated( RouteIdValidator ) @PathVariable( "routeId" ) String routeId ) {
        busService.findEstimateTimesByRouteId( routeId )
    }

    @RequestMapping( value = "routes/{routeId}/stops" )
    public String findStopsByRouteId( @Validated( RouteIdValidator ) @PathVariable( "routeId" ) String routeId ) {
        busService.findStopsByRouteId( routeId )
    }

}
