package server.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarTypeRepository extends CrudRepository<server.jpa.CarType, Long>
{
    CarType findByCarType(String carType);
    @Query(value = "SELECT DISTINCT ct.* from cars as c, car_types as ct WHERE c.client_id = ?1 and c.car_type_id = ct.id", nativeQuery = true)
    List<CarType> findDistinctCarTypesByClientId(Long id);
}