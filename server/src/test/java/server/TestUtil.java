package server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import server.jpa.*;

import java.io.IOException;
import java.nio.charset.Charset;
import server.jpa.*;

import java.sql.Date;
import java.util.Random;

public class TestUtil {

    private static final Random random = new Random();

    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static Admin produceAdmin() {
        Admin admin = new Admin();

        admin.setLogin(Double.toString(random.nextDouble()));
        admin.setPassword(Double.toString(random.nextDouble()));
        admin.setId(random.nextLong());
        admin.setUser(produceUser());
        admin.getUser().setLogin(admin.getLogin());
        admin.getUser().setPassword(admin.getPassword());
        admin.getUser().setRole(1);

        return admin;
    }

    public static Car produceCar() {
        Car car = new Car();

        car.setId(random.nextLong());
        car.setBrand("brand");
        car.setColor("color");
        car.setNumber("a222aa");
        car.setCarType(produceCarType());
        car.setClient(produceClient());

        return car;
    }

    public static AvailableOrder produceAvailableOrder() {
        AvailableOrder availableOrder = new AvailableOrder();

        availableOrder.setId(random.nextLong());
        availableOrder.setAddress(Double.toString(random.nextDouble()));
        availableOrder.setCommentary(Double.toString(random.nextDouble()));
        availableOrder.setOrderDate(new Date(random.nextInt(1),
                random.nextInt(12), random.nextInt(29)));
        availableOrder.setServiceType(Double.toString(random.nextDouble()));
        availableOrder.setCar(produceCar());
        availableOrder.setClient(produceClient());

        return availableOrder;
    }

    public static CarType produceCarType() {
        CarType carType = new CarType();

        carType.setId(random.nextLong());
        carType.setCarType(Double.toString(random.nextDouble()));

        return carType;
    }

    public static Client produceClient() {
        Client client = new Client();

        client.setId(random.nextLong());
        client.setLogin(Double.toString(random.nextDouble()));
        client.setPassword(Double.toString(random.nextDouble()));
        client.setName("name");
        client.setFname("fname");
        client.setPnumber(88005553535L);
        client.setCity("city");
        client.setRating(random.nextInt(5));
        client.setEmail("a@a.com");
        client.setUser(produceUser());
        client.getUser().setLogin(client.getLogin());
        client.getUser().setPassword(client.getPassword());
        client.getUser().setRole(2);

        return client;
    }

    public static Order produceOrder() {
        Order order = new Order();

        order.setId(random.nextLong());
        order.setClientMark(random.nextInt(5));
        order.setWorkerMark(random.nextInt(5));
        order.setOrderDate(new Date(random.nextInt(1),
                random.nextInt(12), random.nextInt(29)));
        order.setAddress(Double.toString(random.nextDouble()));
        order.setCommentary(Double.toString(random.nextDouble()));
        order.setServiceType(Double.toString(random.nextDouble()));
        order.setStatus(0);
        order.setTotalPrice(random.nextInt());
        order.setClient(produceClient());
        order.setCar(produceCar());
        order.setWorker(produceWorker());

        return order;
    }

    public static Passport producePassport() {
        Passport passport = new Passport();

        passport.setId(random.nextLong());
        passport.setIssuedBy(Double.toString(random.nextDouble()));
        passport.setNumber(3611451712L);
        passport.setWorker(produceWorker());

        return passport;
    }

    public static Price producePrice() {
        Price price = new Price();

        price.setId(random.nextLong());
        price.setPrice(random.nextLong());
        price.setServiceType(Double.toString(random.nextDouble()));
        price.setCarType(produceCarType());

        return price;
    }

    public static Service produceService() {
        Service service = new Service();

        service.setCoef(1 + Math.random()*10);
        service.setId(random.nextLong());
        service.setPrice(producePrice());
        service.setWorker(produceWorker());

        return service;
    }

    public static User produceUser() {
        User user = new User();

        user.setLogin(Double.toString(random.nextDouble()));
        user.setPassword(Double.toString(random.nextDouble()));
        user.setRole(random.nextInt(2) + 1);
        user.setId(random.nextLong());

        return user;
    }

    public static Worker produceWorker() {
        Worker worker = new Worker();

        worker.setId(random.nextLong());
        worker.setLogin(Double.toString(random.nextDouble()));
        worker.setPassword(Double.toString(random.nextDouble()));
        worker.setName("name");
        worker.setFname("fname");
        worker.setCity("city");
        worker.setPnumber(88005553535L);
        worker.setRating(random.nextInt(5));
        worker.setStatus(0);
        worker.setEmail("w@w.com");
        worker.setUser(produceUser());
        worker.getUser().setLogin(worker.getLogin());
        worker.getUser().setPassword(worker.getPassword());
        worker.getUser().setRole(3);

        return worker;
    }

}
