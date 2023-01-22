package com.ada.moviegame.security.service;

import com.ada.moviegame.security.domain.GameUser;
import com.ada.moviegame.security.repository.GameUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameUserService {

  private final GameUserRepository gameUserRepository;

  public GameUser getGameUser(String username) {
    return gameUserRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public GameUser saveGameUser(GameUser user) {
    log.info("Creating new game user {}", user);
    return gameUserRepository.save(user);
  }
}
