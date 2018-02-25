package server.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
import server.jpa.Admin;
import server.jpa.AdminRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class AdminController extends WebMvcConfigurerAdapter {
    private final AdminRepository adminRepository;

    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = (List<Admin>) adminRepository.findAll();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public ResponseEntity<Void> postAdmin(@RequestBody Admin admin, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/admin").build().toUri());
        if(adminRepository.findAdminByLogin(admin.getLogin()).size() != 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try {
            adminRepository.save(admin);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<Admin> checkAuthA(@RequestParam("login") String login, @RequestParam("password") String password) {
        List<Admin> admins = adminRepository.findAdminByLoginAndPassword(login, password);
        if(admins.size() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(admins.get(0), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping(value = "/admin")
    public ResponseEntity<Void> deleteAdmin(@RequestParam("id") long id) {
        try {
            adminRepository.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/admin")
    public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin) {
        Admin sourceAdmin;
        try {
            sourceAdmin = adminRepository.findOne(admin.getId());
            if(sourceAdmin == admin)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            adminRepository.save(admin);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }
}
