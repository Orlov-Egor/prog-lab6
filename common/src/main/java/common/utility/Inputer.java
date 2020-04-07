package common.utility;

import common.exceptions.InputerException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Inputer {
    private static Scanner scanner;

    public static String nextLine() throws InputerException {
        try {
            return scanner.nextLine();
        } catch (Exception exception) {
            throw new InputerException();
        }
    }

    public static void open() {
        scanner = new Scanner(System.in);
    }

    public static void close() {
        scanner.close();
    }
}
