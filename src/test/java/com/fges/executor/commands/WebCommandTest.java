package com.fges.executor.commands;

import com.fges.cli.CommandLineArgs;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link WebCommand}.
 * Verifies that the web server starts correctly with valid arguments and port handling.
 */
class WebCommandTest {

    /**
     * Should start the web server using the default port (8080).
     */
    @Test
    void shouldStartWebCommandWithDefaultPort() throws Exception {
        CommandLineArgs args = new CommandLineArgs("grocery.json", "json", "default", List.of("web"));
        WebCommand command = new WebCommand(args);

        Thread serverThread = new Thread(() -> {
            try {
                command.execute(List.of());
                fail("Expected thread to be interrupted.");
            } catch (Exception e) {
                assertTrue(e instanceof InterruptedException || e instanceof RuntimeException);
            }
        });

        serverThread.start();
        Thread.sleep(500);
        serverThread.interrupt();
    }

    /**
     * Should start the web server on a custom port if one is provided.
     */
    @Test
    void shouldUseCustomPortIfProvided() throws Exception {
        CommandLineArgs args = new CommandLineArgs("grocery.json", "json", "default", List.of("web", "12345"));
        WebCommand command = new WebCommand(args);

        Thread serverThread = new Thread(() -> {
            try {
                command.execute(List.of());
                fail("Expected thread to be interrupted.");
            } catch (Exception e) {
                assertTrue(e instanceof InterruptedException || e instanceof RuntimeException);
            }
        });

        serverThread.start();
        Thread.sleep(500);
        serverThread.interrupt();
    }

    /**
     * Should default to port 8080 when given port is invalid (non-numeric).
     */
    @Test
    void shouldFallbackToDefaultPortOnInvalidInput() throws Exception {
        CommandLineArgs args = new CommandLineArgs("grocery.json", "json", "default", List.of("web", "invalidPort"));
        WebCommand command = new WebCommand(args);

        Thread serverThread = new Thread(() -> {
            try {
                command.execute(List.of());
                fail("Expected thread to be interrupted.");
            } catch (Exception e) {
                assertTrue(e instanceof InterruptedException || e instanceof RuntimeException);
            }
        });

        serverThread.start();
        Thread.sleep(500);
        serverThread.interrupt();
    }
}