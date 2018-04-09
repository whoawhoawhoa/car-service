package server.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.jpa.OrderRepository;
import server.jpa.Worker;
import server.jpa.WorkerRepository;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class MonthPayment extends TimerTask {
    private final static long ONCE_PER_DAY = 1000*60*60*24;
    private final WorkerRepository workerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public MonthPayment(WorkerRepository workerRepository, OrderRepository orderRepository) {
        this.workerRepository = workerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run() {
        try {
            sendCheck();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void startTask(){
        Timer timer = new Timer();
        timer.schedule(this, 0, ONCE_PER_DAY);
    }

    private void sendCheck() throws MessagingException {
        if(workerRepository != null && orderRepository != null) {
            List<Worker> workers = workerRepository.findToPayment();
            for (Worker worker : workers) {
                long price = orderRepository.findToPayment(worker.getId());
                PaymentGmail gmail = new PaymentGmail(worker.getEmail(), price);
                gmail.sendEmail();
            }
        }
    }
}
