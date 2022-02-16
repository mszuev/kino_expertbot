package ru.mzuev.kino_expertbot.command.adminCommand;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.bot.TelegramBot;
import ru.mzuev.kino_expertbot.command.Command;
import ru.mzuev.kino_expertbot.service.PlayerService;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;
import static ru.mzuev.kino_expertbot.command.UnknownCommand.UNKNOWN_MESSAGE;

public class DeleteCommand implements Command {
    private final PlayerService playerService;
    private final SendBotMessageService sendBotMessageService;

    public DeleteCommand(SendBotMessageService sendBotMessageService, PlayerService playerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.playerService = playerService;
    }

    @Override
    public void execute(Update update) {
        if(update.getMessage().getChat().getUserName().equals(TelegramBot.getAdminUserName())) {
            String message = update.getMessage().getText();
            String[] messageArr = message.split(" ");
            if(messageArr.length > 1) {
                long id = Long.parseLong(messageArr[1]);
                playerService.delete(id);
                sendBotMessageService.sendMessageToAdmin(id + " удален");
            } else {
                sendBotMessageService.sendMessage(update, "/delete [id]");
            }
        } else {
            sendBotMessageService.sendMessage(update, UNKNOWN_MESSAGE);
        }
    }
}
