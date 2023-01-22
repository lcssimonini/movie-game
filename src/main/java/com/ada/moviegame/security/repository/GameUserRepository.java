package com.ada.moviegame.security.repository;

import com.ada.moviegame.security.domain.GameUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserRepository extends JpaRepository<GameUser, Integer> {

  Optional<GameUser> findByUsername(String username);
}
