package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.Order;
import server.jpa.OrderRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class OrderController extends WebMvcConfigurerAdapter {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
