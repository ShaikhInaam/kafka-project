package com.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class LibraryEventsProducer {

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    private static String topic = "library-events";


    public ListenableFuture<SendResult<Integer,String>> sendLibraryEventMessageAsyncApproch2(LibraryEvent libraryEvent) throws Exception {
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer , String> producerRecord =
                buildProducerRecord(key,value,topic);

        ListenableFuture<SendResult<Integer,String>>
                listenableFuture = kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(@NotNull Throwable ex) {
                handleFailure(key , value , ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key,value,result);
            }

        });

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {

        List<Header> recordHeaders = Arrays.asList(new RecordHeader("event-source","scanner".getBytes()),
                                                   new RecordHeader("event-source","scanner".getBytes()));

        return new ProducerRecord<>(topic,null,key,value,recordHeaders);
    }


    public SendResult<Integer, String> sendLibraryEventMessageSynchronous(LibraryEvent libraryEvent) throws Exception {
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        SendResult<Integer,String> sendResult = (SendResult<Integer, String>)
                kafkaTemplate.sendDefault(key , value).get();

        return sendResult;

    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message and exception is {}",ex.getMessage());
        try{
            throw ex;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("Error sending the message and exception is lala {}",ex.getMessage());

        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message sent successfully key is : {} and the value is : {} ,Partition is : {} ", key , value , result.getRecordMetadata().partition());
    }


}
