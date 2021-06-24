package com.infoshareacademy.kulturalniweb.controllers;

import com.infoshareacademy.kulturalniweb.domainData.EventNew;
import com.infoshareacademy.kulturalniweb.models.NewEventDto;
import com.infoshareacademy.kulturalniweb.services.RepositoryServiceClass;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AddEventController {
    RepositoryServiceClass repositoryServiceClass;

    public AddEventController(RepositoryServiceClass repositoryServiceClass) {
        this.repositoryServiceClass = repositoryServiceClass;
    }

    @GetMapping("/addevent")
    public String addEvent(Model model) {
        NewEventDto newEventDto = new NewEventDto();

        model.addAttribute("newEventDto", newEventDto);

        return "addeventform";
    }





    @PostMapping(value = "/saveevent")
    public String addEvent(@ModelAttribute @Valid NewEventDto newEventDto, BindingResult result, Model model) {
        model.addAttribute("newEventDto", newEventDto);

        if (result.hasFieldErrors()) {
            return "addeventform";
        } else {
            EventNew eventNew = repositoryServiceClass.createEventNewFromNewEventDto(newEventDto);
            repositoryServiceClass.saveEventNew(eventNew);
            return "eventsaved";
        }
    }




}
