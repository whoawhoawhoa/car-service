package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.Price;
import server.jpa.PriceRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class PriceController extends WebMvcConfigurerAdapter {
    private final PriceRepository priceRepository;

    @Autowired
    public PriceController(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @RequestMapping(value = "/prices", method = RequestMethod.GET)
    public ResponseEntity<List<Price>> getAllPrices() {
        List<Price> prices = (List<Price>) priceRepository.findAll();
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }

    @RequestMapping(path = "/price", method = RequestMethod.GET)
    public ResponseEntity<Price> getPrice(@RequestParam String id)
    {
        Price price = priceRepository.findOne(Long.parseLong(id));
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    @RequestMapping(path = "/price", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePrice(@RequestParam String id)
    {
        priceRepository.delete(Long.parseLong(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/price", method = RequestMethod.PUT)
    public ResponseEntity<Void> addPrice(@RequestBody Price price)
    {
        priceRepository.save(price);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
