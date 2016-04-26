package de.rewe.poll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/polls", produces = MediaType.APPLICATION_JSON_VALUE)
public class PollResource {

    @Autowired
    private PollRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Poll> all() {
        return repository.entries();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Poll create(@RequestBody Poll poll) {
        repository.put(poll);
        return poll;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        repository.remove(id);
    }

}
