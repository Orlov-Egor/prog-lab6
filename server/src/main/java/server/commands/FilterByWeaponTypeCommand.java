package server.commands;

import common.data.Weapon;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'filter_by_weapon_type'. Filters the collection by weapon type.
 */
public class FilterByWeaponTypeCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public FilterByWeaponTypeCommand(CollectionManager collectionManager) {
        super("filter_by_weapon_type", "<weaponType>",
                "вывести элементы, значение поля weaponType которых равно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            Weapon weapon = Weapon.valueOf(stringArgument.toUpperCase());
            String filteredInfo = collectionManager.weaponFilteredInfo(weapon);
            if (!filteredInfo.isEmpty()) {
                ResponseOutputer.appendln(filteredInfo);
                return true;
            } else ResponseOutputer.appendln("В коллекции нет солдат с выбранным типом оружия!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (IllegalArgumentException exception) {
            ResponseOutputer.appenderror("Оружия нет в списке!");
            ResponseOutputer.appendln("Список оружия дальнего боя - " + Weapon.nameList());
        }
        return false;
    }
}
