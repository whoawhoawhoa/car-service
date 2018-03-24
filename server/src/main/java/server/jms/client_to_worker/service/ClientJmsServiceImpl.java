package server.jms.client_to_worker.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.jms.client_to_worker.messaging.ClientJmsSender;


@Service("orderService")
public class ClientJmsServiceImpl implements ClientJmsService {


	@Autowired
	ClientJmsSender messageSender;

	
	@Override
	public void sendOrder(String order) {
		messageSender.sendMessage(order);
	}

}
