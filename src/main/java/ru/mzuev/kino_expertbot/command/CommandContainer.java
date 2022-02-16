package ru.mzuev.kino_expertbot.command;

import com.google.common.collect.ImmutableMap;
import ru.mzuev.kino_expertbot.command.adminCommand.DeleteAllCommand;
import ru.mzuev.kino_expertbot.command.adminCommand.DeleteCommand;
import ru.mzuev.kino_expertbot.command.adminCommand.GetAllPlayersCommand;
import ru.mzuev.kino_expertbot.service.PlayerService;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;
import static ru.mzuev.kino_expertbot.command.CommandName.*;

public class CommandContainer {
    private final ImmutableMap <String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, PlayerService playerService) {

        commandMap = ImmutableMap.<String, Command> builder()
                .put(START_COMMAND.getCommandName(), new StartCommand(sendBotMessageService, playerService))
                .put(STATISTIC_COMMAND.getCommandName(), new StatisticCommand(sendBotMessageService, playerService))
                .put(DELETE_COMMAND.getCommandName(), new DeleteCommand(sendBotMessageService, playerService))
                .put(DELETE_ALL_COMMAND.getCommandName(), new DeleteAllCommand(sendBotMessageService, playerService))
                .put(GET_ALL_COMMAND.getCommandName(), new GetAllPlayersCommand(sendBotMessageService, playerService))
                .put("callBack", new CallBackAnswer(sendBotMessageService, playerService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String messageIdentifier) {
        return commandMap.getOrDefault(messageIdentifier, unknownCommand);
    }
}
