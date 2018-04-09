package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "services")
public class Service implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "serviceSeq", sequenceName = "services_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serviceSeq")
    private long id;
    @NotNull
    @Column(name = "coefficient")
    private double coef;
    @ManyToOne
    private Worker worker;
    @ManyToOne
    private Price price;

    public Service() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return id == service.id &&
                Double.compare(service.coef, coef) == 0 &&
                Objects.equals(worker, service.worker) &&
                Objects.equals(price, service.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, coef, worker, price);
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", coef=" + coef +
                ", worker=" + worker +
                ", price=" + price +
                '}';
    }
}