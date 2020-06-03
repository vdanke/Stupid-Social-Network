package com.step.stupid.social.network.configuration.rabbitmq;

import com.step.stupid.social.network.service.ReceiveMessageHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String EXCHANGE = "my-exchange";
    public static final String FIRST_QUEUE = "my-exchange-queue-first";
    public static final String SECOND_QUEUE = "my-exchange-queue-second";

    @Bean
    public Queue firstQueue() {
        return new Queue(FIRST_QUEUE, false);
    }

    @Bean
    public Queue secondQueue() {
        return new Queue(SECOND_QUEUE, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding firstBinding(Queue firstQueue,
                                TopicExchange topicExchange) {
        return BindingBuilder.bind(firstQueue).to(topicExchange).with("my-exchange.*");
    }

    @Bean
    public Binding secondBinding(Queue secondQueue,
                                 TopicExchange topicExchange) {
        return BindingBuilder.bind(secondQueue).to(topicExchange).with("my-exchange.#");
    }

    @Bean
    public MessageListenerAdapter adapter(ReceiveMessageHandler handler) {
        return new MessageListenerAdapter(handler, "handleMessage");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory factory,
                                                    MessageListenerAdapter adapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(factory);
//        container.setQueueNames(FIRST_QUEUE, SECOND_QUEUE);
//        container.setMessageListener(adapter);

        return container;
    }
}
