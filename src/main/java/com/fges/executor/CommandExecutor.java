package com.fges.executor;

import com.fges.cli.CommandLineArgs;
import com.fges.executor.commands.Command;

/**
 * CommandExecutor class that handles execution of CLI commands.
 * It delegates creation to {@link CommandFactory}.
 */
public class CommandExecutor {

    private final CommandLineArgs cliArgs;

    /**
     * Constructs a CommandExecutor with parsed command-line arguments.
     * @param cliArgs the fully parsed command-line arguments object
     */
    public CommandExecutor(CommandLineArgs cliArgs) {
        if (cliArgs == null) {
            throw new IllegalArgumentException("CommandLineArgs must not be null.");
        }
        this.cliArgs = cliArgs;
    }

    /**
     * Executes the appropriate command and returns an exit code.
     *
     * @return 0 on success, non-zero on failure
     * @throws Exception if the command fails
     */
    public int execute() throws Exception {
        Command command = CommandFactory.create(cliArgs);
        return command.execute(cliArgs.getCommandArgs());
    }
}