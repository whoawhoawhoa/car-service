package server.jms.worker_to_client.service;

import server.jpa.AvailableOrder;

import javax.mail.MessagingException;

public interface WorkerJmsService {
    void sendMessage(AvailableOrder availableOrder);
    void sendMail(AvailableOrder availableOrder) throws MessagingException;
}
