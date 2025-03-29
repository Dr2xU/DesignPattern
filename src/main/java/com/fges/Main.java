// Main.java

package com.fges;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            System.exit(exec(args));
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public static int exec(String[] args) throws IOException {
        try {
            CommandLineProcessor processor = new CommandLineProcessor();
            CommandLineArgs commandArgs = processor.parseArgs(args);
            
            GroceryListManager manager = new GroceryListManager(commandArgs.getFileName());
            CommandExecutor executor = new CommandExecutor(manager);
            
            return executor.execute(commandArgs.getCommand(), commandArgs.getArguments());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
    }
}