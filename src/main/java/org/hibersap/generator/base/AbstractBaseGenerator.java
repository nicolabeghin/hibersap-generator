package org.hibersap.generator.base;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Abstract base class for basic plumbing
 */
public abstract class AbstractBaseGenerator {

    public static Logger LOG;
    private static final String LOG_FORMAT = "[%1$tF %1$tT] [%4$-7s] %5$s %n";
    private static final String APP_NAME = "hibersap-generator.log";

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
        LOG = Logger.getLogger(APP_NAME);
        LOG.setLevel(Level.ALL);
        try {
            FileHandler handler = new FileHandler(String.format("%s.log", APP_NAME));
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.ALL);
            LOG.addHandler(handler);
        } catch (Exception ex) {
            System.err.println("Unable to setup logger: " + ex.getMessage());
        }
    }

    public static void setupFunctionModuleFileLogger(String FUNCTION_MODULE) {
        if (LOG==null) return;
        try {
            FileHandler handler = new FileHandler(String.format("%s.log", FUNCTION_MODULE));
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.ALL);
            LOG.addHandler(handler);
        } catch (Exception ex) {
            System.err.println("Unable to setup logger: " + ex.getMessage());
        }
    }
}
