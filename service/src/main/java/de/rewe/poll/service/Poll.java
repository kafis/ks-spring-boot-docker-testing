package de.rewe.poll.service;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name = "polls")
public class Poll {

    @Id @Column(name = "id")
    private String id;

    @Column(name = "topic")
    private String topic;

    @OneToMany
    @JoinColumn(name= "fk_poll_id", referencedColumnName = "id")
    private Set<PollEntry> entries = Collections.EMPTY_SET;

    public Set<PollEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<PollEntry> entries) {
        this.entries = entries;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PrePersist
    private void assignPrimaryKey(){
        if(id == null) {
            id = UUID.randomUUID().toString();
        }
    }

}
