package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminRepository extends CrudRepository<Admin, Long> {
    List<Admin> findAdminByLoginAndPassword(String login, String password);
    List<Admin> findAdminByLogin(String login);
    void deleteAdminByLogin(String login);
}
