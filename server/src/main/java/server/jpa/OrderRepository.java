package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findOrderByClientLogin(String login);
    List<Order> findOrderByWorkerLogin(String login);
    List<Order> findOrderByCarId(long id);
}