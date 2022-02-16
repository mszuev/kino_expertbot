package ru.mzuev.kino_expertbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.model.Player;

public class PointService {
    private final PlayerService playerService;

    public PointService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void updatePoints(Update update, boolean answerIsCorrect) {
        long id = update.getCallbackQuery().getMessage().getChatId();

        if(playerService.isExist(id)) {
            Player player = playerService.getById(id);
            player.setId(id);
            player.setName(update.getCallbackQuery().getFrom().getFirstName());
            player.setLogin(update.getCallbackQuery().getFrom().getUserName());

            if(answerIsCorrect){
                player.setPoints(playerService.getById(id).getPoints() + 2);
            }else {
                if(playerService.getById(id).getPoints() - 3 < 1){
                    player.setPoints(0);
                }else {
                    player.setPoints(playerService.getById(id).getPoints() - 3);
                }
            }
            playerService.save(player);
        }
    }
}
