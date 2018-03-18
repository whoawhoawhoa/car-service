package server.jpa;

import org.springframework.data.repository.CrudRepository;

public interface CarTypeRepository extends CrudRepository<server.jpa.CarType, Long>
{
    CarType findByCarType(String carType);
}