package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findCarsByClientLogin(String login);
    List<Car> getCarsByClientIdAndCarTypeId(Long id, Long carTypeId);
}