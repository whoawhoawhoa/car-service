package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    List<Service> getServicesByWorkerLogin(String login);
    List<Service> getServicesByPriceId(Long id);
    List<Service> getServicesByWorkerLoginAndPriceId(String login, Long id);
    List<Service> getServicesByWorkerIdAndPriceServiceTypeAndPriceCarTypeCarType(
            long id, String serviceType, String carType);
}