package server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
import server.jpa.Worker;
import server.jpa.WorkerRepository;

import java.util.List;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
public class WorkerController extends WebMvcConfigurerAdapter {
    private final WorkerRepository workerRepository;

    @Autowired
    public WorkerController(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @RequestMapping(value = "/worker", method = RequestMethod.POST)
    public ResponseEntity<Void> postWorker(@RequestBody Worker worker, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/worker").build().toUri());
        if(isValid(worker)) {
            if (workerRepository.findWorkerByLogin(worker.getLogin()).size() != 0) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            workerRepository.save(worker);
        } else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/worker", method = RequestMethod.GET)
    public ResponseEntity<Worker> getWorkerByLoginAndPassword(
            @RequestParam("login") String login, @RequestParam("password") String password) {
        List<Worker> workers = workerRepository.findWorkerByLoginAndPassword(login, password);
        if(workers.size() == 0) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            return new ResponseEntity<>(workers.get(0), HttpStatus.ACCEPTED);
        }
    }

    @RequestMapping(value = "/workers", method = RequestMethod.GET)
    public ResponseEntity<List<Worker>> getAllWorkers() {
        List<Worker> workers = (List<Worker>) workerRepository.findAll();
        return new ResponseEntity<>(workers, HttpStatus.OK);
    }

    @RequestMapping(value = "/workers_by_id", method = RequestMethod.GET)
    public ResponseEntity<List<Worker>> getWorkersByIds(@RequestParam List<Integer> ids) {
        List<Worker> workers = workerRepository.findWorkersByIdIn(ids);
        return new ResponseEntity<>(workers, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/worker", method = RequestMethod.PUT)
    public ResponseEntity<Worker> updateWorker(@RequestBody Worker worker)
    {
        try
        {
            List<Worker> workers = workerRepository.findWorkerByLogin(worker.getLogin());
            if(workers.size() != 0) {
                Worker sourceWorker = workers.get(0);
                if (sourceWorker.equals(worker))
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                worker.setUser(sourceWorker.getUser());
                workerRepository.save(worker);
                return new ResponseEntity<>(worker, HttpStatus.OK);
            } else {
                workerRepository.save(worker);
                return new ResponseEntity<>(worker, HttpStatus.OK);
            }
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/worker", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteWorker(@RequestParam String login)
    {
        Worker worker = workerRepository.findWorkerByLogin(login).get(0);
        workerRepository.delete(worker.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean isValid(Worker worker) {
        String check = Long.toString(worker.getPnumber());
        if(check.length() != 11)
            return false;
        check = worker.getName();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        check = worker.getFname();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        check = worker.getCity();
        if(!check.matches("[a-zA-Z]+"))
            return false;
        return true;
    }
}
