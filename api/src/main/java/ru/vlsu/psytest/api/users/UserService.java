package ru.vlsu.psytest.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.psytest.api.users.Role;
import ru.vlsu.psytest.api.users.User;
import ru.vlsu.psytest.api.users.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void create(UserRepr userRepr){
        User user = new User();
        user.setLogin(userRepr.getLogin());
        user.setPassword(userRepr.getPassword());
        //чтобы зарегистрированному пользователю автоматически давалась роль юзера
        user.setRoles(Collections.singleton(new Role(2L, "USER")));
        repository.save(user);
    }

    //Получение всех пользователей
    public List<User> allUsers(){
        return repository.findAll();
    }

    //Удаление пользователя
    public boolean deleteUser(Integer id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}