package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
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

    private boolean isValid(Car car) {
        String check;
        if(car.getClient() == null || car.getCarType() == null)
            return false;
        check = car.getColor();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        check = car.getBrand();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        check = car.getNumber();
        if(!check.matches("\\w\\d\\d\\d\\w\\w"))
            return false;
        return true;
    }

    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = (List<Car>) carRepository.findAll();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @RequestMapping(value = "/car", method = RequestMethod.GET)
    public ResponseEntity<Car> getCarById(@RequestParam("id") long id) {
        Car car = carRepository.findOne(id);
        if(car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(car, HttpStatus.ACCEPTED);
        }
    }

    @RequestMapping(value = "/client_cars", method = RequestMethod.GET)
    public ResponseEntity<List<Car>> getCarsByClientLogin(@RequestParam("login") String login) {
        List<Car> cars = carRepository.findCarsByClientLogin(login);
        if(cars.size() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(cars, HttpStatus.ACCEPTED);
        }
    }

    @RequestMapping(value = "/car", method = RequestMethod.POST)
    public ResponseEntity<Void> postCar(@RequestBody Car car, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/car").build().toUri());
        if(isValid(car)) {
            try {
                carRepository.save(car);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/car")
    public ResponseEntity<Void> deleteCar(@RequestParam("id") long id) {
        try {
            carRepository.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/car")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        Car sourceCar;
        try {
            if(isValid(car)) {
                sourceCar = carRepository.findOne(car.getId());
                if (sourceCar == car)
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                carRepository.save(car);
            }
            else return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(car, HttpStatus.OK);
    }
}
