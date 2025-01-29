package com.mkkabi.dev.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class AppLogger extends Logger {
//private String logFilesLocation = System.getenv("APPDATA")+"\\SchoolCalendar\\logs\\";
private String logFilesLocation = "logs";
    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    public AppLogger(String name) {
        super(name, null);
        Path path = Paths.get(logFilesLocation);
        try {
            if(!Files.exists(path))
                Files.createDirectory(path);
            this.addHandler(new FileHandler(logFilesLocation+"/"+name+".log"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
