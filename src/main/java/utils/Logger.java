package utils;

public class Logger {

    public static void printNewLine() {
        System.out.println();
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static void printError(String message) {
        System.out.println(ConsoleColors.RED_BOLD + message + ConsoleColors.RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(ConsoleColors.GREEN_BOLD + message + ConsoleColors.RESET);
    }

    public static void printWarning(String message) {
        System.out.println(ConsoleColors.YELLOW_BOLD + message + ConsoleColors.RESET);
    }

    public static void printDetails(String message) {
        printNewLine();
        printNewLine();
        System.out.println(ConsoleColors.BLUE_BOLD + "> " + message + "" + ConsoleColors.RESET);
    }

    public static void printTitle(String message) {
        System.out.println(ConsoleColors.CYAN_UNDERLINED + message + ConsoleColors.RESET);
    }

    public static void printDebug(String message) {
        System.out.println(ConsoleColors.PURPLE_BOLD + message + ConsoleColors.RESET);
    }

    public static void printHeader(String message) {
        System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "* " + message.toUpperCase() + " *" + ConsoleColors.RESET);
    }

    public static void printDivider() {
        System.out.println("\n" + ConsoleColors.GREEN_BOLD_BRIGHT + "-----------------------------------------" + ConsoleColors.RESET);
    }

}

