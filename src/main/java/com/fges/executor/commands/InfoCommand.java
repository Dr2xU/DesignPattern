package com.fges.executor.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Command to display system information such as the current date, operating system, and Java version.
 * This command retrieves and prints essential system information.
 */
public class InfoCommand implements Command {

    /**
     * Executes the 'info' command to display the current system information.
     * This includes the current date, operating system name, and Java version.
     *
     * @param args the arguments for the info command (ignored in this case)
     * @return 0 if successful
     */
    @Override
    public int execute(List<String> args) {
        String osName = System.getProperty("os.name");
        String javaVersion = System.getProperty("java.version");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        System.out.println("Today's date: " + currentDate);
        System.out.println("Operating System: " + osName);
        System.out.println("Java version: " + javaVersion);
        System.out.println();

        return 0;
    }
}