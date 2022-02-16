package ru.mzuev.kino_expertbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.model.Player;
import ru.mzuev.kino_expertbot.service.PlayerService;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;

public class StartCommand implements Command {
    private final PlayerService playerService;
    private final SendBotMessageService sendBotMessageService;
    private final static String START_MESSAGE = "Поехали";

    public StartCommand(SendBotMessageService sendBotMessageService, PlayerService playerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.playerService = playerService;
    }

    @Override
    public void execute(Update update) {
        Player player = new Player();

        player.setId(update.getMessage().getFrom().getId());
        player.setName(update.getMessage().getFrom().getFirstName());
        player.setLogin(update.getMessage().getFrom().getUserName());
        player.setPoints(0);
        String startGame = String.format("%d %s %s начал играть", player.getId(), player.getName(), player.getLogin());

        playerService.save(player);

        sendBotMessageService.sendMessage(update, START_MESSAGE);
        sendBotMessageService.sendPhoto(update, player);
        sendBotMessageService.sendMessageToAdmin(startGame);
    }
}