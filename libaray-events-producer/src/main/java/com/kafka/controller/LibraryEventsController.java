package com.kafka.controller;

import com.kafka.domain.LibraryEvent;
import com.kafka.domain.LibraryEventType;
import com.kafka.producer.LibraryEventsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class LibraryEventsController {

    @Autowired
    LibraryEventsProducer libraryEventsProducer;


    @PostMapping("/v1/libraryevent/asynccall")
    public ResponseEntity<LibraryEvent> postLibraryEventAsync(@RequestBody @Valid LibraryEvent libraryEvent) throws Exception {

          log.info("before sending library event");

          libraryEvent.setLibraryEventType(LibraryEventType.NEW);
          libraryEventsProducer.sendLibraryEventMessageAsyncApproch2(libraryEvent);

          log.info("after sending library event");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);

    }

    @PostMapping("/v1/libraryevent/synccall")
    public ResponseEntity<LibraryEvent> postLibraryEventSync(@RequestBody LibraryEvent libraryEvent) throws Exception {

        log.info("before sending library event");

        SendResult<Integer , String> sendResult =
                libraryEventsProducer.sendLibraryEventMessageSynchronous(libraryEvent);

        log.info("send result "+sendResult.toString());


        log.info("after sending library event");

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);

    }




}
