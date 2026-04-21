package com.auth.app;

import com.auth.db.DatabaseConnection;
import com.auth.util.AuthLogger;
import com.auth.web.ApiHandler;
import com.auth.web.StaticFileHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainApp {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        AuthLogger.logInfo("Starting system...");

        // init db
        DatabaseConnection.getInstance().getConnection();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            server.createContext("/", new StaticFileHandler("docs"));
            server.createContext("/api/login", new ApiHandler());
            server.createContext("/api/register", new ApiHandler());
            
            server.setExecutor(null);
            server.start();

            System.out.println("Server running on port " + PORT);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.stop(0);
                DatabaseConnection.closeConnection();
            }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
