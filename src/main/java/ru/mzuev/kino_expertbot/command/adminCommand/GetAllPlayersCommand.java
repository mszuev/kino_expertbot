package ru.mzuev.kino_expertbot.command.adminCommand;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.bot.TelegramBot;
import ru.mzuev.kino_expertbot.command.Command;
import ru.mzuev.kino_expertbot.model.Player;
import ru.mzuev.kino_expertbot.service.PlayerService;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;
import java.util.List;
import static ru.mzuev.kino_expertbot.command.UnknownCommand.UNKNOWN_MESSAGE;

public class GetAllPlayersCommand implements Command {
    private final PlayerService playerService;
    private final SendBotMessageService sendBotMessageService;

    public GetAllPlayersCommand(SendBotMessageService sendBotMessageService, PlayerService playerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.playerService = playerService;
    }

    @Override
    public void execute(Update update) {
        if(update.getMessage().getChat().getUserName().equals(TelegramBot.getAdminUserName())) {
            List<Player> playerList = playerService.readAll();
            for(Player player : playerList) {
                sendBotMessageService.sendMessageToAdmin(player.toString());
            }
            sendBotMessageService.sendMessageToAdmin("Всего игроков " + playerList.size());
        } else {
            sendBotMessageService.sendMessage(update, UNKNOWN_MESSAGE);
        }
    }
}
