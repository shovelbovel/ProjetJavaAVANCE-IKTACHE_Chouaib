package com.example;

/**
 * Launcher class to start the JavaFX application.
 * This is needed to work around module system issues when running from classpath.
 */
public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}
