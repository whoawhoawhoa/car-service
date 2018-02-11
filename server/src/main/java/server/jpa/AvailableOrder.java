package server.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "available_orders")
public class AvailableOrder {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "availableOrderSeq", sequenceName = "availableOrders_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "availableOrderSeq")
    private long id;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "service_type")
    private String serviceType;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Car car;

    public AvailableOrder() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "AvailableOrder{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", serviceType='" + serviceType + '\'' +
                ", client=" + client +
                ", car=" + car +
                '}';
    }
}