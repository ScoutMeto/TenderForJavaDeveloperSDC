package org.SDC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyLogger {
    // Vytvoř logger pro třídu
    private static final Logger logger = LogManager.getLogger(MyLogger.class);

    public static void logInfo(String message) {
        logger.info(message);
    }

}