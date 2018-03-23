package server.jms.worker_to_client.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import server.jms.worker_to_client.service.WorkerJmsService;
import server.jpa.AvailableOrder;

import javax.jms.JMSException;
import javax.mail.MessagingException;

@Component
public class WorkerJmsReciever {

    private static final String WORKER_TO_CLIENT_QUEUE = "worker-to-client-queue";

    @Autowired
    WorkerJmsService workerJmsService;

    @JmsListener(destination = WORKER_TO_CLIENT_QUEUE)
    public void receiveMessage(final Message<AvailableOrder> message) throws JMSException, MessagingException {
        MessageHeaders headers =  message.getHeaders();
        AvailableOrder availableOrder = message.getPayload();
        workerJmsService.sendMail(availableOrder);
    }
}
