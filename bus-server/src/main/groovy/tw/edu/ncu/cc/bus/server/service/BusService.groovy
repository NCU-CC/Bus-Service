package tw.edu.ncu.cc.bus.server.service

public interface BusService {
    def findAllProviders()
    def findAllRoutes()
    def findBusesByRouteId( routeId )
    def findEstimateTimesByRouteId( routeId )
    def findStopsByRouteId( routeId )
}
