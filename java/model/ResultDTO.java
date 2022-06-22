package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResultDTO extends Entity<Integer>{
    private String alias;
    private LocalDateTime date;
    private int points;

    public ResultDTO(String alias, LocalDateTime date, int points) {
        this.alias = alias;
        this.date = date;
        this.points = points;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
