package ru.mzuev.kino_expertbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mzuev.kino_expertbot.command.CommandContainer;
import ru.mzuev.kino_expertbot.model.Player;
import ru.mzuev.kino_expertbot.service.InlineKeyboard;
import ru.mzuev.kino_expertbot.service.PointService;
import ru.mzuev.kino_expertbot.service.SendBotMessageServiceImpl;
import ru.mzuev.kino_expertbot.service.PlayerService;

@Component
public class TelegramBot extends TelegramWebhookBot {

    @Value("${bot.webHookPath}")
    private String webHookPath;

    @Value("${bot.username}")
    private String botUserName;

    @Value("${bot.token}")
    private String botToken;

    private static String adminUserName;
    private static String adminChatId;
    private static String apiToken;
    private static String falseEmoji = "\u274C";
    private static String trueEmoji = "\u2705";
    private final CommandContainer commandContainer;
    private final PointService pointService;
    private final PlayerService playerService;

    @Autowired
    public TelegramBot(PlayerService playerService, InlineKeyboard inlineKeyboard) {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this, inlineKeyboard, playerService), playerService);
        this.pointService = new PointService(playerService);
        this.playerService = playerService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if(update.hasCallbackQuery()){

            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            Message message = callbackQuery.getMessage();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(message.getChatId().toString());
            editMessageText.setMessageId(message.getMessageId());

            Player player = playerService.getById(message.getChatId());

            if(playerService.isExist(message.getChatId())){

                if(data.startsWith(player.getCorrectFilmNameCheck())){
                    editMessageText.setText(trueEmoji + " +2 балла\n" + player.getCorrectFilmName());
                    pointService.updatePoints(update, true);
                } else {
                    editMessageText.setText(falseEmoji + " -3 балла\n" + "Правильный ответ - " + player.getCorrectFilmName());
                    pointService.updatePoints(update, false);
                }
            } else {
                editMessageText.setText("Не засчитано");
            }

            try {
                commandContainer.retrieveCommand("callBack").execute(update);
                execute(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if(update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText().trim();
            commandContainer.retrieveCommand(message.split(" ")[0]).execute(update);
        }
        return null;
    }

    @Value("${bot.admin}")
    public void setAdminUserName(String adminUserName) {
        TelegramBot.adminUserName = adminUserName;
    }
    public static String getAdminUserName() {
        return adminUserName;
    }

    @Value("${bot.apiToken}")
    public void setApiToken(String apiToken) {
        TelegramBot.apiToken = apiToken;
    }
    public static String getApiToken() {
        return apiToken;
    }

    @Value("${bot.adminChatId}")
    public void setAdminChatId(String adminChatId) {
        TelegramBot.adminChatId = adminChatId;
    }
    public static String getAdminChatId() {
        return adminChatId;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
}