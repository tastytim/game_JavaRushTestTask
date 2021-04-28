package com.game.service;


import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(Player player) {
        PlayerParamValidator.nullValidate(player);
        PlayerParamValidator.validateName(player.getName());
        PlayerParamValidator.validateTitle(player.getTitle());
        PlayerParamValidator.validateRace(player.getRace());
        PlayerParamValidator.validateProfession(player.getProfession());
        PlayerParamValidator.validate(player.getBirthday());
        PlayerParamValidator.validateExperience(player.getExperience());
        if (player.getBanned() == null) player.setBanned(false);
        PlayerParamValidator.updateLevel(player);
        PlayerParamValidator.updateUntilNextLevel(player);
        return playerRepository.save(player);
    }

    public Player updatePlayer (Long id,Player player) {
        PlayerParamValidator.validate(id);
        if (!playerRepository.findById(id).isPresent())  PlayerParamValidator.Error404("Not Exist ID");

        if (PlayerParamValidator.isEmptyBody(player)) return playerRepository.findById(id).get();

        Player dbPlayer = playerRepository.findById(id).get();

        if (player.getId() != null) {
            if (!player.getId().equals(dbPlayer.getId())) player.setId(dbPlayer.getId());
        } else player.setId(dbPlayer.getId());

        if (player.getName() != null) {
            PlayerParamValidator.validateName(player.getName());
        } else player.setName(dbPlayer.getName());

        if (player.getTitle() != null) {
            PlayerParamValidator.validateTitle(player.getTitle());
        } else player.setTitle(dbPlayer.getTitle());

        if (player.getRace() != null) {
            PlayerParamValidator.validateRace(player.getRace());
        } else player.setRace(dbPlayer.getRace());

        if (player.getProfession() != null) {
            PlayerParamValidator.validateProfession(player.getProfession());
        } else player.setProfession(dbPlayer.getProfession());

        if (player.getExperience() != null) {
            PlayerParamValidator.validateExperience(player.getExperience());
        }else player.setExperience(dbPlayer.getExperience());

        if (player.getBanned() == null) player.setBanned(dbPlayer.getBanned());

        if (player.getBirthday() != null) {
            PlayerParamValidator.validate(player.getBirthday());
        } else player.setBirthday(dbPlayer.getBirthday());

        PlayerParamValidator.updateLevel(player);
        PlayerParamValidator.updateUntilNextLevel(player);

        return playerRepository.save(player);
    }


    public Page<Player> getAll(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification,pageable);
    }

    public long getPlayersCount(Specification<Player> specification) {
        return playerRepository.count(specification);
    }

    public Player getById(Long id) {
        PlayerParamValidator.validate(id);

        if (playerRepository.findById(id).isPresent())
            return playerRepository.findById(id).get();
        else
            PlayerParamValidator.Error404("Not Exist ID");
        return null;

    }

    public void deleteById(Long id){
        PlayerParamValidator.validate(id);
        if (playerRepository.findById(id).isPresent()) playerRepository.deleteById(id);
        else PlayerParamValidator.Error404("Not Exist ID");
    }


}
