package server.jms.client_to_worker.messaging;

import javax.jms.JMSException;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import server.jms.client_to_worker.ClientGmail;
import server.jms.client_to_worker.service.ClientJmsService;

import java.io.UnsupportedEncodingException;


@Component
public class ClientJmsReceiver {

	private static final String CLIENT_TO_WORKER_QUEUE = "client-to-worker-queue";
	
	@Autowired
	ClientJmsService clientJmsService;

	
	@JmsListener(destination = CLIENT_TO_WORKER_QUEUE)
	public void receiveMessage(final Message<String> message) throws JMSException, UnsupportedEncodingException, MessagingException {

		String response = message.getPayload();
		String[] emails = response.split(" ");
		ClientGmail gmail = new ClientGmail(emails);
		gmail.sendEmail();
	}


}
