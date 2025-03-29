// CommandLineProcessor.java

package com.fges;

import org.apache.commons.cli.*;
import java.util.List;

public class CommandLineProcessor {
    public CommandLineArgs parseArgs(String[] args) throws ParseException {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the grocery list");
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(cliOptions, args);
        
        String fileName = cmd.getOptionValue("s");
        List<String> positionalArgs = cmd.getArgList();
        
        if (positionalArgs.isEmpty()) {
            throw new IllegalArgumentException("Missing Command");
        }
        
        return new CommandLineArgs(fileName, positionalArgs);
    }
}
