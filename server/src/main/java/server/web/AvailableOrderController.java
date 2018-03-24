package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
import server.jms.client_to_worker.messaging.MessageSender;
import server.jms.worker_to_client.service.WorkerJmsService;
import server.jpa.AvailableOrder;
import server.jpa.AvailableOrderRepository;

import java.sql.Date;
import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class AvailableOrderController extends WebMvcConfigurerAdapter {
    private final AvailableOrderRepository availableOrderRepository;
    private final ServiceController serviceController;
    private MessageSender ms;
    private final WorkerJmsService workerJmsService;

    @Autowired
    public AvailableOrderController(AvailableOrderRepository availableOrderRepository,
                                    ServiceController serviceController,
                                    MessageSender messageSender,
                                    WorkerJmsService workerJmsService) {
        this.availableOrderRepository = availableOrderRepository;
        this.serviceController = serviceController;
        this.ms = messageSender;
        this.workerJmsService = workerJmsService;
    }

    @RequestMapping(value = "/available_orders", method = RequestMethod.GET)
    public ResponseEntity<List<AvailableOrder>> getAllAvailableOrders() {
        List<AvailableOrder> availableOrders = (List<AvailableOrder>) availableOrderRepository.findAll();
        return new ResponseEntity<>(availableOrders, HttpStatus.OK);
    }

    @RequestMapping(value = "/putavorder", method = RequestMethod.POST)
    public ResponseEntity<Void> postAvailableOrder(@RequestBody AvailableOrder order, UriComponentsBuilder builder) {
        order.setOrderDate(new Date(new java.util.Date().getTime()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/putavorder").build().toUri());
        checkWorkers(order);
        try {
            availableOrderRepository.save(order);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/avorderbyclientlogin", method = RequestMethod.GET)
    public ResponseEntity<List<AvailableOrder>> getAvailableOrderByClientLogin(@RequestParam("login") String login) {
        List<AvailableOrder> orders = availableOrderRepository.findAvailableOrderByClientLogin(login);
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
        try {
            availableOrderRepository.save(order);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        workerJmsService.sendMessage(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteavorder")
    public ResponseEntity<Void> deleteAvailableOrder(@RequestParam long id){
        availableOrderRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkWorkers(AvailableOrder avOrder)
    {
        String serviceType = avOrder.getServiceType();
        Long carTypeId = avOrder.getCar().getCarType().getId();
        List<String> workersEmails = serviceController.getWorkersEmailsByServices(serviceType, carTypeId);
        String msg = "";
        if(workersEmails != null) {
            for (String email: workersEmails) {
                msg+= email + " ";
            }
            ms.sendMessage("toNotify " + msg);
        }
    }
}