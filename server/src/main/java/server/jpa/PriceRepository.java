package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriceRepository extends CrudRepository<Price, Long> {
    Price findPriceById(Long id);
    List<Price> findByCarTypeCarType(String carType);
    List<Price> findPriceByServiceTypeOrderByCarType(String serviceType);
    List<Price> findPriceByCarTypeCarTypeAndServiceType(String carType, String serviceType);
}