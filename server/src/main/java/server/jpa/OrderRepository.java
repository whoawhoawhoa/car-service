package server.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findOrderByClientLogin(String login);
    List<Order> findOrderByWorkerLogin(String login);
    List<Order> findOrderByCarId(long id);
    List<Order> findOrderByClientAndWorkerAndOrderDateAndCar(
            Client client, Worker worker, Date date, Car car);
    @Query(value = "SELECT SUM(total_price) FROM ORDERS WHERE (order_date - CURRENT_DATE) < 30" +
            "AND status = 6 AND worker_id = ?1", nativeQuery = true)
    Long findToPayment(long id);
}