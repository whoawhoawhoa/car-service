package server.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkerRepository extends CrudRepository<Worker, Long> {
    List<Worker> findWorkerByLogin(String login);
    List<Worker> findWorkerByLoginAndPassword(String login, String password);
    List<Worker> findWorkersByIdIn(List<Long> ids);
    @Query(value = "SELECT * FROM WORKERS WHERE CAST((start_date - CURRENT_DATE) AS INT)%30=0", nativeQuery = true)
    List<Worker> findToPayment();
}