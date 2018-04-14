package server.jpa;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import server.jpa.Car;
import server.jpa.Client;
import server.jpa.Worker;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;

@Document(indexName = "carservice", type = "orders",shards = 1)
public class OrderES implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Field(type = FieldType.Double)
    private long clientMark;

    @Field(type = FieldType.Double)
    private long workerMark;

    @Field(type = FieldType.Date)
    private Date orderDate;

    private String serviceType;

    @Field(type = FieldType.Integer)
    private int totalPrice;
    /*
        0 - ПРИНЯТ
        1 - ОПЛАЧЕН
        2 - РАБОТНИК ЗАКОНЧИЛ
        3 - КЛИЕНТ ОЦЕНИВАЕТ (клиент подтвердил
                        выполнение после статуса 2)
        4 - РАБОТНИК ОЦЕНИВАЕТ
        5 - ОТМЕНЕН
        6 - ВЫПОЛНЕН
        * */
    @Field(type = FieldType.Integer)
    private int status;

    @NotNull
    private String address;

    private String commentary;
    @Field(type = FieldType.Object)
    private Client client;
    @Field(type = FieldType.Object)
    private Worker worker;
    @Field(type = FieldType.Object)
    private Car car;


    public long getId() {
        return id;
    }

    public OrderES(){}

    public OrderES(long id, long clientMark, long workerMark,
                   Date orderDate, String serviceType, int totalPrice,
                   int status, String address, String commentary) {
        this.id = id;
        this.clientMark = clientMark;
        this.workerMark = workerMark;
        this.orderDate = orderDate;
        this.serviceType = serviceType;
        this.totalPrice = totalPrice;
        this.status = status;
        this.address = address;
        this.commentary = commentary;
    }

    public void setId(long id) {

        this.id = id;
    }

    public double getClientMark() {
        return clientMark;
    }

    public void setClientMark(long clientMark) {
        this.clientMark = clientMark;
    }

    public double getWorkerMark() {
        return workerMark;
    }

    public void setWorkerMark(long workerMark) {
        this.workerMark = workerMark;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientMark=" + clientMark +
                ", workerMark=" + workerMark +
                ", orderDate=" + orderDate +
                ", serviceType='" + serviceType + '\'' +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", address='" + address + '\'' +
                ", commentary='" + commentary + '\'' +
                ", client=" + client +
                ", worker=" + worker +
                ", car=" + car +
                '}';
    }
}