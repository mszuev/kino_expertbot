package ru.mzuev.kino_expertbot.service;

import ru.mzuev.kino_expertbot.model.Player;
import java.util.List;

public interface PlayerService {
    List<Player> readAll();
    void save(Player player);
    Player getById (long id);
    boolean isExist (long id);
    void delete (long id);
    void deleteAll();
}
