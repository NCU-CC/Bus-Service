package tw.edu.ncu.cc.bus.server.service

import org.json.XML
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.bus.server.helper.http.HttpClientSpring

@Service
public class BusServiceImpl implements BusService {

    @Value( '${custom.connection.remote.prefix}' )
    def String remotePrefix

    @Override
    @Cacheable( value="long-term", key="'providers'" )
    def findAllProviders() {
        return parse(
                HttpClientSpring
                .connect( remotePrefix + "/StaticData/GetProvider.xml" )
                .get()
        )
    }

    @Override
    @Cacheable( value="long-term", key="'routes'" )
    def findAllRoutes() {
        return parse(
                HttpClientSpring
                .connect( remotePrefix + "/StaticData/GetRoute.xml" )
                .get()
        )
    }

    @Override
    @Cacheable( value="short-term", key="'bus-' + #routeId" )
    def findBusesByRouteId( routeId ) {
        return parse(
                HttpClientSpring
                .connect( remotePrefix + "/GetBusData.xml" )
                .parameter( "routeIds", routeId as String )
                .get()
        )
    }

    @Override
    @Cacheable( value="short-term", key="'estimate-' + #routeId" )
    def findEstimateTimesByRouteId( routeId ) {
        return parse(
                HttpClientSpring
                .connect( remotePrefix + "/GetEstimateTime.xml" )
                .parameter( "routeIds", routeId as String )
                .get()
        )
    }

    @Override
    @Cacheable( value="long-term", key="'stop-' + #routeId" )
    def findStopsByRouteId( routeId ) {
        return parse(
                HttpClientSpring
                .connect( remotePrefix + "/StaticData/GetStop.xml" )
                .parameter( "routeIds", routeId as String )
                .get()
        )
    }

    private static def parse( String text ) {
        XML.toJSONObject(text).toString()
    }

}
