package utils;

public class Logger {

    private String log;

    public Logger(Class c) {
        this.log = c.getSimpleName().length() > 20 ? c.getSimpleName().substring(0, 20) : c.getSimpleName();
    }

    public void info(String msg) {
        System.out.println("[INFO] " + log + " >>> " + msg);
    }

    public void warn(String msg) {
        System.out.println("[WARN] " + log + " >>> " + msg);
    }

    public void error(String msg) {
        System.err.println("[ERROR] " + log + " >>> " + msg);
    }
}