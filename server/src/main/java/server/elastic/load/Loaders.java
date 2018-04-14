package server.elastic.load;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import server.jpa.OrderESRepository;
import server.jpa.*;

@Component
public class Loaders {

    @Autowired
    ElasticsearchOperations operations;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    OrderESRepository orderESRepository;

//    @PostConstruct
//    @Transactional
//    public void loadAll(){
//
//        operations.putMapping(OrderES.class);
//        System.out.println("Loading Data");
//        orderESRepository.save(getData());
//        System.out.printf("Loading Completed");
//
//    }
//
//    private List<OrderES> getData() {
//        List<OrderES> orders = new ArrayList<>();
//        for (int i = 0; i < 100000; i++) {
//            long randWorker = (long) (Math.random() * 10) + 26;
//            long randClient = (long) (Math.random() * 6) + 20;
//            long randCar = (long) (Math.random() * 9) + 45;
//            long randMarkClient = (long) (Math.random() * 5) + 1;
//            long randMarkWorker = (long) (Math.random() * 5) + 1;
//            long randServiceType = (long) (Math.random() * 15) + 18;
//            int randPrice = (int) (Math.random() * 2001) + 10;
//            int randYear = (int) (Math.random() * 17) + 100;
//            int randMonth = (int) (Math.random() * 12) + 1;
//            int randDay = (int) (Math.random() * 30) + 1;
//            Date date = new Date(randYear, randMonth, randDay);
//            OrderES order = new OrderES();
//            order.setId(i);
//            order.setClient(clientRepository.findClientById(randClient));
//            order.setWorker(workerRepository.findOne(randWorker));
//            order.setCar(carRepository.findOne(randCar));
//            order.setClientMark(randMarkClient);
//            order.setWorkerMark(randMarkWorker);
//            order.setServiceType(priceRepository.findPriceById(randServiceType).getServiceType());
//            order.setStatus(1);
//            order.setTotalPrice(randPrice);
//            order.setAddress(clientRepository.findClientById(randClient).getCity());
//            order.setOrderDate(date);
//            orders.add(order);
//        }
//        return orders;
//    }
}