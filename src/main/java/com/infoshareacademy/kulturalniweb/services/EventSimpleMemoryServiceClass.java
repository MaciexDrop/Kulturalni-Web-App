package com.infoshareacademy.kulturalniweb.services;

import com.infoshareacademy.kulturalniweb.domainData.EventNew;
import com.infoshareacademy.kulturalniweb.domainData.EventSimple;
import com.infoshareacademy.kulturalniweb.repository.EventSimpleMemory;
import com.infoshareacademy.kulturalniweb.repository.ListEventRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventSimpleMemoryServiceClass {
    EventSimpleMemory eventSimpleMemory;
    ListEventRepository listEventRepository;

    public EventSimpleMemoryServiceClass(EventSimpleMemory eventSimpleMemory, ListEventRepository listEventRepository) {
        this.eventSimpleMemory = eventSimpleMemory;
        this.listEventRepository = listEventRepository;
    }

    public void prepareSimpleEventsListFromRepository() {
        List<EventSimple> listOfEventSimple = new ArrayList<>();

        for (int i = 0; i < listEventRepository.getEventsDB().size(); i++) {
            listOfEventSimple.add(getSingleEventSimpleFromList(i));
        }
        eventSimpleMemory.setListOfEventSimple(listOfEventSimple);
    }

    public EventSimple getSingleEventSimpleFromList(Integer pointer) {
        return createSingleEventSimple(listEventRepository.getEventsDB().get(pointer));
    }

    public EventSimple createSingleEventSimple(EventNew eventNew) {
        EventSimple eventSimple = new EventSimple();
        eventSimple.setEventSimpleId(eventNew.getId());
        eventSimple.setEventSimpleName(eventNew.getName());
        eventSimple.setEventSimpleDescription(eventNew.getDescLong());

        String[] date = eventNew.getStartDate().split("T");
        eventSimple.setEventSimpleDate(date[0]);

        String time = date[1].substring(0, 8);
        eventSimple.setEventSimpleStartTime(time);
        eventSimple.setEventSimpleEndTime(time);
        eventSimple.setEventSimplePlace(eventNew.getPlace().getName());
        eventSimple.setEventSimpleTicketPrice(0.0);
        eventSimple.setEventSimpleWebPageAddress(eventNew.getUrls().getWww());
        eventSimple.setEventSimpleDescriptionShort(eventNew.getDescShort());
        eventSimple.setEventSimplePicture(eventNew.getPlace().getSubname());
        return eventSimple;
    }

    public void clearMemory() {
        eventSimpleMemory.clear();
    }

    public List<EventSimple> getPartialListOfEventSimple(Integer numberOfEventsOnThePage) {
        List<EventSimple> listOfEventSimple = new ArrayList<>();

        for (int i = 0; i < numberOfEventsOnThePage; i++) {
            listOfEventSimple.add(eventSimpleMemory.getListOfEventSimple().get(i));
        }
        return listOfEventSimple;
    }

                        public List<EventSimple> getListOfEventSimple() {
                            List<EventSimple> listOfEventSimple = new ArrayList<>();

                            for (int i = 0; i < listEventRepository.getEventsDB().size(); i++) {
                                listOfEventSimple.add(getSingleEventSimpleFromList(i));
                            }
                            return listOfEventSimple;
                        }




    public List<EventSimple> getListOfEventSimpleFromMemory() {
        return eventSimpleMemory.getListOfEventSimple();
    }
}
