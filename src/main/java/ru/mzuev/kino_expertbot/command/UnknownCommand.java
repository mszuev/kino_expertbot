package ru.mzuev.kino_expertbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;

public class UnknownCommand implements Command {
    public static final String UNKNOWN_MESSAGE = "Неизвестная команда";
    private final SendBotMessageService sendBotMessageService;

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
            sendBotMessageService.sendMessage(update, UNKNOWN_MESSAGE);
    }
}