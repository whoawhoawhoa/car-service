package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;

@Entity
@Table(name = "available_orders")
public class AvailableOrder implements Serializable {
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
    private int[] workers;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Car car;

    public AvailableOrder() {
    }

    public AvailableOrder(Date orderDate, String serviceType, String address, String commentary) {
        this.orderDate = orderDate;
        this.serviceType = serviceType;
        this.address = address;
        this.commentary = commentary;
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

    public int[] getWorkers() {
        return workers;
    }

    public void setWorkers(int[] workers) {
        this.workers = workers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AvailableOrder that = (AvailableOrder) o;

        if (id != that.id) return false;
        if (orderDate != null ? (orderDate.getDate() != that.orderDate.getDate()
        || orderDate.getMonth() != that.orderDate.getMonth()
        || orderDate.getYear() != that.orderDate.getYear())
                : that.orderDate != null) return false;
        if (serviceType != null ? !serviceType.equals(that.serviceType) : that.serviceType != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (commentary != null ? !commentary.equals(that.commentary) : that.commentary != null) return false;
        if (!Arrays.equals(workers, that.workers)) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        return car != null ? car.equals(that.car) : that.car == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (serviceType != null ? serviceType.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (commentary != null ? commentary.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(workers);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (car != null ? car.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AvailableOrder{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", serviceType='" + serviceType + '\'' +
                ", address='" + address + '\'' +
                ", commentary='" + commentary + '\'' +
                ", workers=" + Arrays.toString(workers) +
                ", client=" + client +
                ", car=" + car +
                '}';
    }
}