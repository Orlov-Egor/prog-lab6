package client.utility;

import client.App;
import common.exceptions.InputerException;
import common.interaction.Request;
import common.utility.Inputer;
import common.utility.Outputer;

// TODO: Полученияе объекта для команд типа add
// TODO: Получение скрипта

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private int rewriteAttempts = 0;

    public Request handle() {
        String[] userCommand;
        do {
            try {
                Outputer.print(App.PS1);
                userCommand = (Inputer.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
            } catch (InputerException exception) {
                Outputer.println();
                Outputer.printerror("Произошла ошибка при пользовательском вводе!");
                userCommand = new String[] {"", ""};
                rewriteAttempts++;
                if (rewriteAttempts >= maxRewriteAttempts) {
                    Outputer.printerror("Превышено количество попыток пользовательского ввода!");
                    System.exit(0);
                }
            }
        } while (!validateCommand(userCommand[0], userCommand[1]));
        return new Request(userCommand[0], userCommand[1]);
    }

    private boolean validateCommand(String command, String commandArgument) {
        // TODO: Валидация команды
        return !command.isEmpty();
    }
}
