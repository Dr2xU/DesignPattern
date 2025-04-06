package com.fges.cli;

import java.util.List;

/**
 * Data class that encapsulates parsed command-line arguments.
 * This class provides access to the input file name, format (json/csv),
 * optional category, and command with its associated parameters.
 */
public class CommandLineArgs {

    /** The file name specified via the -s or --source option */
    private final String fileName;

    /** The format specified via the -f or --format option (default is "json") */
    private final String format;

    /** The category specified via the -c or --category option (default is "default") */
    private final String category;

    /** The raw list of positional arguments (e.g., ["add", "Milk", "2"]) */
    private final List<String> arguments;

    /**
     * Constructs a {@link CommandLineArgs} object with all parsed components.
     *
     * @param fileName  the name of the grocery list file
     * @param format    the format of the file (json or csv)
     * @param category  the category to associate with items
     * @param arguments the full list of positional arguments
     */
    public CommandLineArgs(String fileName, String format, String category, List<String> arguments) {
        this.fileName = fileName;
        this.format = format;
        this.category = category;
        this.arguments = arguments;
    }

    /**
     * @return the source file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the specified format (e.g., "json", "csv")
     */
    public String getFormat() {
        return format;
    }

    /**
     * @return the item category (e.g., "dairy", "default")
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the command name (first argument in the list)
     */
    public String getCommand() {
        return arguments.get(0);
    }

    /**
     * @return all positional arguments, including the command and its parameters
     */
    public List<String> getArguments() {
        return arguments;
    }
}
