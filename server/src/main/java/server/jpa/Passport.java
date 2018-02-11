package server.jpa;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "passports")
public class Passport {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "passportSeq", sequenceName = "passports_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passportSeq")
    private long id;
    @NotNull
    @Column(name = "number")
    private long number;
    @NotNull
    @Column(name = "issued_by")
    private String issuedBy;
    @ManyToOne
    private Worker worker;

    public Passport() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "id=" + id +
                ", number=" + number +
                ", issuedBy='" + issuedBy + '\'' +
                ", worker=" + worker +
                '}';
    }
}
