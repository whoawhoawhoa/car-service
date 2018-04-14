package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import server.jpa.OrderES;
import server.jpa.OrderESRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("rest/search")
public class OrderESController {

    @Autowired
    OrderESRepository orderESRepository;

    @GetMapping(value = "/order_es_by_id")
    public OrderES searchOrderESById(@RequestParam("id") Long id){
        return orderESRepository.findOrderESById(id);
    }

    @GetMapping(value = "/order_es_by_clientmark")
    public List<OrderES> searchOrderEsByClientMark(@RequestParam("clientmark") Long clientmark){
        return orderESRepository.findOrderESByClientMark(clientmark);
    }

    @GetMapping(value = "/all_order_es")
    public List<OrderES> searchAll(){
        List<OrderES> ordersList = new ArrayList<>();
        Iterable<OrderES> ordersEs = orderESRepository.findAll();
        ordersEs.forEach(ordersList::add);
        return ordersList;
    }

    @PostMapping(value = "/order_es")
    public ResponseEntity<Void> postOrderEs(@RequestBody OrderES orderES, UriComponentsBuilder builder){
        List<OrderES> orders = searchAll();
        long maxId = orders.get(orders.size()-1).getId()+1;
        orderES.setId(maxId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/order_es").build().toUri());
        try {
            orderESRepository.save(orderES);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/order_es_by_client", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByClientLogin(@RequestParam("login") String login) {
        List<OrderES> orders = orderESRepository.findOrderESByClient_Login(login);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/order_es_by_worker", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByWorkerLogin(@RequestParam("login") String login) {
        List<OrderES> orders = orderESRepository.findOrderESByWorker_Login(login);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/order_es_by_date_after", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByOrderDateAfter(@RequestParam("orderDate") Date date) {
        if(date!=null) {
            List<OrderES> filteredOrders = orderESRepository.findOrderESByOrderDateAfter(date);
            return new ResponseEntity<>(filteredOrders, HttpStatus.OK);
        }
        else{
            List<OrderES> ordersList = new ArrayList<>();
            Iterable<OrderES> ordersEs = orderESRepository.findAll();
            ordersEs.forEach(ordersList::add);
            return new ResponseEntity<>(ordersList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/order_es_by_service_type", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByServiceType(@RequestParam("serviceType") String serviceType) {
        List<OrderES> orders = orderESRepository.findOrderESByServiceType(serviceType);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/order_es_by_date_before", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByOrderDateBefore(@RequestParam("orderDate") Date date) {
        List<OrderES> orders = orderESRepository.findOrderESByOrderDateBefore(date);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/order_es_by_totalprice_greater_than", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByTotalPriceGreaterThan(@RequestParam("price") long price) {
        List<OrderES> orders = orderESRepository.findOrderESByTotalPriceGreaterThan(price);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/order_es_by_totalprice_less_than", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByTotalPriceLessThan(@RequestParam("price") long price) {
        List<OrderES> orders = orderESRepository.findOrderESByTotalPriceLessThan(price);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/order_es_by_part_of", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByClient_LoginContaining(@RequestParam("part") String part) {
        List<OrderES> orders = orderESRepository.findOrderESByClient_LoginContainingOrWorker_CityContainingOrWorker_LoginContainingOrClient_CityContainingOrServiceTypeContainingOrCar_CarType_CarType(part,part,part,part,part, part);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    //Т.к параметры с типом String могут прилететь с нулевыми значениями(""),
    //необходимо вызывать разные методы из репозитория, где отсутствует определенный нулевой параметр
    @RequestMapping(value = "/order_es_by_filter", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByFilter(@RequestParam("serviceType") String serviceType, @RequestParam("carType") String carType,
                                                            @RequestParam("clientLogin") String clientLogin, @RequestParam("workerLogin") String workerLogin,
                                                            @RequestParam("price1") long price1, @RequestParam("price2") long price2,
                                                            @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate,
                                                            @RequestParam("fromClientMark") long fromClientMark, @RequestParam("toClientMark") long toClientMark,
                                                            @RequestParam("fromWorkerMark") long fromWorkerMark,  @RequestParam("toWorkerMark") long toWorkerMark)
    {
        // Все поля со стринговыми типами данных пустые
        if(serviceType.equals("") && carType.equals("") && clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetween(
            price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        // Указан логин исполнителя
        if(serviceType.equals("") && carType.equals("") && clientLogin.equals("") && !workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndWorker_Login(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, workerLogin);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан логин клиента
        if(serviceType.equals("") && carType.equals("") && !clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_Login(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, clientLogin);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указаны оба логина
        if(serviceType.equals("") && carType.equals("") && !clientLogin.equals("") && !workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_LoginAndWorker_Login(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, clientLogin,workerLogin);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип машины
        if(serviceType.equals("") && !carType.equals("") && clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, carType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указаны тип машины + логин исполнителя
        if(serviceType.equals("") && !carType.equals("") && clientLogin.equals("") && !workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndWorker_Login(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, carType, workerLogin);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип машины + логин клиента
        if(serviceType.equals("") && !carType.equals("") && !clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndClient_Login(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, carType, clientLogin);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указаны логины + тип машины
        if(serviceType.equals("") && !carType.equals("") && !clientLogin.equals("") && !workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndClient_LoginAndWorker_Login(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, carType, clientLogin, workerLogin);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип услуги
        if(!serviceType.equals("") && carType.equals("") && clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndServiceType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, serviceType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип услуги + логин исполнителя
        if(!serviceType.equals("") && carType.equals("") && clientLogin.equals("") && !workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndWorker_LoginAndServiceType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, workerLogin, serviceType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип услуги + логин клиента
        if(!serviceType.equals("") && carType.equals("") && !clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_LoginAndServiceType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, clientLogin, serviceType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип услуги + логины
        if(!serviceType.equals("") && carType.equals("") && !clientLogin.equals("") && !workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_LoginAndWorker_LoginAndServiceType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, clientLogin,workerLogin, serviceType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип услуги + тип машины
        if(!serviceType.equals("") && !carType.equals("") && clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndServiceType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, carType, serviceType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип услуги + тип машины + логин исполнителя
        if(!serviceType.equals("") && !carType.equals("") && clientLogin.equals("") && !workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndWorker_LoginAndServiceType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, carType, workerLogin, serviceType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан тип услуги + тип машины + логин клиента
        if(!serviceType.equals("") && !carType.equals("") && !clientLogin.equals("") && workerLogin.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndClient_LoginAndServiceType(
                    price1,price2,fromDate,toDate,fromClientMark,toClientMark,fromWorkerMark,toWorkerMark, carType, clientLogin, serviceType);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        //Указан все стринговые переменные
        else {
            List<OrderES> orders = orderESRepository.findOrderESByServiceTypeAndCar_CarType_CarTypeAndClient_LoginAndWorker_LoginAndTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetween(
                    serviceType, carType, clientLogin, workerLogin, price1, price2, fromDate, toDate, fromClientMark, toClientMark, fromWorkerMark, toWorkerMark);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/order_es_by_client_filter", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByClientFilter(@RequestParam("clientLogin") String clientLogin, @RequestParam("serviceType") String serviceType,
                                                            @RequestParam("price1") long price1, @RequestParam("price2") long price2,
                                                            @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate) {
        if(serviceType.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByClient_LoginAndTotalPriceBetweenAndOrderDateBetween(clientLogin, price1, price2, fromDate, toDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        else {
            List<OrderES> orders = orderESRepository.findOrderESByClient_LoginAndServiceTypeAndTotalPriceBetweenAndOrderDateBetween(clientLogin, serviceType, price1, price2, fromDate, toDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/order_es_by_worker_filter", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByWorkerFilter(@RequestParam("workerLogin") String workerLogin, @RequestParam("serviceType") String serviceType,
                                                            @RequestParam("price1") long price1, @RequestParam("price2") long price2,
                                                            @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate) {
        if(serviceType.equals("")){
            List<OrderES> orders = orderESRepository.findOrderESByWorker_LoginAndTotalPriceBetweenAndOrderDateBetween(workerLogin, price1, price2, fromDate, toDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        else {
            List<OrderES> orders = orderESRepository.findOrderESByWorker_LoginAndServiceTypeAndTotalPriceBetweenAndOrderDateBetween(workerLogin, serviceType, price1, price2, fromDate, toDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/order_es_by_totalprice_between", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderESByTotalPriceBetween(@RequestParam("price1") long fromPrice, @RequestParam("price2") long toPrice) {
            List<OrderES> orders = orderESRepository.findOrderESByTotalPriceBetween(fromPrice, toPrice);
            return new ResponseEntity<>(orders, HttpStatus.OK);

    }

    @RequestMapping(value = "/order_es_by_car_id", method = RequestMethod.GET)
    public ResponseEntity<List<OrderES>> getOrderByCarId(@RequestParam("id") long id) {
        List<OrderES> orders = orderESRepository.findOrderESByCar_Id(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete_order_es_greater_than")
    public ResponseEntity<Void> deleteOrderGreaterThan(@RequestParam long id){
        orderESRepository.deleteByIdGreaterThan(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/order_es")
    public ResponseEntity<OrderES> updateOrder(@RequestBody OrderES orderES){
        OrderES sourceOrderES;
        sourceOrderES = orderESRepository.findOrderESById(orderES.getId());
        if(sourceOrderES == orderES)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        orderESRepository.save(orderES);
        return new ResponseEntity<>(orderES, HttpStatus.OK);
    }

    @DeleteMapping(value = "/order_es")
    public ResponseEntity<Void> deleteOrder(@RequestParam long id){
        orderESRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/order_es_by_id_grater_than")
    public ResponseEntity<Void> deleteOrderWithIdGraterThan(){
        orderESRepository.deleteOrderESByClient_IdGreaterThan(0);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}