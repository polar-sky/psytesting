package ru.vlsu.psytest.api.users;

import javax.validation.constraints.NotBlank;


public class UserRepr {
    private Integer id;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
