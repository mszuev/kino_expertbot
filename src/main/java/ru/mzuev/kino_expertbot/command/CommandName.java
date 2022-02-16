package ru.mzuev.kino_expertbot.command;

public enum CommandName {

    START_COMMAND("/start"),
    STATISTIC_COMMAND("/statistic"),
    DELETE_COMMAND("/delete"),
    DELETE_ALL_COMMAND("/delete_all"),
    GET_ALL_COMMAND("/get_all");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
