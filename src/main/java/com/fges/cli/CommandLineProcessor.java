package com.fges.cli;

import org.apache.commons.cli.*;

import java.util.List;

/**
 * Responsible for parsing command-line arguments using Apache Commons CLI.
 * This class extracts and validates the required input arguments for the grocery list application.
 * Supported arguments:
 *     -s or --source (required): the source file name
 *     -f or --format (optional): file format (json or csv)
 *     -c or --category (optional): item category (defaults to "default")
 */
public class CommandLineProcessor {

    /**
     * Parses command-line arguments including required options: source (-s),
     * optional format (-f), and category (-c).
     *
     * @param args the raw command-line arguments
     * @return a {@link CommandLineArgs} object containing the parsed values
     * @throws ParseException           if required options are missing or malformed
     * @throws IllegalArgumentException if no positional command is provided
     */
    public CommandLineArgs parseArgs(String[] args) throws ParseException {
        Options cliOptions = new Options();

        cliOptions.addRequiredOption("s", "source", true, "File containing the grocery list");
        cliOptions.addOption("f", "format", true, "Format of the grocery list file (json or csv)");
        cliOptions.addOption("c", "category", true, "Category of the grocery item");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(cliOptions, args);

        String fileName = cmd.getOptionValue("s");
        String format = cmd.getOptionValue("f", "json").toLowerCase();
        String category = cmd.getOptionValue("c", "default");

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            throw new IllegalArgumentException("Missing Command");
        }

        return new CommandLineArgs(fileName, format, category, positionalArgs);
    }
}
