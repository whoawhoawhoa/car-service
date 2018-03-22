package server.springmvc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.springmvc.messaging.MessageSender;

import java.util.List;


@Service("orderService")
public class OrderServiceImpl implements OrderService{


	@Autowired
	MessageSender messageSender;

	
	@Override
	public void sendOrder(String order) {
		messageSender.sendMessage(order);
	}

}
