package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.CarType;
import server.jpa.CarTypeRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:9090"})
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

    @PutMapping(value = "/cartypeupdate")
    public ResponseEntity<CarType> updateCarType(@RequestBody CarType carType){
        CarType sourceType;
        sourceType = carTypeRepository.findOne(carType.getId());
        if(sourceType == carType)
            return new ResponseEntity<CarType>(HttpStatus.CONFLICT);
        carTypeRepository.save(carType);
        return new ResponseEntity<>(carType, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deletecartype")
    public ResponseEntity<Void> deleteCarType(@RequestParam long id){
        carTypeRepository.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}