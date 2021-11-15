package ru.vlsu.psytest.api.users;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AuthorizationController {

    private final UserService userService;

    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public List<User> list() {
        return userService.allUsers();
    }

    //регистрируем пользователя
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@Valid UserRepr userRepr, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else
        //создаем пользователя
        userService.create(userRepr);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/user/{id}")
    @ApiOperation("Удаление пользователя по id")
    public void delete(@PathVariable Integer id){
        userService.deleteUser(id);
    }

}
