package server.jpa;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "services")
public class Service {
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
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", coef=" + coef +
                ", worker=" + worker +
                ", price=" + price +
                '}';
    }
}
