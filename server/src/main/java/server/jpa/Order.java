package server.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "orderSeq", sequenceName = "orders_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    private long id;
    @Column(name = "client_mark")
    private double clientMark;
    @Column(name = "worker_mark")
    private double workerMark;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "service_type")
    private String serviceType;
    @Column(name = "total_price")
    private int totalPrice;
    @Column(name = "status")
    /*
    0 - IN_PROCESS
    1- FINISHED
    2- REJECTED
    * */
    private int status;
    @Column(name="address")
    @NotNull
    private String address;
    @Column(name="commentary")
    private String commentary;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Worker worker;
    @ManyToOne
    private Car car;

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getClientMark() {
        return clientMark;
    }

    public void setClientMark(double clientMark) {
        this.clientMark = clientMark;
    }

    public double getWorkerMark() {
        return workerMark;
    }

    public void setWorkerMark(double workerMark) {
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
    public Order clone() throws CloneNotSupportedException {
        Order o = new Order();
        o.setId(this.id);
        o.setWorker(this.worker);
        o.setCar(this.getCar());
        o.setClient(this.getClient());
        o.setTotalPrice(this.totalPrice);
        o.setStatus(this.status);
        o.setServiceType(this.serviceType);
        o.setCommentary(this.commentary);
        o.setAddress(this.address);
        o.setOrderDate(this.orderDate);
        o.setWorkerMark(this.workerMark);
        o.setClientMark(this.clientMark);
        return o;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (Double.compare(order.clientMark, clientMark) != 0) return false;
        if (Double.compare(order.workerMark, workerMark) != 0) return false;
        if (totalPrice != order.totalPrice) return false;
        if (status != order.status) return false;
        if (orderDate != null ? (orderDate.getDate() != order.orderDate.getDate()
                || orderDate.getMonth() != order.orderDate.getMonth()
                || orderDate.getYear() != order.orderDate.getYear()) : order.orderDate != null) return false;
        if (serviceType != null ? !serviceType.equals(order.serviceType) : order.serviceType != null) return false;
        if (address != null ? !address.equals(order.address) : order.address != null) return false;
        if (commentary != null ? !commentary.equals(order.commentary) : order.commentary != null) return false;
        if (client != null ? !client.equals(order.client) : order.client != null) return false;
        if (worker != null ? !worker.equals(order.worker) : order.worker != null) return false;
        return car != null ? car.equals(order.car) : order.car == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(clientMark);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(workerMark);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (serviceType != null ? serviceType.hashCode() : 0);
        result = 31 * result + totalPrice;
        result = 31 * result + status;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (commentary != null ? commentary.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (worker != null ? worker.hashCode() : 0);
        result = 31 * result + (car != null ? car.hashCode() : 0);
        return result;
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