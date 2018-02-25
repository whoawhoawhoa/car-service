package server.jpa;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "adminSeq", sequenceName = "admins_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adminSeq")
    private long id;
    @NotNull
    @Column(name = "login")
    private String login;
    @NotNull
    @Column(name = "password")
    private String password;

    public Admin() {

    }

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

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
