package com.auth.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class AuthLogger {

    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + File.separator + "auth.log";
    private static Logger logger;

    static {
        try {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            logger = Logger.getLogger("AuthLogger");
            logger.setLevel(Level.ALL);

            Logger rootLogger = Logger.getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AuthLogger() {}

    public static void logRegistration(String username) {
        logger.info("[REGISTRATION] New user registered: " + username);
    }

    public static void logRegistrationFailed(String username, String reason) {
        logger.warning("[REGISTRATION FAILED] Username: " + username + " | Reason: " + reason);
    }

    public static void logLoginSuccess(String username) {
        logger.info("[LOGIN SUCCESS] User: " + username);
    }

    public static void logLoginFailed(String username, int failedAttempts) {
        logger.warning("[LOGIN FAILED] User: " + username + " | Failed attempts: " + failedAttempts);
    }

    public static void logAccountLocked(String username) {
        logger.severe("[ACCOUNT LOCKED] User: " + username + " — Account locked after 3 failed login attempts.");
    }

    public static void logSecurityWarning(String message) {
        logger.warning("[SECURITY] " + message);
    }

    public static void logInfo(String message) {
        logger.info("[INFO] " + message);
    }
}
