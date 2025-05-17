package com.fges.cli;

import org.apache.commons.cli.*;

import java.util.List;

/**
 * Responsible for parsing command-line arguments using Apache Commons CLI.
 * This class extracts and validates the required input arguments for the grocery list application.
 * Supported arguments:
 *     -s or --source (required except for "info" and "web")
 *     -f or --format (optional): file format (json or csv)
 *     -c or --category (optional): item category (defaults to "default")
 */
public class CommandLineProcessor {

    /**
     * Parses command-line arguments including required options: source (-s),
     * optional format (-f), and category (-c). It also handles 'info' and 'web'
     * as special commands that do not require a source file.
     *
     * @param args the raw command-line arguments
     * @return a {@link CommandLineArgs} object containing the parsed values
     * @throws IllegalArgumentException if required options are missing or malformed
     */
    public CommandLineArgs parseArgs(String[] args) {
        Options options = new Options();
        options.addOption("s", "source", true, "Source file (required for most commands)");
        options.addOption("f", "format", true, "Data format: json/csv");
        options.addOption("c", "category", true, "Optional category");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            List<String> remainingArgs = cmd.getArgList();

            if (remainingArgs.isEmpty()) {
                throw new IllegalArgumentException("Missing command (add/list/remove/info/web)");
            }

            String source = cmd.getOptionValue("s");
            String format = cmd.getOptionValue("f", "json");
            String category = cmd.getOptionValue("c", "default");

            return new CommandLineArgs(source, format, category, remainingArgs);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse CLI arguments", e);
        }
    }
}