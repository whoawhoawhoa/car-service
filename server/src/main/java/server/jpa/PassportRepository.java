package server.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PassportRepository extends CrudRepository<Passport, Long> {
    Passport findPassportByWorkerLogin(String login);
    Passport findPassportByNumber(long number);
    List<Passport> findPassportByWorkerStatus(int status);
}