package server.jms.client_to_worker.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;



@Component
public class ClientJmsSender {

	@Autowired
	JmsTemplate clientJmsTemplate;

	public void sendMessage(final String order) {

		clientJmsTemplate.send(new MessageCreator(){
				@Override
				public Message createMessage(Session session) throws JMSException{
					ObjectMessage objectMessage = session.createObjectMessage(order);
					return objectMessage;
				}
			});
	}

}
