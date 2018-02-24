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
import server.jpa.ClientRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class ClientController extends WebMvcConfigurerAdapter {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @RequestMapping(value = "/client", method = RequestMethod.POST)
    public ResponseEntity<Void> postClient(@RequestBody Client client, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/client").build().toUri());
        if(clientRepository.findClientByLogin(client.getLogin()).size() != 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        clientRepository.save(client);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<Client> checkAuthC(@RequestParam("login") String login, @RequestParam("password") String password) {
        List<Client> clients = clientRepository.findClientByLoginAndPassword(login, password);
        if(clients.size() == 0) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            return new ResponseEntity<>(clients.get(0), HttpStatus.ACCEPTED);
        }
    }

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = (List<Client>) clientRepository.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
