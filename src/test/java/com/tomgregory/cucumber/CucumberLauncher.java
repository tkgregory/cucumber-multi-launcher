package com.tomgregory.cucumber;

import io.cucumber.core.cli.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class CucumberLauncher {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<List<String>> allExecutionArguments = new ArgumentParser().parseAllExecutionArguments(List.of(args));
        System.out.printf("Executing %s instances of Cucumber%n", allExecutionArguments.size());

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Byte>> futures = allExecutionArguments.stream().map(theseArgs -> executorService.submit(() -> {
            System.out.println("Starting CucumberLauncher execution");
            return Main.run(theseArgs.toArray(new String[]{}));
        })).toList();

        List<Byte> exitStatuses = waitForCompletion(futures);
        exitStatuses.stream().filter(exitStatus -> exitStatus != 0).findFirst().ifPresent(System::exit);
        System.exit(0);
    }


    private static List<Byte> waitForCompletion(List<Future<Byte>> futures) throws InterruptedException, ExecutionException {
        List<Byte> exitStatuses = new ArrayList<>();
        for (Future<Byte> future : futures) {
            exitStatuses.add(future.get());
        }
        return exitStatuses;
    }

    private static final class ArgumentParser {
        private static final String DIRECTORY_ARGUMENT = "--dir";

        public List<List<String>> parseAllExecutionArguments(List<String> args) {
            int executionCount = countExecutions(args);

            List<List<String>> allExecutionArguments = new ArrayList<>();
            IntStream.range(0, executionCount).forEach(
                    index -> allExecutionArguments.add(getIndividualExecutionArguments(args, index))
            );

            return allExecutionArguments;
        }

        private List<String> getIndividualExecutionArguments(List<String> args, int executionIndex) {
            List<String> argsCopy = new ArrayList<>(args);
            List<String> executionArguments = new ArrayList<>();

            String directoryArgument = "";

            while (!argsCopy.isEmpty()) {
                String arg = argsCopy.remove(0).trim();

                if (arg.startsWith(DIRECTORY_ARGUMENT)) {
                    String dir = argsCopy.remove(0).trim();
                    if (arg.equals(directoryArgumentKey(executionIndex))) {
                        directoryArgument = dir;
                    }
                } else {
                    executionArguments.add(arg);
                }
            }

            if (directoryArgument.isEmpty()) {
                throw new IllegalStateException(String.format("Directory argument %s was not found", directoryArgumentKey(executionIndex)));
            }
            executionArguments.add(directoryArgument);
            return executionArguments;
        }

        private String directoryArgumentKey(int executionIndex) {
            return String.format("%s%s", DIRECTORY_ARGUMENT, executionIndex);
        }

        private int countExecutions(List<String> args) {
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
}
