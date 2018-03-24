package server.jms.worker_to_client.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import server.jpa.AvailableOrder;

@Component
public class WorkerJmsSender {
    @Autowired
    JmsTemplate workerJmsTemplate;

    public void sendMessage(final AvailableOrder availableOrder) {
        workerJmsTemplate.send(session -> session.createObjectMessage(availableOrder));
    }

}
