package server.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
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
}
