package app.routes;

import app.config.HibernateConfig;
import app.utils.Populator;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static io.javalin.apibuilder.ApiBuilder.get;

public class PopulatorRoutes {


    protected EndpointGroup getRoutes() {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        return () -> {
            get("/", ctx -> {
                Populator.populateUsers(emf);
                ctx.result("Populated");
            });
        };
    }
}