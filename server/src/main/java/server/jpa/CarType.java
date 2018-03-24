package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "car_types")
public class CarType implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "carTypeSeq", sequenceName = "carTypes_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carTypeSeq")
    private long id;
    @NotNull
    @Column(name = "car_type")
    private String carType;
    @OneToMany(mappedBy = "carType", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Price> priceSet;
    @OneToMany(mappedBy = "carType", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Car> carSet;

    public CarType() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Set<Price> getPriceSet() {
        return priceSet;
    }

    public void setPriceSet(Set<Price> priceSet) {
        this.priceSet = priceSet;
    }

    public Set<Car> getCarSet() {
        return carSet;
    }

    public void setCarSet(Set<Car> carSet) {
        this.carSet = carSet;
    }

    @Override
    public String toString() {
        return "CarType{" +
                "id=" + id +
                ", carType='" + carType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarType carType1 = (CarType) o;
        return id == carType1.id &&
                Objects.equals(carType, carType1.carType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, carType);
    }
}