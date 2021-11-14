package ru.vlsu.psytest.api.users;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private Integer ID;
    private String login;
    private String password;
    private String gender;

    public User() {
    }

    public User(Integer ID, String login, String password, String gender) {
        this.ID = ID;
        this.login = login;
        this.password = password;
        this.gender = gender;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
