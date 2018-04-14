package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;

import server.jpa.*;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController extends WebMvcConfigurerAdapter {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<User> checkAuth(@RequestParam("login") String login, @RequestParam("password") String password) {
        List<User> users = userRepository.findUserByLoginAndPassword(login, password);
        if(users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(users.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<Void> postUser(@RequestBody User user, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/user").build().toUri());
        if(userRepository.findUserByLogin(user.getLogin()).size() != 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity<Void> deleteUser(@RequestParam("id") long id) {
        try {
            userRepository.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User sourceUser;
        try {
            sourceUser = userRepository.findOne(user.getId());
            List<User> users = userRepository.findUserByLogin(user.getLogin());
            if(sourceUser.equals(user) || users.size() != 0)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}