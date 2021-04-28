package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class PlayerSpecificFilter {

    private PlayerSpecificFilter(){}

    public static Specification<Player> filterStringLike(String value, String param) {
        return (root, query, cb) -> value == null ? null : cb.like(root.get(param), "%" + value + "%");
    }

    public static Specification<Player> filterBetween(Integer min, Integer max, String param) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(root.get(param), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get(param), min);
            return cb.between(root.get(param), min, max);
        };
    }


    public static Specification<Player> filterBetween(Long min, Long max, String param) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(root.get(param), new Date(max));
            if (max == null) return cb.greaterThanOrEqualTo(root.get(param), new Date(min));
            return cb.between(root.get(param), new Date(min), new Date(max));
        };
    }


    public static Specification<Player> filterBool(Boolean banned, String param) {
        return (root, query, cb) -> {
            if (banned == null) return null;
            if (banned) return cb.isTrue(root.get(param));
            return cb.isFalse(root.get(param));
        };
    }

    public static Specification<Player> filterEqual(Race race, String param) {
        return (root, query, cb) -> race == null ? null : cb.equal(root.get(param), race);
    }

    public static Specification<Player> filterEqual(Profession profession, String param) {
        return (root, query, cb) -> profession == null ? null : cb.equal(root.get(param), profession);
    }
}
