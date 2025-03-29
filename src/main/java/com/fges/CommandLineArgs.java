// CommandLineArgs.java

package com.fges;

import java.util.List;

public class CommandLineArgs {
    private final String fileName;
    private final List<String> arguments;

    public CommandLineArgs(String fileName, List<String> arguments) {
        this.fileName = fileName;
        this.arguments = arguments;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCommand() {
        return arguments.get(0);
    }

    public List<String> getArguments() {
        return arguments;
    }
}