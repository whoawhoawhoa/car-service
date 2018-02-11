package server.jpa;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "clientSeq", sequenceName = "clients_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientSeq")
    private long id;
    @NotNull
    @Column(name = "login")
    private String login;
    @NotNull
    @Column(name = "password")
    private String password;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "fname")
    private String fname;
    @Column(name = "phone_number")
    private long pnumber;
    @Column(name = "city")
    private String city;
    @Column(name = "rating")
    private int rating;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Car> carSet;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Order> orderSet;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<AvailableOrder> availableOrderSet;

    public Client() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public long getPnumber() {
        return pnumber;
    }

    public void setPnumber(long number) {
        this.pnumber = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Set<Car> getCarSet() {
        return carSet;
    }

    public void setCarSet(Set<Car> carSet) {
        this.carSet = carSet;
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
        return "Worker{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", fname='" + fname + '\'' +
                ", number=" + pnumber +
                ", city='" + city + '\'' +
                ", rating=" + rating +
                '}';
    }
}
