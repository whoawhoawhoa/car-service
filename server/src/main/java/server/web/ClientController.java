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
import server.jpa.Order;
import server.jpa.OrderRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class ClientController extends WebMvcConfigurerAdapter {
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;


    @Autowired
    public ClientController(ClientRepository clientRepository, OrderRepository orderRepository) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
    }

    private boolean isValid(Client client) {
        String check = Long.toString(client.getPnumber());
        if(check.length() != 11)
            return false;
        check = client.getName();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        check = client.getFname();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        check = client.getCity();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        check = client.getEmail();
        if(!check.matches("[a-zA-Z0-9]+[@][a-zA-Z]+[.][a-zA-Z]+"))
            return false;
        return true;
    }

    @RequestMapping(value = "/client", method = RequestMethod.POST)
    public ResponseEntity<Void> postClient(@RequestBody Client client, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/client").build().toUri());
        if(clientRepository.findClientsByLogin(client.getLogin()).size() != 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(isValid(client)) {
            try {
                clientRepository.save(client);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<Client> getClientByLoginAndPassword(
            @RequestParam("login") String login, @RequestParam("password") String password) {
        List<Client> clients = clientRepository.findClientsByLoginAndPassword(login, password);
        if(clients.size() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(clients.get(0), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = (List<Client>) clientRepository.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @RequestMapping(value = "/client", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClient(@RequestParam String login) {
        Client client = clientRepository.findClientsByLogin(login).get(0);
        clientRepository.delete(client.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/client", method = RequestMethod.PUT)
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        try
        {
            Client sourceClient = clientRepository.findClientById(client.getId());
            List<Client> clients = clientRepository.findClientsByLogin(client.getLogin());
            if(sourceClient.equals(client))
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            if(client.getRating() == -1)
                client.setRating(calcNewRating(client));
            client.setUser(sourceClient.getUser());
            clientRepository.save(client);
            return new ResponseEntity<>(client, HttpStatus.OK);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private double calcNewRating(Client client)
    {
        double sum = 0;
        int counter = 0;
        List<Order> orders = orderRepository.findOrderByClientLogin(client.getLogin());
        for (Order order : orders) {
            if (order.getWorkerMark() != 0) {
                sum += order.getWorkerMark();
                counter++;
            }
        }
        if(counter != 0 && sum != 0) {
            sum = sum / counter;
            sum = (double)Math.round(sum * 10d) / 10d;
        }
        return sum;
    }

}
