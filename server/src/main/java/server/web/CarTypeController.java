package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
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

    @PostMapping(value = "/car_type")
    public ResponseEntity<Void> addCarType(@RequestBody CarType carType, UriComponentsBuilder builder)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/car_type").build().toUri());
        if(carTypeRepository.findByCarType(carType.getCarType()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        carTypeRepository.save(carType);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/car_types", method = RequestMethod.GET)
    public ResponseEntity<List<CarType>> getAllCarTypes() {
        List<CarType> carTypes = (List<CarType>) carTypeRepository.findAll();
        return new ResponseEntity<>(carTypes, HttpStatus.OK);
    }

    @PutMapping(value = "/cartypeupdate")
    public ResponseEntity<CarType> updateCarType(@RequestBody CarType carType){
        CarType sourceType;
        sourceType = carTypeRepository.findOne(carType.getId());
        if(sourceType == carType)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        carTypeRepository.save(carType);
        return new ResponseEntity<>(carType, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deletecartype")
    public ResponseEntity<Void> deleteCarType(@RequestParam long id){
        carTypeRepository.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/car_type", method = RequestMethod.GET)
    public ResponseEntity<CarType> getCarTypeById(@RequestParam String id) {
        CarType carType = carTypeRepository.findOne(Long.parseLong(id));
        return new ResponseEntity<>(carType, HttpStatus.OK);
    }

    @RequestMapping(value = "/car_type_type", method = RequestMethod.GET)
    public ResponseEntity<CarType> getCarTypeByType(@RequestParam String type) {
        CarType carType = carTypeRepository.findByCarType(type);
        return new ResponseEntity<>(carType, HttpStatus.OK);
    }


}