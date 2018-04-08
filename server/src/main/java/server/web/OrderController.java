package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
import server.jms.client_to_worker.messaging.ClientJmsSender;
import server.jpa.Order;
import server.jpa.OrderRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class OrderController extends WebMvcConfigurerAdapter {
    private final OrderRepository orderRepository;
    private ClientJmsSender ms;

    @Autowired
    public OrderController(OrderRepository orderRepository, ClientJmsSender ms) {
        this.orderRepository = orderRepository;
        this.ms = ms;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<Order> postOrder(@RequestBody Order order, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/order").build().toUri());

        ms.sendMessage("toReportOnConfirm " + order.getWorker().getEmail());
        orderRepository.save(order);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/orderbyclient", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getOrderByClientLogin(@RequestParam("login") String login) {
        List<Order> orders = orderRepository.findOrderByClientLogin(login);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/orderbyworker", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getOrderByWorkerLogin(@RequestParam("login") String login) {
        List<Order> orders = orderRepository.findOrderByWorkerLogin(login);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/orderbycarid", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getOrderByCarId(@RequestParam("id") long id) {
        List<Order> orders = orderRepository.findOrderByCarId(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping(value = "/order")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order){
        Order sourceOrder = null;
        List<Order> sourceOrders = null;
        if(order.getId() != 0) {
            sourceOrder = orderRepository.findOne(order.getId());
        } else {
            sourceOrders = orderRepository.findOrderByClientAndWorkerAndOrderDateAndCar(
                    order.getClient(), order.getWorker(), order.getOrderDate(), order.getCar()
            );
        }
        if(sourceOrder != null && order.equals(sourceOrder))
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        if(sourceOrders != null && sourceOrders.size() != 0) {
            order.setId(sourceOrders.get(sourceOrders.size() - 1).getId());
        }
        orderRepository.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping(value = "/order")
    public ResponseEntity<Void> deleteOrder(@RequestParam long id){
        orderRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}