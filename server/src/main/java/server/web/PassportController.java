package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import server.jpa.Passport;
import server.jpa.PassportRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class PassportController extends WebMvcConfigurerAdapter {
    private final PassportRepository passportRepository;

    @Autowired
    public PassportController(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @RequestMapping(value = "/passports", method = RequestMethod.GET)
    public ResponseEntity<List<Passport>> getAllPassports() {
        List<Passport> passports = (List<Passport>) passportRepository.findAll();
        return new ResponseEntity<>(passports, HttpStatus.OK);
    }

    @RequestMapping(path = "/passport", method = RequestMethod.GET)
    public ResponseEntity<Passport> getPassport(String id)
    {
        Passport passport = passportRepository.findOne(Long.parseLong(id));
        return new ResponseEntity<>(passport, HttpStatus.OK);
    }
}
