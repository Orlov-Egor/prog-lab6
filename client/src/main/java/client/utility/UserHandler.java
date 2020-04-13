package client.utility;

import client.App;
import common.data.*;
import common.exceptions.CommandUsageException;
import common.exceptions.IncorrectInputInScriptException;
import common.interaction.MarineRaw;
import common.interaction.Request;
import common.utility.Outputer;

import java.util.NoSuchElementException;
import java.util.Scanner;

// TODO: Сделать возможным выполнение скрипта

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private MarineAsker marineAsker;

    public UserHandler(Scanner userScanner, MarineAsker marineAsker) {
        this.userScanner = userScanner;
        this.marineAsker = marineAsker;
    }

    public Request handle() {
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        do {
            try {
                Outputer.print(App.PS1);
                userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
            } catch (NoSuchElementException | IllegalStateException exception) {
                Outputer.println();
                Outputer.printerror("Произошла ошибка при пользовательском вводе!");
                userCommand = new String[] {"", ""};
                rewriteAttempts++;
                if (rewriteAttempts >= maxRewriteAttempts) {
                    Outputer.printerror("Превышено количество попыток пользовательского ввода!");
                    System.exit(0);
                }
            }
            processingCode = processCommand(userCommand[0], userCommand[1]);
        } while (processingCode == ProcessingCode.ERROR);
        try {
            switch (processingCode) {
                case OBJECT:
                    MarineRaw marineAddRaw = new MarineRaw(
                            marineAsker.askName(),
                            marineAsker.askCoordinates(),
                            marineAsker.askHealth(),
                            marineAsker.askCategory(),
                            marineAsker.askWeaponType(),
                            marineAsker.askMeleeWeapon(),
                            marineAsker.askChapter()
                    );
                    return new Request(userCommand[0], userCommand[1], marineAddRaw);
                case UPDATE_OBJECT:
                    String name = marineAsker.askQuestion("Хотите изменить имя солдата?") ?
                            marineAsker.askName() : null;
                    Coordinates coordinates = marineAsker.askQuestion("Хотите изменить координаты солдата?") ?
                            marineAsker.askCoordinates() : null;
                    double health = marineAsker.askQuestion("Хотите изменить здоровье солдата?") ?
                            marineAsker.askHealth() : -1;
                    AstartesCategory category = marineAsker.askQuestion("Хотите изменить категорию солдата?") ?
                            marineAsker.askCategory() : null;
                    Weapon weaponType = marineAsker.askQuestion("Хотите изменить оружие дальнего боя солдата?") ?
                            marineAsker.askWeaponType() : null;
                    MeleeWeapon meleeWeapon = marineAsker.askQuestion("Хотите изменить оружие ближнего боя солдата?") ?
                            marineAsker.askMeleeWeapon() : null;
                    Chapter chapter = marineAsker.askQuestion("Хотите изменить орден солдата?") ?
                            marineAsker.askChapter() : null;
                    MarineRaw marineUpdateRaw = new MarineRaw(
                            name,
                            coordinates,
                            health,
                            category,
                            weaponType,
                            meleeWeapon,
                            chapter
                    );
                    return new Request(userCommand[0], userCommand[1], marineUpdateRaw);
                case SCRIPT:
                    Outputer.printerror("Как же я устал, зачем все вот это, сколько можно....");
                    break;
            }
            // TODO: Для чего-то все-таки был нужен этот эксепшн...
        }  catch (IncorrectInputInScriptException exception) {}
        return new Request(userCommand[0], userCommand[1]);
    }

    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "show":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "save":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add_if_min":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "remove_greater":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "sum_of_health":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "max_by_melee_weapon":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "filter_by_weapon_type":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<weapon_type>");
                    break;
                default:
                    Outputer.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Использование: '" + command + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }
}
