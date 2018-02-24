package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
