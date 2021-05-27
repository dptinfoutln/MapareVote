package fr.univtln.mapare;

import fr.univtln.mapare.controllers.Controllers;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Main Class.
 */
public class Main {
    /**
     * The constant BASE_URI.
     */
    public static final String BASE_URI = "http://0.0.0.0:5431/";

    /**
     * Start server http server.
     *
     * @return the http server
     */
    public static HttpServer startServer() {
        Logger logger = Logger.getAnonymousLogger();
        logger.setLevel(Level.FINEST);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        logger.addHandler(handler);

        final ResourceConfig rc = new ResourceConfig()
                .packages(true, "fr.univtln.mapare")
                .register(new LoggingFeature(logger, Level.FINEST, null, null));

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        Controllers.init();
        final HttpServer server = startServer();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

        Thread.currentThread().join();
        server.shutdown();
        Controllers.close();
    }
}
