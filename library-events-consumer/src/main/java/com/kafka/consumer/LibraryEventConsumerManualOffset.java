package com.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Slf4j
public class LibraryEventConsumerManualOffset implements AcknowledgingMessageListener {

    @Override
    public void onMessage(Object o) {

    }

    @KafkaListener(topics = {"library-events"})
    @Override
    public void onMessage(ConsumerRecord consumerRecord, Acknowledgment acknowledgment) {
        log.info("Consumer Record : {} ",consumerRecord);
       // acknowledgment.acknowledge();
    }


}
