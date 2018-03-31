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
import java.util.Set;

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

    @RequestMapping(path = "/price", method = RequestMethod.POST)
    public ResponseEntity<Void> addPrice(@RequestBody Price price)
    {
        List<Price> prices = priceRepository.findPriceByCarTypeCarTypeAndServiceType(
                price.getCarType().getCarType(), price.getServiceType());
        if (prices != null && prices.size() != 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        priceRepository.save(price);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/updatePrice", method = RequestMethod.PUT)
    public ResponseEntity<Price> updatePrice(@RequestBody Price price)
    {
        try
        {
            Price sourcePrice = priceRepository.findPriceById(price.getId());
            if(sourcePrice.equals(price))
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            priceRepository.save(price);
            return new ResponseEntity<>(price, HttpStatus.OK);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/pricesByCarType")
    public ResponseEntity<List<Price>> getPricesByCarTypeCarType(@RequestParam String carType)
    {
        List<Price> prices =  priceRepository.findByCarTypeCarType(carType);
        for (int i = 0; i < prices.size(); i++) {
            if(prices.get(i).getServiceSet().size() == 0)
                prices.remove(prices.get(i));
        }
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }

    @GetMapping(path = "/pricesByServiceType")
    public ResponseEntity<List<Price>> getPricesByServiceType(@RequestParam String serviceType)
    {
        List<Price> prices =  priceRepository.findPriceByServiceTypeOrderByCarType(serviceType);
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }
}
