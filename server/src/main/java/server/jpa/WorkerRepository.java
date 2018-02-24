package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkerRepository extends CrudRepository<Worker, Long> {
    List<Worker> findWorkerByLogin(String login);
    List<Worker> findWorkerByLoginAndPassword(String login, String password);
}