// CommandExecutor.java

package com.fges;

import java.io.IOException;
import java.util.List;

public class CommandExecutor {
    private final GroceryListManager manager;
    
    public CommandExecutor(GroceryListManager manager) {
        this.manager = manager;
    }
    
    public int execute(String command, List<String> args) throws IOException {
        return switch (command) {
            case "add" -> addItem(args);
            case "list" -> listItems();
            case "remove" -> removeItem(args);
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }
    
    private int addItem(List<String> args) throws IOException {
        if (args.size() < 3) {
            throw new IllegalArgumentException("Missing arguments for 'add' command");
        }
        
        String itemName = args.get(1);
        int quantity;
        
        try {
            quantity = Integer.parseInt(args.get(2));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity must be a number");
        }
        
        manager.addItem(itemName, quantity);
        return 0;
    }
    
    private int listItems() {
        manager.listItems().forEach(System.out::println);
        return 0;
    }
    
    private int removeItem(List<String> args) throws IOException {
        if (args.size() < 2) {
            throw new IllegalArgumentException("Missing arguments for 'remove' command");
        }
        
        String itemName = args.get(1);
        manager.removeItem(itemName);
        return 0;
    }
}