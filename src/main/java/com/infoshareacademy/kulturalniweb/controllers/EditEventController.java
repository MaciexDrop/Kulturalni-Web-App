package com.infoshareacademy.kulturalniweb.controllers;

import com.infoshareacademy.kulturalniweb.dto.EventDto;

import com.infoshareacademy.kulturalniweb.jsonData.EventNew;
import com.infoshareacademy.kulturalniweb.mappers.EventMapper;
import com.infoshareacademy.kulturalniweb.models.EditEventDto;
import com.infoshareacademy.kulturalniweb.models.NewEventDto;
import com.infoshareacademy.kulturalniweb.services.EditEventService;
import com.infoshareacademy.kulturalniweb.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class EditEventController {

    EventService eventService;
    EventMapper eventMapper;
    EditEventService editEventService;

    public EditEventController(EventService eventService, EventMapper eventMapper, EditEventService editEventService) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.editEventService = editEventService;
    }

    @GetMapping("/editEvent")
    public String editEvent(Model model) {
        Integer id = 140469;
        EventDto eventDto = editEventService.getSingleEvent(id);

        EditEventDto editEventDto = eventMapper.mapEventDtoToEditEventDto(eventDto);

        model.addAttribute("editEventDto", editEventDto);

        return "editEventForm";
    }

    @PostMapping("/updateEvent")
    public String updateEvent(@ModelAttribute @Valid EditEventDto editEventDto, BindingResult result, Model model) {

        editEventService.saveEditedEvent(editEventDto);
        System.out.println(editEventDto.getNewEventId() + "  id");
        EventDto eventDto = editEventService.getSingleEvent(editEventDto.getNewEventId());

        model.addAttribute("eventDto", eventDto);

        return "editEventSavedForm";

/*        if (result.hasFieldErrors()) {
            model.addAttribute("editEventDto", editEventDto);
            return "editEventForm";
        } else {
            editEventService.saveEditedEvent(editEventDto);
            EventDto eventDto = editEventService.getSingleEvent(editEventDto.getId());
            model.addAttribute("eventDto", eventDto);

            return "editEventSavedForm";
        }*/
    }
}
