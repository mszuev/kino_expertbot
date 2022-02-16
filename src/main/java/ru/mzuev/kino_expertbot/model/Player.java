package ru.mzuev.kino_expertbot.model;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.mzuev.kino_expertbot.bot.TelegramBot;
import javax.persistence.*;
import java.util.Objects;

@Entity
public class Player implements Comparable<Player>{

    @Id
    private long id;
    private String firstname;
    private String login;
    private int points;

    private int correctFilmId;
    private String correctFilmName;
    private String correctFilmNameCheck;
    private String[] callbackDataArray;

    public Integer getRandomId() {
        return (int) (Math.random() * 150000) + 1;
    }

    public String getFilmNameById (int id) {
        ResponseEntity<String> result;
        int filmId = id;

        do {
            String uri = "https://kinopoiskapiunofficial.tech/api/v2.2/films/" + filmId;
            result = getResponseEntity(uri, TelegramBot.getApiToken());
            if(result == null) {
                filmId = getRandomId();
            }
        } while (result == null);

        JSONObject json = new JSONObject(Objects.requireNonNull(result.getBody()));

        if (json.get("nameRu").toString().equals("null")) {
            return json.get("nameOriginal").toString();
        }
        return json.get("nameRu").toString();
    }

    public ResponseEntity <String> getResponseEntity (String uri, String apiToken){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> result = null;

        try {
            result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        }catch (HttpClientErrorException e){
            System.out.println("[no body]");
        }
        return result;
    }

    public String getCorrectFilmNameCheck() {
        correctFilmName = getCorrectFilmName();
        callbackDataArray = correctFilmName.split(" ");

        if(callbackDataArray.length > 1) {
            correctFilmNameCheck = callbackDataArray[0] + " " + callbackDataArray[1];
        }else {
            correctFilmNameCheck = correctFilmName;
        }
        return correctFilmNameCheck;
    }

    public String[] getCallbackDataArray() {
        return callbackDataArray = getCorrectFilmName().split(" ");
    }

    @Override
    public int compareTo(Player o) {
        if (this.getPoints() == o.getPoints()) {
            return 0;
        } else if (this.getPoints() < o.getPoints()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return String.format("<code>%d</code> %s %s %d", id, firstname, login, points);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return firstname;
    }

    public void setName(String name) {
        this.firstname = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCorrectFilmName() {
        return getFilmNameById(correctFilmId);
    }

    public void setCorrectFilmId(int correctFilmId) {
        this.correctFilmId = correctFilmId;
    }
}