package server.jms.client_to_worker.messaging;

import javax.jms.JMSException;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import server.jms.client_to_worker.GmailFromClient;
import server.jms.client_to_worker.service.OrderService;

import java.io.UnsupportedEncodingException;


@Component
public class MessageReceiver {

	private static final String CLIENT_TO_WORKER_QUEUE = "client-to-worker-queue";
	
	@Autowired
	OrderService orderService;
	
	
	@JmsListener(destination = CLIENT_TO_WORKER_QUEUE)
	public void receiveMessage(final Message<String> message) throws JMSException, UnsupportedEncodingException, MessagingException {

		String response = message.getPayload();
		System.out.println(response);
		String[] emails = response.split(" ");
		GmailFromClient gmail = new GmailFromClient(emails);
		gmail.sendEmail();
	}


}
