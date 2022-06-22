package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Result extends Entity<Integer> {
    private Integer user;
    private Integer game;
    private Integer points;
    private LocalDateTime date;
    private Boolean word1;
    private Boolean word2;
    private Boolean word3;

    public Result() {
    }

    public Result(Integer userId, Integer gameId, Integer points, LocalDateTime date) {
        this.user = userId;
        this.game = gameId;
        this.points = points;
        this.date = date;
        this.word1 = false;
        this.word2 = false;
        this.word3 = false;
    }


    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getGame() {
        return game;
    }

    public void setGame(Integer game) {
        this.game = game;
    }

    public Boolean getWord1() {
        return word1;
    }

    public void setWord1(Boolean word1) {
        this.word1 = word1;
    }

    public Boolean getWord2() {
        return word2;
    }

    public void setWord2(Boolean word2) {
        this.word2 = word2;
    }

    public Boolean getWord3() {
        return word3;
    }

    public void setWord3(Boolean word3) {
        this.word3 = word3;
    }
}


