package de.rewe.poll.service;

import javax.persistence.*;
import java.util.UUID;

@Entity @Table(name = "poll_entries")
public class PollEntry {

    @Id @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "up_votes")
    private int upVotes;

    @PrePersist
    private void assignPrimaryKey(){
        if(id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
