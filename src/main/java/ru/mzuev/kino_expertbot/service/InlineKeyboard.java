package ru.mzuev.kino_expertbot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mzuev.kino_expertbot.model.Player;
import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboard {
    private static String[] callbackDataArray;

    public InlineKeyboardMarkup getInlineKeyboardMarkup(Player player){

        int randomId1 = ((int) (Math.random() * 10) + 1);
        int randomId2 = ((int) (Math.random() * 10) + 1);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();

        button1.setText(player.getFilmNameById(player.getRandomId()));
        button1.setCallbackData("Button1");
        button2.setText(player.getFilmNameById(player.getRandomId()));
        button2.setCallbackData("Button2");
        button3.setText(player.getCorrectFilmName());

        callbackDataArray = player.getCallbackDataArray();

        if(callbackDataArray.length > 1){
            button3.setCallbackData(callbackDataArray[0] + " " + callbackDataArray[1]);
        }else {
            button3.setCallbackData(player.getCorrectFilmName());
        }

        button4.setText(player.getFilmNameById(player.getRandomId()));
        button4.setCallbackData("Button3");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        if (randomId1 > 5){
            row1.add(button1);
            row2.add(button2);
            row1.add(button3);
            row2.add(button4);
        }else {
            row1.add(button4);
            row2.add(button3);
            row1.add(button2);
            row2.add(button1);
        }

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        if (randomId2 > 5){
            rowList.add(row1);
            rowList.add(row2);
        }else {
            rowList.add(row2);
            rowList.add(row1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}