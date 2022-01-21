package com.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.entity.LibraryEvent;
import com.kafka.jpa.LibraryEventJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class LibraryEventService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LibraryEventJpaRepository libraryEventJpaRepository;

    public void processLibraryEventRecord(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

      LibraryEvent libraryEvent =
              objectMapper.readValue(consumerRecord.value() , LibraryEvent.class);

      log.info("Library Event Record , {}" , libraryEvent);

      switch (libraryEvent.getLibraryEventType()){

          case NEW:
              saveLibraryEvent(libraryEvent);
              break;
          case UPDATE:
              if(libraryEvent.getLibraryEventId()==null)
                  throw new IllegalArgumentException("Library event id must not be null");
              Optional<LibraryEvent> optionalLibraryEvent
                      = libraryEventJpaRepository.findById(libraryEvent.getLibraryEventId());
              if(!optionalLibraryEvent.isPresent())
                  throw new IllegalArgumentException("Library event not found");
              saveLibraryEvent(libraryEvent);
              break;
          default:
              log.info("Invalid library event");
      }

    }


    public void saveLibraryEvent(LibraryEvent libraryEvent){

        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventJpaRepository.save(libraryEvent);

    }

}
