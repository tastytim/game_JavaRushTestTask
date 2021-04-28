package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Service
public class ControllerPlayer {

    private final PlayerService playerService;

    @Autowired
    public ControllerPlayer(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/rest/players")
    public List<Player> getPlayersList(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "race", required = false) Race race,
                                       @RequestParam(value = "profession", required = false) Profession profession,
                                       @RequestParam(value = "after", required = false) Long after,
                                       @RequestParam(value = "before", required = false) Long before,
                                       @RequestParam(value = "banned", required = false) Boolean banned,
                                       @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                       @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                       @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                       @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                       @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                       @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return playerService.getAll(Specification
                        .where(PlayerSpecificFilter.filterStringLike(name, "name")
                                .and(PlayerSpecificFilter.filterStringLike(title, "title")))
                        .and(PlayerSpecificFilter.filterEqual(race, "race"))
                        .and(PlayerSpecificFilter.filterEqual(profession, "profession"))
                        .and(PlayerSpecificFilter.filterBool(banned, "banned"))
                        .and(PlayerSpecificFilter.filterBetween(after, before, "birthday"))
                        .and(PlayerSpecificFilter.filterBetween(minExperience, maxExperience, "experience"))
                        .and(PlayerSpecificFilter.filterBetween(minLevel, maxLevel, "level")),
                pageable).getContent();
    }


    @GetMapping(value = "/rest/players/count")
    public long getPlayersCount(@RequestParam(value = "name", required = false ) String name,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "race", required = false) Race race,
                                @RequestParam(value = "profession", required = false) Profession profession,
                                @RequestParam(value = "after", required = false) Long after,
                                @RequestParam(value = "before", required = false) Long before,
                                @RequestParam(value = "banned", required = false) Boolean banned,
                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        return playerService.getPlayersCount(Specification
                .where(PlayerSpecificFilter.filterStringLike(name, "name")
                        .and(PlayerSpecificFilter.filterStringLike(title, "title")))
                .and(PlayerSpecificFilter.filterEqual(race, "race"))
                .and(PlayerSpecificFilter.filterEqual(profession, "profession"))
                .and(PlayerSpecificFilter.filterBetween(after, before, "birthday"))
                .and(PlayerSpecificFilter.filterBool(banned, "banned"))
                .and(PlayerSpecificFilter.filterBetween(minExperience, maxExperience, "experience"))
                .and(PlayerSpecificFilter.filterBetween(minLevel, maxLevel, "level")));
    }


    @PostMapping(value = "/rest/players/{id}")
    public Player updatePlayerById(@PathVariable Long id, @RequestBody Player player) {
        return playerService.updatePlayer(id, player);
    }

    @PostMapping(value = "/rest/players")
    public Player createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    @DeleteMapping(value = "/rest/players/{id}")
    public void deletePlayerById(@PathVariable Long id) {
        playerService.deleteById(id);
    }

    @GetMapping(value = "/rest/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getById(id);
    }
}