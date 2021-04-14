package fr.univtln.mapare;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static final String BASE_URI = "http://localhost:5431/maparevote";

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

    public static void main(String[] args) throws InterruptedException {
        final HttpServer server = startServer();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

        Thread.currentThread().join();
        server.shutdown();
    }
}