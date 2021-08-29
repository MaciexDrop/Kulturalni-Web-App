package com.infoshareacademy.kulturalniweb.repository;

import com.infoshareacademy.kulturalniweb.dto.EventDto;
import com.infoshareacademy.kulturalniweb.entities.event.*;
import com.infoshareacademy.kulturalniweb.mappers.EventMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class EventsRepository implements Dao<EventEntity> {

    protected EntityManager entityManager;
    private EventMapper eventMapper;

    public EventsRepository(EntityManager entityManager, EventMapper eventMapper) {
        this.entityManager = entityManager;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventEntity find(Integer id) {
        final Query query = entityManager.createQuery("SELECT e FROM EventEntity e WHERE e.id = :id");
        List<EventEntity> list = query.getResultList();

        return list.get(0);
    }

    @Override
    public Collection<EventEntity> findAll() {
        final Query query = entityManager.createQuery("SELECT e FROM EventEntity e", EventEntity.class);
        return query.getResultList();
    }

    @Override
    public void save(EventEntity eventEntity) {
        entityManager.persist(eventEntity);
        TicketEntity ticketEntity = eventEntity.getTicketEntity();
        entityManager.persist(ticketEntity);
        OrganizerEntity organizerEntity = eventEntity.getOrganizerEntity();
        entityManager.persist(organizerEntity);
        UrlEntity urlEntity = eventEntity.getUrlEntity();
        entityManager.persist(urlEntity);
        AttachmentEntity attachmentEntity = eventEntity.getAttachmentEntity();
        entityManager.persist(attachmentEntity);
        PlaceEntity placeEntity = eventEntity.getPlaceEntity();
        entityManager.persist(placeEntity);

    }

    // Czy można zrobić od razu update całego obiektu czy tez trzeba podac po kolei wszystkie kolumny?
    @Override
    public EventEntity update(EventEntity eventEntity, Integer id) {
        return null;
    }

    @Override
    public void delete(EventEntity eventEntity) {
    }

    public List<EventDto> createListOfClosestEvents() {
        final Query query = entityManager.createQuery("SELECT e FROM EventEntity e ORDER BY e.startDateTime DESC, e.startDateDate DESC", EventEntity.class);
        List<EventEntity> queryResult = query.getResultList();
        List<EventDto> eventDtos = new ArrayList<>();
        for(int i = 0; i < queryResult.size(); i++) {
            eventDtos.add(eventMapper.mapEventEntityToEventDto(queryResult.get(i)));
        }
        return eventDtos;
    }

    public List<EventDto> createListOfNewestEvents() {
        final Query query = entityManager.createQuery("SELECT e FROM EventEntity e ORDER BY e.id DESC", EventEntity.class);
        List<EventEntity> queryResult = query.getResultList();
        List<EventDto> eventDtos = new ArrayList<>();
        for(int i = 0; i < queryResult.size(); i++) {
            eventDtos.add(eventMapper.mapEventEntityToEventDto(queryResult.get(i)));
        }
        return eventDtos;
    }

    public EventEntity getSingleEvent(Integer id) {
        final Query query = entityManager.createQuery("SELECT e FROM EventEntity e WHERE e.id = :id", EventEntity.class);
        query.setParameter("id", id);
        List<EventEntity> eventEntities = query.getResultList();

        return eventEntities.get(0);
    }

    //@Modifying(flushAutomatically = true)
    public List<EventEntity> createListOfSortedEventEntities(Map<String, String> sortingParameters) {
        String orderDefinition = "";

        if(sortingParameters.get("eventSortType").equals("startDateDate")) {
            orderDefinition = "e.startDateDate " + sortingParameters.get("eventSortDirection") + ", e.startDateTime " + sortingParameters.get("eventSortDirection");
        } else if(sortingParameters.get("eventSortType").equals("city")) {
            orderDefinition = "e.city " + sortingParameters.get("eventSortDirection");
        } else {
            orderDefinition = "e.name " + sortingParameters.get("eventSortDirection");
        }

        Integer requestedPageNumber = Integer.parseInt(sortingParameters.get("requestedPageNumber"));
        Integer numberOfEventsOnThePage = Integer.parseInt(sortingParameters.get("numberOfEventsOnThePage"));
        Integer firstResult = (requestedPageNumber * numberOfEventsOnThePage) - (numberOfEventsOnThePage - 1);

        final Query query = entityManager
                .createQuery("SELECT e FROM EventEntity e " /*+
                        "WHERE e.categoryId " + sortingParameters.get("eventFilterType") + " " +
                        "ORDER BY " + orderDefinition, EventEntity.class*/)
                .setFirstResult(firstResult)
                .setMaxResults(numberOfEventsOnThePage);

        List<EventEntity> eventEntities = query.getResultList();
        return eventEntities;
    }

    public List<EventEntity> getSizeOfListOfSortedEventEntities(Map<String, String> sortingParameters) {
        String orderDefinition = "";

        if(sortingParameters.get("eventSortType").equals("startDateDate")) {
            orderDefinition = "e.startDateDate " + sortingParameters.get("eventSortDirection") + ", e.startDateTime " + sortingParameters.get("eventSortDirection");
        } else if(sortingParameters.get("eventSortType").equals("city")) {
            orderDefinition = "e.city " + sortingParameters.get("eventSortDirection");
        } else {
            orderDefinition = "e.name " + sortingParameters.get("eventSortDirection");
        }

        final Query query = entityManager
                .createQuery("SELECT e FROM EventEntity e " +
                        "WHERE e.categoryId " + sortingParameters.get("eventFilterType") + " " +
                        "ORDER BY " + orderDefinition, EventEntity.class);

        List<EventEntity> eventEntities = query.getResultList();
        return eventEntities;
    }

/*    public Integer getSizeOfDB() {
        final Query query = entityManager
                .createQuery("SELECT COUNT(distinct e.id) FROM EventEntity e");

        int result = query.getFirstResult();
        System.out.println("result A: " + result);

        return (Integer) query.getFirstResult();
    }*/


    public void updateEvent(EventEntity eventEntity) {
        final Query query = entityManager
                .createQuery("UPDATE EventEntity e SET " +
                        "e.placeEntity = :placeEntity, " +
                        "e.endDateDate = :endDateDate," +
                        "e.endDateTime = :endDateTime," +
                        "e.endDateLastTime = :endDateLastTime," +
                        "e.name = :name," +
                        "e.urlEntity = :urlEntity," +
                        "e.attachmentEntity = :attachmentEntity," +
                        "e.descLong = :descLong," +
                        "e.categoryId = :categoryId," +
                        "e.startDateDate =: startDateDate," +
                        "e.startDateTime =: startDateTime," +
                        "e.startDateLastTime =: startDateLastTime," +
                        "e.organizerEntity =: organizerEntity," +
                        "e.active = :active," +
                        "e.descShort = :descShort," +
                        "e.ticketEntity = :ticketEntity," +
                        "e.picture = :picture," +
                        "e.city = :city," +
                        "e.isFavourite = :isFavourite  WHERE e.id = :id")
                .setParameter("placeEntity", eventEntity.getPlaceEntity())
                .setParameter("endDateDate", eventEntity.getEndDateDate())
                .setParameter("endDateTime", eventEntity.getEndDateTime())
                .setParameter("endDateLastTime", eventEntity.getEndDateLastTime())
                .setParameter("name", eventEntity.getName())
                .setParameter("urlEntity", eventEntity.getUrlEntity())
                .setParameter("attachmentEntity", eventEntity.getAttachmentEntity())
                .setParameter("descLong", eventEntity.getDescLong())
                .setParameter("categoryId", eventEntity.getCategoryId())
                .setParameter("startDateDate", eventEntity.getStartDateDate())
                .setParameter("startDateTime", eventEntity.getStartDateTime())
                .setParameter("startDateLastTime", eventEntity.getStartDateLastTime())
                .setParameter("organizerEntity", eventEntity.getOrganizerEntity())
                .setParameter("active", eventEntity.getActive())
                .setParameter("descShort", eventEntity.getDescShort())
                .setParameter("ticketEntity", eventEntity.getTicketEntity())
                .setParameter("picture", eventEntity.getPicture())
                .setParameter("city", eventEntity.getCity())
                .setParameter("isFavourite", eventEntity.getFavourite())
                .setParameter("id", eventEntity.getId());

        int result = query.executeUpdate();
        System.out.println(result);
    }



    public void updateFavourite(Integer id, Boolean favStatus) {
        final Query query = entityManager
                .createQuery("UPDATE EventEntity e SET e.isFavourite = :newStatus WHERE e.id = :id")
                .setParameter("id", id)
                .setParameter("newStatus", !favStatus);

        int result = query.executeUpdate();
        System.out.println(result);
    }

    public Integer getMaximumId() {
        final Query query = entityManager
                .createQuery("SELECT MAX(id) FROM EventEntity");

        List<Integer> result = query.getResultList();
        Integer maximumId = result.get(0);

        return maximumId;
    }
}

