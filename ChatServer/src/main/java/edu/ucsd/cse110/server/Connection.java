package edu.ucsd.cse110.server;

import javax.jms.ConnectionFactory;
import javax.jms.Message;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import edu.ucsd.cse110.shared.Constants;

@Configuration
@ComponentScan
public class Connection {
	
	@Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(
                new ActiveMQConnectionFactory(Constants.ACTIVEMQ_URL));
    }
    
    @Bean
    MessageListenerAdapter receiver() {
        return new MessageListenerAdapter(this) {{
            setDefaultListenerMethod("receive");
            setMessageConverter(null); // disable automatic message conversion
        }};
    }
    
    @Bean
    SimpleMessageListenerContainer container(final MessageListenerAdapter messageListener,
            final ConnectionFactory connectionFactory) {
        return new SimpleMessageListenerContainer() {{
            setMessageListener(messageListener);
            setConnectionFactory(connectionFactory);
            setDestinationName(Constants.ADMIN_QUEUE_NAME);
        }};
    }

    @Bean
    JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
    
    public void receive( Message m ) throws Exception {
    	ChatServerApplication.getInstance().onMessage( m );
    }
}
