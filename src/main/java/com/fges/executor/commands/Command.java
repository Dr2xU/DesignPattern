package com.fges.executor.commands;

import java.io.IOException;
import java.util.List;

/**
 * Command interface that defines the contract for all commands.
 */
public interface Command {
    /**
     * Executes the command with the provided arguments.
     *
     * @param args the arguments associated with the command
     * @return 0 if successful, or an error code
     * @throws IOException if an I/O error occurs during execution
     */
    int execute(List<String> args) throws IOException;
}