package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    List<Client> findClientByLogin(String login);
    List<Client> findClientByLoginAndPassword(String login, String password);
    Client findClientById(Long id);
}