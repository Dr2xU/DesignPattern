package com.fges.app;

import com.fges.cli.CommandLineArgs;
import com.fges.cli.CommandLineProcessor;
import com.fges.executor.CommandExecutor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application entry point. Parses CLI arguments and delegates execution to core logic.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        int exitCode = exec(args);
        System.exit(exitCode);
    }

    /**
     * Executes the CLI logic and returns a result code (0 = OK, 1 = Error).
     * This is separated from main() to support testability.
     *
     * @param args the raw CLI arguments
     * @return 0 if success, 1 if failure
     */
    public static int exec(String[] args) {
        try {
            CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(args);
            return new CommandExecutor(parsedArgs).execute();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "I/O failure during execution", e);
            return 1;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Command failed: " + e.getMessage(), e);
            return 1;
        }
    }
}