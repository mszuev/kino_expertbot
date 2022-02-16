package ru.mzuev.kino_expertbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mzuev.kino_expertbot.model.Player;
import ru.mzuev.kino_expertbot.repository.PlayerRepository;
import java.util.Collections;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    @Override
    public void save(Player player) {
        playerRepository.save(player);
    }

    @Override
    public Player getById(long id) {
        if(playerRepository.existsById(id)){
            return playerRepository.getById(id);
        }
        return null;
    }

    @Override
    public List<Player> readAll() {
        List<Player> allPlayers = playerRepository.findAll();
        Collections.sort(allPlayers);
        return allPlayers;
    }

    @Override
    public boolean isExist(long id) {
       return playerRepository.existsById(id);
    }

    @Override
    public void delete(long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        playerRepository.deleteAll();
    }
}
