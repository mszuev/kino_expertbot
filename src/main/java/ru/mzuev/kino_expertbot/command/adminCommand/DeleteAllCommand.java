package ru.mzuev.kino_expertbot.command.adminCommand;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.bot.TelegramBot;
import ru.mzuev.kino_expertbot.command.Command;
import ru.mzuev.kino_expertbot.service.PlayerService;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;
import static ru.mzuev.kino_expertbot.command.UnknownCommand.UNKNOWN_MESSAGE;

public class DeleteAllCommand implements Command {
    private final PlayerService playerService;
    private final SendBotMessageService sendBotMessageService;

    public DeleteAllCommand(SendBotMessageService sendBotMessageService, PlayerService playerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.playerService = playerService;
    }

    @Override
    public void execute(Update update) {
        if(update.getMessage().getChat().getUserName().equals(TelegramBot.getAdminUserName())) {
            int countOfPlayers = playerService.readAll().size();
            playerService.deleteAll();
            sendBotMessageService.sendMessageToAdmin(countOfPlayers + " игроков удалено");
        } else {
            sendBotMessageService.sendMessage(update, UNKNOWN_MESSAGE);
        }
    }
}
