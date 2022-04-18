package com.tomgregory.cucumber;

import io.cucumber.core.cli.Main;

import java.util.ArrayList;
import java.util.List;

public class CucumberLauncher {
    private static final String DIRECTORY_ARGUMENT = "--dir";

    public static void main(String[] args) {
        parseExecutionArguments(List.of(args)).forEach(theseArgs -> Main.run(theseArgs.toArray(new String[]{})));
    }

    private static List<List<String>> parseExecutionArguments(List<String> args) {
        int executionCount = countExecutions(args);

        List<List<String>> allExecutionArguments = new ArrayList<>();
        for (int i = 0; i < executionCount; i++) {
            allExecutionArguments.add(getExecutionArguments(args, i));
        }

        return allExecutionArguments;
    }

    private static List<String> getExecutionArguments(List<String> args, int executionIndex) {
        List<String> argsCopy = new ArrayList<>(args);
        List<String> executionArguments = new ArrayList<>();

        String directoryArgument = "";

        while (!argsCopy.isEmpty()) {
            String arg = argsCopy.remove(0).trim();

            if (arg.startsWith(DIRECTORY_ARGUMENT)) {
                String dir = argsCopy.remove(0).trim();
                if (arg.equals(String.format("%s%s", DIRECTORY_ARGUMENT, executionIndex))) {
                    directoryArgument = dir;
                }
            } else {
                executionArguments.add(arg);
            }
        }

        executionArguments.add(directoryArgument);
        return executionArguments;
    }

    private static int countExecutions(List<String> args) {
        List<String> argsCopy = new ArrayList<>(args);
        int dirCount = 0;

        while (!argsCopy.isEmpty()) {
            String arg = argsCopy.remove(0).trim();

            if (arg.startsWith(DIRECTORY_ARGUMENT)) {
                argsCopy.remove(0);
                dirCount++;
            }
        }
        return dirCount;
    }
}
