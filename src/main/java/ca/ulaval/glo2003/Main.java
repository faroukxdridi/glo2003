package ca.ulaval.glo2003;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static HttpServer startServer() {
        // Enregistrer toutes les ressources ici
        final ResourceConfig rc = new ResourceConfig()
                .register(new HealthResource())   // Ressource de vérification de la santé
                .register(new GroupResource());  // Nouvelle ressource pour gérer les groupes

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        HttpServer server = startServer();
        System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
        System.out.println("Press Ctrl+C to stop the server...");
    }
}
