package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
import server.jpa.Client;
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

    private boolean isValid(Passport passport) {
        String check = Long.toString(passport.getNumber());
        return check.length() == 10;
    }

    @RequestMapping(value = "/passports", method = RequestMethod.GET)
    public ResponseEntity<List<Passport>> getAllPassports() {
        List<Passport> passports = (List<Passport>) passportRepository.findAll();
        return new ResponseEntity<>(passports, HttpStatus.OK);
    }

    @RequestMapping(path = "/passport", method = RequestMethod.GET)
    public ResponseEntity<Passport> getPassport(@RequestParam String id)
    {
        Passport passport = passportRepository.findOne(Long.parseLong(id));
        return new ResponseEntity<>(passport, HttpStatus.OK);
    }

    @RequestMapping(path = "/worker_passport", method = RequestMethod.GET)
    public ResponseEntity<Passport> getPassportByWorkerLogin(@RequestParam String login)
    {
        Passport passport = passportRepository.findPassportByWorkerLogin(login);
        if(passport == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(passport, HttpStatus.OK);
    }

    @RequestMapping(value = "/passport", method = RequestMethod.POST)
    public ResponseEntity<Void> postPassport(@RequestBody Passport passport, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/passport").build().toUri());
        if(passportRepository.findPassportByNumber(passport.getNumber()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(isValid(passport)) {
            try {
                passportRepository.save(passport);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/passport", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePassport(@RequestParam long id)
    {
        passportRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/passport", method = RequestMethod.PUT)
    public ResponseEntity<Passport> updatePassport(@RequestBody Passport passport)
    {
        try {
            if(isValid(passport)) {
                passportRepository.save(passport);
                return new ResponseEntity<>(passport, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/passports_to_admin", method = RequestMethod.GET)
    public ResponseEntity<List<Passport>> getPassportsByStatus(@RequestParam int status) {
        try {
            List<Passport> passports = passportRepository.findPassportByWorkerStatus(status);
            return new ResponseEntity<>(passports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
