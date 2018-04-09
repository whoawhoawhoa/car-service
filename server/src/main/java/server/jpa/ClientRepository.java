package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    List<Client> findClientsByLogin(String login);
    List<Client> findClientsByLoginAndPassword(String login, String password);
    Client findClientById(Long id);
}