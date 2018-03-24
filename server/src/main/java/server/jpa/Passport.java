package server.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "passports")
public class Passport implements Serializable {
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
    @OneToOne
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passport passport = (Passport) o;

        if (id != passport.id) return false;
        if (number != passport.number) return false;
        return issuedBy != null ? issuedBy.equals(passport.issuedBy) : passport.issuedBy == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (number ^ (number >>> 32));
        result = 31 * result + (issuedBy != null ? issuedBy.hashCode() : 0);
        return result;
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