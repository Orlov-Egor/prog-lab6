package server.commands;

import common.data.*;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MarineNotFoundException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "<ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument) {
//        try {
//            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
//            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
//
//            Long id = Long.parseLong(argument);
//            SpaceMarine oldMarine = collectionManager.getById(id);
//            if (oldMarine == null) throw new MarineNotFoundException();
//
//            String name = oldMarine.getName();
//            Coordinates coordinates = oldMarine.getCoordinates();
//            LocalDateTime creationDate = oldMarine.getCreationDate();
//            double health = oldMarine.getHealth();
//            AstartesCategory category = oldMarine.getCategory();
//            Weapon weaponType = oldMarine.getWeaponType();
//            MeleeWeapon meleeWeapon = oldMarine.getMeleeWeapon();
//            Chapter chapter = oldMarine.getChapter();
//
//            collectionManager.removeFromCollection(oldMarine);
//
//            if (marineAsker.askQuestion("Хотите изменить имя солдата?")) name = marineAsker.askName();
//            if (marineAsker.askQuestion("Хотите изменить координаты солдата?")) coordinates = marineAsker.askCoordinates();
//            if (marineAsker.askQuestion("Хотите изменить здоровье солдата?")) health = marineAsker.askHealth();
//            if (marineAsker.askQuestion("Хотите изменить категорию солдата?")) category = marineAsker.askCategory();
//            if (marineAsker.askQuestion("Хотите изменить оружие дальнего боя солдата?")) weaponType = marineAsker.askWeaponType();
//            if (marineAsker.askQuestion("Хотите изменить оружие ближнего боя солдата?")) meleeWeapon = marineAsker.askMeleeWeapon();
//            if (marineAsker.askQuestion("Хотите изменить орден солдата?")) chapter = marineAsker.askChapter();
//
//            collectionManager.addToCollection(new SpaceMarine(
//                id,
//                name,
//                coordinates,
//                creationDate,
//                health,
//                category,
//                weaponType,
//                meleeWeapon,
//                chapter
//            ));
//            Console.println("Солдат успешно изменен!");
//            return true;
//        } catch (WrongAmountOfElementsException exception) {
//            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
//        } catch (CollectionIsEmptyException exception) {
//            Console.printerror("Коллекция пуста!");
//        } catch (NumberFormatException exception) {
//            Console.printerror("ID должен быть представлен числом!");
//        } catch (MarineNotFoundException exception) {
//            Console.printerror("Солдата с таким ID в коллекции нет!");
//        } catch (IncorrectInputInScriptException exception) {}
        return false;
    }
}
