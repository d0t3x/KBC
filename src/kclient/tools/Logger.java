package kclient.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author SeBi
 */
public class Logger {
    private static final Logger instance;
    static {
        instance = new Logger();
    }
    
    public static Logger get() {
        synchronized (instance) {
            return instance;
        }
    }
    
    private Level level = Level.ALL;
    public void setLevel(Level level) {
        this.level = level;
    }
    
    private String getDate() {
        Date now = Calendar.getInstance().getTime();
        return new SimpleDateFormat("[HH:mm:ss]").format(now);
    }
    
    public void debug(String message) {
        if (this.level == Level.DEBUG || this.level == Level.ALL) {
            String log = String.format("%s[DEBUG] %s", getDate(), message);
            System.out.println(log);
        }
    }
    
    public void error(Object message) {
        if (this.level == Level.ERROR || this.level == Level.ALL) {
            if (message instanceof Exception) {
                System.out.println("[ERROR] ");
                ((Exception)message).printStackTrace();
                Exception e = (Exception)message;
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    e.printStackTrace(ps);
                    baos.toString("UTF-8");
                } catch (IOException ex) {
                }
            } else {
                System.err.println("[ERROR] " + message);
            }
        }
    }
    
    public void info(String message) {
        if (this.level == Level.INFO || this.level == Level.ALL) {
            String log = String.format("%s[INFO] %s", getDate(), message);
            System.out.println(log);
        }
    }
    
    public enum Level {
        INFO,
        DEBUG,
        ERROR,
        ALL,
        NONE
    }
}