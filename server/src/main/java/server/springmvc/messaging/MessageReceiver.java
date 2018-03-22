package server.springmvc.messaging;

import javax.jms.JMSException;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import server.springmvc.Gmail;
import server.springmvc.service.OrderService;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Component
public class MessageReceiver {

	private static final String ORDER_RESPONSE_QUEUE = "order-response-queue";
	
	@Autowired
	OrderService orderService;
	
	
	@JmsListener(destination = ORDER_RESPONSE_QUEUE)
	public void receiveMessage(final Message<String> message) throws JMSException, UnsupportedEncodingException, MessagingException {

		String response = message.getPayload();
		System.out.println(response);
		String[] emails = response.split(" ");
		Gmail gmail = new Gmail(emails);
	}
}
