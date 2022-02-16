package ru.mzuev.kino_expertbot.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mzuev.kino_expertbot.bot.TelegramBot;
import ru.mzuev.kino_expertbot.model.Player;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {
    private final TelegramBot bot;
    private InlineKeyboard inlineKeyboard;
    private PlayerService playerService;

    @Autowired
    public SendBotMessageServiceImpl(TelegramBot bot, InlineKeyboard inlineKeyboard, PlayerService playerService) {
        this.bot = bot;
        this.inlineKeyboard = inlineKeyboard;
        this.playerService = playerService;
    }

    @Override
    public void sendMessage(Update update, String text) {
        long chatId;

        if(update.getMessage() != null){
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageToAdmin(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(TelegramBot.getAdminChatId());
        sendMessage.setText(text);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPhoto(Update update, Player player) {
        long chatId;

        if(update.getMessage() != null){
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        SendPhoto sendPhoto;
        JSONArray json2;
        JSONObject json3;

        int correctFilmId;
        do {
            correctFilmId = player.getRandomId();
            String uri = "https://kinopoiskapiunofficial.tech/api/v2.2/films/" + correctFilmId + "/images";

            ResponseEntity<String> result = player.getResponseEntity(uri, TelegramBot.getApiToken());
            JSONObject json = new JSONObject(result.getBody());
            json2 = json.getJSONArray("items");


        } while (json2.isNull(0));

        player.setCorrectFilmId(correctFilmId);
        playerService.save(player);

        json3 = json2.getJSONObject(0);
        sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile((String) json3.get("imageUrl")));

        try {
            bot.execute(sendPhoto);
            bot.execute(sendInlineKeyBoardMessage(chatId, player));
        } catch (TelegramApiException e) {
            sendPhoto(update, player);
        }
    }

    public SendMessage sendInlineKeyBoardMessage(long chatId, Player player) {
        SendMessage sm = new SendMessage();
            sm.setChatId(String.valueOf(chatId));
            sm.setText("Выберите один из вариантов:");
            sm.setReplyMarkup(inlineKeyboard.getInlineKeyboardMarkup(player));
        return sm;
    }
}