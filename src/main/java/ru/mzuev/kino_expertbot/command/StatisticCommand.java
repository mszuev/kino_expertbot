package ru.mzuev.kino_expertbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.model.Player;
import ru.mzuev.kino_expertbot.service.PlayerService;
import ru.mzuev.kino_expertbot.service.SendBotMessageService;
import java.util.ArrayList;
import java.util.List;

public class StatisticCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final PlayerService playerService;

    public StatisticCommand(SendBotMessageService sendBotMessageService, PlayerService playerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.playerService = playerService;
    }

    public String getPointSuffix(int point) {
        int preLastDigit = point % 100 / 10;

        if (preLastDigit == 1) {
            return " баллов";
        }
        switch (point % 10) {
            case 1:
                return " балл";
            case 2:
            case 3:
            case 4:
                return " балла";
            default:
                return " баллов";
        }
    }

    @Override
    public void execute(Update update) {
        if(playerService.isExist(update.getMessage().getFrom().getId())) {
            int currentPoints = playerService.getById(update.getMessage().getFrom().getId()).getPoints();
            String pointSuffix = getPointSuffix(currentPoints);

            List<Player> allPlayers = playerService.readAll();
            List<Player> fiveTopPlayers = new ArrayList<>();

            if(allPlayers.size() <= 5) {
                fiveTopPlayers.addAll(allPlayers);
            }else {
                for (int i = 0; i < 5; i++) {
                    fiveTopPlayers.add(allPlayers.get(i));
                }
            }
            StringBuilder sb = new StringBuilder();
            String playerInfo;
            int pointLength;
            String space = "";
            String userName;

            for (Player player : fiveTopPlayers) {
                pointLength = player.getPoints();
                if(pointLength < 10) space = "   ";
                if(pointLength >= 10 && pointLength < 100) space = "  ";
                if(pointLength >= 100 && pointLength < 1000) space = " ";
                if(player.getLogin() == null){
                    userName = player.getName();
                } else {
                    userName = player.getLogin();
                }
                playerInfo = String.format("<code>%s%d %s</code>\n", space, player.getPoints(), userName);
                sb.append(playerInfo);
            }
            sendBotMessageService.sendMessage(update, "<b>Топ 5 игроков:</b>\n" + sb +
                    "\nВаша статистика - " + currentPoints + pointSuffix);
        } else sendBotMessageService.sendMessage(update, "Что-то пошло не так, нажмите /start");
    }
}
