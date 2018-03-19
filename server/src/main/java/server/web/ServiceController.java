package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.Service;
import server.jpa.ServiceRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class ServiceController extends WebMvcConfigurerAdapter {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = (List<Service>) serviceRepository.findAll();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @RequestMapping(path = "/service", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateService(@RequestBody Service service)
    {
        List<Service> sourceService = serviceRepository.getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId());
        if(sourceService.size() != 0 && sourceService.get(0) == service)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        serviceRepository.save(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/service", method = RequestMethod.POST)
    public ResponseEntity<Void> createService(@RequestBody Service service)
    {
        List<Service> sourceService = serviceRepository.getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId());
        if(sourceService.size() != 0)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        serviceRepository.save(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/service", method = RequestMethod.GET)
    public ResponseEntity<Service> getService(@RequestParam String id)
    {
        Service service = serviceRepository.findOne(Long.parseLong(id));
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    @RequestMapping(path = "/service", method = RequestMethod.DELETE)
    public ResponseEntity<Service> deleteService(@RequestParam String id)
    {
        serviceRepository.delete(Long.parseLong(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/serviceForAvOrder", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getServicesForAvOrder(@RequestParam long id,
                                                               @RequestParam String serviceType,
                                                               @RequestParam String carType) {
        List<Service> services = serviceRepository.getServicesByWorkerIdAndPriceServiceTypeAndPriceCarTypeCarType(
                id, serviceType, carType);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @RequestMapping(path = "/serviceWorker", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getServicesByWorkerLogin(@RequestParam String login)
    {
        List<Service> services =  serviceRepository.getServicesByWorkerLogin(login);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @RequestMapping(path = "/servicePrice", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> getServicesByPriceId(@RequestParam String id)
    {
        List<Service> services =  serviceRepository.getServicesByPriceId(Long.parseLong(id));
        return new ResponseEntity<>(services, HttpStatus.OK);
    }
}
