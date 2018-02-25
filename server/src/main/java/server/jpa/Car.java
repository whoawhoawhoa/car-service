package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "carSeq", sequenceName = "cars_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carSeq")
    private long id;
    @NotNull
    @Column(name = "number")
    private String number;
    @NotNull
    @Column(name = "brand")
    private String brand;
    @NotNull
    @Column(name = "color")
    private String color;
    @ManyToOne
    private Client client;
    @ManyToOne
    private CarType carType;
    @JsonIgnore
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<Order> orderSet;
    @JsonIgnore
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<AvailableOrder> availableOrderSet;

    public Car() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public Set<Order> getOrderSet() {
        return orderSet;
    }

    public void setOrderSet(Set<Order> orderSet) {
        this.orderSet = orderSet;
    }

    public Set<AvailableOrder> getAvailableOrderSet() {
        return availableOrderSet;
    }

    public void setAvailableOrderSet(Set<AvailableOrder> availableOrderSet) {
        this.availableOrderSet = availableOrderSet;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", number=" + number +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", client=" + client +
                ", carType=" + carType +
                '}';
    }
}