package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    /*
    private final HotelRoute hotelRoute = new HotelRoute();
    private final RoomRoute roomRoute = new RoomRoute();
*/

    private final SkiRoutes skiRoutes = new SkiRoutes();
    private final PopulatorRoutes populator = new PopulatorRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/populate", populator.getRoutes());
            path("/skilessons", skiRoutes.getRoutes());

            /*
                path("/", hotelRoute.getRoutes());
                path("/rooms", roomRoute.getRoutes());
             */
        };
    }
}
