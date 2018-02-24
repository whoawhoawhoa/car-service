package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "prices")
public class Price {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "priceSeq", sequenceName = "prices_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priceSeq")
    private long id;
    @NotNull
    @Column(name = "service_type")
    private String serviceType;
    @NotNull
    @Column(name = "price")
    private long price;
    @ManyToOne
    private CarType carType;
    @JsonIgnore
    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL)
    private Set<Service> serviceSet;

    public Price() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public Set<Service> getServiceSet() {
        return serviceSet;
    }

    public void setServiceSet(Set<Service> serviceSet) {
        this.serviceSet = serviceSet;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", serviceType='" + serviceType + '\'' +
                ", price=" + price +
                ", carType=" + carType +
                '}';
    }
}