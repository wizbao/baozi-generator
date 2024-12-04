package com.baozi.cli.example;

import picocli.CommandLine;

@CommandLine.Command
public class Main implements Runnable {
    @CommandLine.Option(names = "--interactive", arity = "0..1", interactive = true)
    String value;

    @Override
    public void run() {
        if (value == null && System.console() != null) {
            // alternatively, use Console::readPassword
            value = System.console().readLine("Enter value for --interactive: ");
        }
        System.out.println("You provided value '" + value + "'");
    }

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }
}