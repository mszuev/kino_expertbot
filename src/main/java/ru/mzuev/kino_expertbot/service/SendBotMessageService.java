package ru.mzuev.kino_expertbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mzuev.kino_expertbot.model.Player;

public interface SendBotMessageService {
    void sendMessage(Update update, String text);
    void sendMessageToAdmin (String text);
    void sendPhoto(Update update, Player player);
}