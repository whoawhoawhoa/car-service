package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AvailableOrderRepository extends CrudRepository<AvailableOrder, Long> {
    List<AvailableOrder> findAvailableOrderByClientLogin(String login);
    List<AvailableOrder> findAvailableOrderByCarId(long id);
}