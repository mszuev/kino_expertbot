package ru.mzuev.kino_expertbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mzuev.kino_expertbot.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository <Player, Long> {
}
