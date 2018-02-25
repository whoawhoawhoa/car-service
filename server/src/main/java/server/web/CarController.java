package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.Car;
import server.jpa.CarRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class CarController extends WebMvcConfigurerAdapter {
    private final CarRepository carRepository;

    @Autowired
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = (List<Car>) carRepository.findAll();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
}
