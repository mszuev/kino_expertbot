package ru.mzuev.kino_expertbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.model.Player;
import ru.mzuev.kino_expertbot.service.PlayerService;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;

public class CallBackAnswer implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final PlayerService playerService;

    public CallBackAnswer(SendBotMessageService sendBotMessageService, PlayerService playerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.playerService = playerService;
    }

    @Override
    public void execute(Update update) {
        if(playerService.isExist(update.getCallbackQuery().getMessage().getChatId())){
            Player player = playerService.getById(update.getCallbackQuery().getMessage().getChatId());
            sendBotMessageService.sendPhoto(update, player);
        }else sendBotMessageService.sendMessage(update, "Что-то пошло не так, нажмите /start");
    }
}
