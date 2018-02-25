package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.AvailableOrder;
import server.jpa.AvailableOrderRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class AvailableOrderController extends WebMvcConfigurerAdapter {
    private final AvailableOrderRepository availableOrderRepository;

    @Autowired
    public AvailableOrderController(AvailableOrderRepository availableOrderRepository) {
        this.availableOrderRepository = availableOrderRepository;
    }

    @RequestMapping(value = "/available_orders", method = RequestMethod.GET)
    public ResponseEntity<List<AvailableOrder>> getAllAvailableOrders() {
        List<AvailableOrder> availableOrders = (List<AvailableOrder>) availableOrderRepository.findAll();
        return new ResponseEntity<>(availableOrders, HttpStatus.OK);
    }
}
