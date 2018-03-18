package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

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
    @Column(name="address")
    @NotNull
    private String address;
    @Column(name = "commentary")
    private String commentary;
    @Column(name = "workers")
    private long[] workers;
    @ManyToOne
    @JsonIgnore
    private Client client;
    @ManyToOne
    @JsonIgnore
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

    public long[] getWorkers() {
        return workers;
    }

    public void setWorkers(long[] workers) {
        this.workers = workers;
    }

    @Override
    public String toString() {
        return "AvailableOrder{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", serviceType='" + serviceType + '\'' +
                ", address='" + address + '\'' +
                ", commentary='" + commentary + '\'' +
                ", client=" + client +
                ", car=" + car +
                '}';
    }
}