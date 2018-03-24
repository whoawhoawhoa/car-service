package server.jms.client_to_worker.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.jms.client_to_worker.messaging.MessageSender;


@Service("orderService")
public class OrderServiceImpl implements OrderService{


	@Autowired
	MessageSender messageSender;

	
	@Override
	public void sendOrder(String order) {
		messageSender.sendMessage(order);
	}

}
