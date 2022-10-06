package ru.axel.logger;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.*;

public final class MiniLogger {
    private static final ConsoleHandler loggerHandler = new ConsoleHandler();
    private static Level logLevel = Level.ALL;
    private static String format = "%1$tF %1$tT [%2$-7s] [%3$s] %4$s %5$s: %6$s %n";

    public static Level getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(Level level) {
        logLevel = level;
    }
    public static void setFormat(String patternFormat) {
        format = patternFormat;
    }

    public static @NotNull Logger getLogger(@NotNull Class<?> clazz) {
        loggerHandler.setFormatter(getFormatter());
        loggerHandler.setLevel(logLevel);

        Logger logger = Logger.getLogger(clazz.getName());

        logger.setUseParentHandlers(false);
        logger.addHandler(loggerHandler);
        logger.setLevel(logLevel);

        return logger;
    }

    @Contract(" -> new")
    @NotNull
    private static Formatter getFormatter() {
        return new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(
                    format,
                    new Date(lr.getMillis()),
                    lr.getLevel().getLocalizedName(),
                    "thread: " + Thread.currentThread().getName(),
                    lr.getLoggerName(),
                    lr.getSourceMethodName(),
                    lr.getMessage()
                );
            }
        };
    }
}
