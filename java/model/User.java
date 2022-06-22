package model;

public class User extends Entity<Integer> {
    private String alias;
    public User() {
    }

    public User(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
