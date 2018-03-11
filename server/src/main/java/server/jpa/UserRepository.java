package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findUserByLogin(String login);
    List<User> findUserByLoginAndPassword(String login, String password);
}
