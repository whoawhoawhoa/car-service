package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car implements Serializable {
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

    public Car(String number, String brand, String color) {
        this.number = number;
        this.brand = brand;
        this.color = color;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (id != car.id) return false;
        if (number != null ? !number.equals(car.number) : car.number != null) return false;
        if (brand != null ? !brand.equals(car.brand) : car.brand != null) return false;
        if (color != null ? !color.equals(car.color) : car.color != null) return false;
        if (client != null ? !client.equals(car.client) : car.client != null) return false;
        if (carType != null ? !carType.equals(car.carType) : car.carType != null) return false;
        if (orderSet != null ? !orderSet.equals(car.orderSet) : car.orderSet != null) return false;
        return availableOrderSet != null ? availableOrderSet.equals(car.availableOrderSet) : car.availableOrderSet == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (carType != null ? carType.hashCode() : 0);
        result = 31 * result + (orderSet != null ? orderSet.hashCode() : 0);
        result = 31 * result + (availableOrderSet != null ? availableOrderSet.hashCode() : 0);
        return result;
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