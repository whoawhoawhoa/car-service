package server.jpa;

import org.springframework.data.repository.CrudRepository;
import sun.security.x509.AVA;

import java.util.List;

public interface AvailableOrderRepository extends CrudRepository<AvailableOrder, Long> {
    List<AvailableOrder> findAvailableOrderByClientId(long id);
    List<AvailableOrder> findAvailableOrderByCarId(long id);
}