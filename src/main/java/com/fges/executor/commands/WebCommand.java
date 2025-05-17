package com.fges.executor.commands;

import com.fges.cli.CommandLineArgs;
import com.fges.core.GroceryListManager;
import com.fges.dao.GroceryListDAOFactory;
import com.fges.web.GroceryShopAdapter;
import fr.anthonyquere.GroceryShopServer;

import java.util.List;

/**
 * Command to start the grocery web server.
 * Launches a web interface for managing the grocery list.
 */
public class WebCommand implements Command {

    /** Parsed CLI arguments */
    private final CommandLineArgs args;

    /**
     * Constructs the WebCommand with parsed CLI arguments.
     *
     * @param args parsed command-line arguments
     */
    public WebCommand(CommandLineArgs args) {
        if (args == null) {
            throw new IllegalArgumentException("CommandLineArgs cannot be null.");
        }
        this.args = args;
    }

    /**
     * Executes the web command: starts a local server.
     * Defaults to port 8080 unless a valid custom port is passed after "web".
     *
     * @param unusedArgs ignored (command args already embedded in CommandLineArgs)
     * @return 0 if success, 1 if failure
     */
    @Override
    public int execute(List<String> unusedArgs) {
        int port = 8080;
        List<String> commandArgs = args.getCommandArgs();

        if (!commandArgs.isEmpty()) {
            try {
                port = Integer.parseInt(commandArgs.get(0));
            } catch (NumberFormatException e) {
                System.err.println("⚠ Invalid port '" + commandArgs.get(0) + "'. Falling back to 8080.");
            }
        }

        try {
            var dao = GroceryListDAOFactory.create(args.getFormat(), args.getFileName());
            var manager = new GroceryListManager(dao);
            var shop = new GroceryShopAdapter(manager);
            var server = new GroceryShopServer(shop);

            server.start(port);
            System.out.println("✅ Web server running at http://localhost:" + port);
            Thread.currentThread().join();
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Failed to start web server: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}