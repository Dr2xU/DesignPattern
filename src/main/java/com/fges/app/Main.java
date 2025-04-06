package com.fges.app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fges.cli.CommandLineArgs;
import com.fges.cli.CommandLineProcessor;
import com.fges.executor.CommandExecutor;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Main method invoked when the program is run from the command line.
     * Delegates argument processing and command execution.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        try {
            System.exit(exec(args));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unexpected IO Error during execution", e);
            System.exit(1);
        }
    }

    /**
     * Executes the command based on provided command-line arguments.
     * Handles command parsing and execution logic.
     *
     * @param args the raw command-line arguments
     * @return exit code: 0 for success, 1 for failure
     * @throws IOException if an I/O error occurs during command execution
     */
    public static int exec(String[] args) throws IOException {
        try {
            CommandLineProcessor processor = new CommandLineProcessor();
            CommandLineArgs commandArgs = processor.parseArgs(args);

            CommandExecutor executor = new CommandExecutor(
                commandArgs.getFileName(),
                commandArgs.getFormat(),
                commandArgs.getCategory()
            );

            return executor.execute(commandArgs.getCommand(), commandArgs.getArguments());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Command execution failed: " + e.getMessage(), e);
            return 1;
        }
    }
}
