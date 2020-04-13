package server.utility;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;

// TODO: Сделать обработку update

public class RequestHandler {
    private CommandManager commandManager;

    public RequestHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response handle(Request request) {
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument());
        commandManager.addToHistory(request.getCommandName());
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    private ResponseCode executeCommand(String command, String commandStringArgument,
                                        Object commandObjectArgument) {
        switch (command) {
            case "help":
                if (!commandManager.help(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeById(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "save":
                if (!commandManager.save(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                else return ResponseCode.EXIT;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "history":
                if (!commandManager.history(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "sum_of_health":
                if (!commandManager.sumOfHealth(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "max_by_melee_weapon":
                if (!commandManager.maxByMeleeWeapon(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "filter_by_weapon_type":
                if (!commandManager.filterByWeaponType(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "ping":
                break;
            default:
                throw new RuntimeException("Easter egg :)");
        }
        return ResponseCode.OK;
    }
}
