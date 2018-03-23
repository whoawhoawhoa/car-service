package server.jms.worker_to_client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.jms.worker_to_client.WorkerGmail;
import server.jms.worker_to_client.messaging.WorkerJmsSender;
import server.jpa.AvailableOrder;

import javax.mail.MessagingException;

@Service("workerJmsService")
public class WorkerJmsServiceImpl implements WorkerJmsService {

    @Autowired
    WorkerJmsSender workerJmsSender;

    @Override
    public void sendMessage(AvailableOrder availableOrder) {
        workerJmsSender.sendMessage(availableOrder);
    }

    @Override
    public void sendMail(AvailableOrder availableOrder) throws MessagingException {
        WorkerGmail gmail = new WorkerGmail(availableOrder);
        gmail.sendEmail();
    }

}
