package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
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

    @RequestMapping(value = "/putavorder", method = RequestMethod.POST)
    public ResponseEntity<Void> postAvailableOrder(@RequestBody AvailableOrder order, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/putavorder").build().toUri());
        availableOrderRepository.save(order);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/avorderbyclientlogin", method = RequestMethod.GET)
    public ResponseEntity<List<AvailableOrder>> getAvailableOrderByClientLogin(@RequestParam("id") long id) {
        List<AvailableOrder> orders = availableOrderRepository.findAvailableOrderByClientId(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @RequestMapping(value = "/avorderbycarid", method = RequestMethod.GET)
    public ResponseEntity<List<AvailableOrder>> getAvailableOrderByCarId(@RequestParam("id") long id) {
        List<AvailableOrder> orders = availableOrderRepository.findAvailableOrderByCarId(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping(value = "/avorderupdate")
    public ResponseEntity<AvailableOrder> updateAvailableOrder(@RequestBody AvailableOrder order){
        AvailableOrder sourceOrder;
        sourceOrder = availableOrderRepository.findOne(order.getId());
        if(sourceOrder == order)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        availableOrderRepository.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteavorder")
    public ResponseEntity<Void> deleteAvailableOrder(@RequestParam long id){
        availableOrderRepository.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}