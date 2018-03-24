package server.jms.configuration;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class MessagingConfiguration {
    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    private static final String WORKER_TO_CLIENT_QUEUE = "worker-to-client-queue";
    private static final String CLIENT_TO_WORKER_QUEUE = "client-to-worker-queue";
    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate workerJmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestinationName(WORKER_TO_CLIENT_QUEUE);
        return template;
    }

    @Bean
    public JmsTemplate clientJmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestinationName(CLIENT_TO_WORKER_QUEUE);
        return template;
    }
}
