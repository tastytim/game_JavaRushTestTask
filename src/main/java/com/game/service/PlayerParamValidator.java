package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;

public class PlayerParamValidator {

    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final int NAME_LENGTH = 12;
    private static final int TITLE_LENGTH = 30;
    private static final int EXPERIENCE_RANGE_MAX = 10000000;
    private static final int EXPERIENCE_RANGE_MIN = 0;
    private static final int MIN_YEAR = 2000;
    private static final int MAX_YEAR = 3000;


    public static void nullValidate(Player player) {
        if (player.getName() == null) Error400("Invalid name");
        if (player.getTitle() == null) Error400("Invalid title");
        if (player.getRace() == null) Error400("Invalid race");
        if (player.getProfession() == null) Error400("Invalid profession");
        if (player.getBirthday() == null) Error400("Invalid birthday");
        if(player.getExperience()==null)Error400("Invalid Experience");
    }

    public static void validateRace(Race race) {
        if (race == null) Error404("Race Not Found");

        boolean si = false;
        for (Race race1 : Race.values()) {
            if (race.equals(race1)) {
                si = true;
            }
        }
        if (si = false) {
            Error404("Bad Request , race not found");
        }
    }

    public static void validateProfession(Profession profession) {
        if (profession == null) Error404("Race Not Found");

        boolean si = false;
        for (Profession profession1 : Profession.values()) {
            if (profession.equals(profession1)) {
                si = true;
            }
        }
        if (si = false) {
            Error404("Bad Request, profession not found");
        }
    }

    public static void validate(Long id) {
        if (id <= 0) Error400(String.format("Requested bad %d", id));
    }

    public static void validate(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);
        Long time = calendar.getTimeInMillis();
        if (time < 0) Error400("Requested bad time");
        if (calendar.get(Calendar.YEAR) <= MIN_YEAR || calendar.get(Calendar.YEAR) >= MAX_YEAR)
            Error400(String.format("Requested bad date %s, min Year = %s, max Year = %s", birthday, MIN_YEAR, MAX_YEAR));
    }

    public static void validateExperience(Integer experience) {
        if (experience > EXPERIENCE_RANGE_MAX || experience < EXPERIENCE_RANGE_MIN)
            Error400(String.format("Requested bad experience value, min = %s, max = %s", EXPERIENCE_RANGE_MIN, EXPERIENCE_RANGE_MAX));
    }

    public static void validateName(String name) {
        if (name.length() > NAME_LENGTH || name.isEmpty())
            Error404(String.format("Name %s is too long. Name must be up to 12 letters long", name));
    }

    public static void validateTitle(String title) {
        if (title.length() > TITLE_LENGTH || title.isEmpty())
            Error400(String.format("Title %s is too long. Title must be up to 30 letters long", title));
    }

    public static void updateLevel(Player player) {
        int k = (int) ((Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100);
        player.setLevel(k);
    }

    public static void updateUntilNextLevel(Player player) {
        int m = 50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience();
        player.setUntilNextLevel(m);
    }

    public static boolean isEmptyBody(Player player) {
        return (player.getName() == null && player.getTitle() == null && player.getRace() == null && player.getProfession() == null
                && player.getBirthday() == null);
    }


    public static void Error400(String err) {
        throw new ResponseStatusException(BAD_REQUEST, err);
    }

    public static void Error404(String err) {
        throw new ResponseStatusException(NOT_FOUND, err);
    }
}
