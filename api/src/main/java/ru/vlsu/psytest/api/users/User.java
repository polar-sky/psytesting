package ru.vlsu.psytest.api.users;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {

    @Id
    private Integer ID;
    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    private String gender;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;


    public User() {
    }

    public User(Integer ID, String login, String password, String gender, Set<Role> roles) {
        this.ID = ID;
        this.login = login;
        this.password = password;
        this.gender = gender;
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
