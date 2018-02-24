package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.CarType;
import server.jpa.CarTypeRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class CarTypeController extends WebMvcConfigurerAdapter {
    private final CarTypeRepository carTypeRepository;

    @Autowired
    public CarTypeController(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    @RequestMapping(value = "/car_types", method = RequestMethod.GET)
    public ResponseEntity<List<CarType>> getAllCarTypes() {
        List<CarType> carTypes = (List<CarType>) carTypeRepository.findAll();
        return new ResponseEntity<>(carTypes, HttpStatus.OK);
    }
}
