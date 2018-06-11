package io.github.externschool.planner.entity.schedule;

import io.github.externschool.planner.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Danil Kuznetsov (kuznetsov.danil.v@gmail.com)
 */
@Entity
@Table(name = "schedule_event")
public class ScheduleEvent {

    //TODO need to extract to base entity class and change strategy to sequence
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String description;

    @Column
    private String location;

    @Column(name = "start_event", nullable = false)
    private LocalDateTime startOfEvent;

    @Column(name = "end_event", nullable = false)
    private LocalDateTime endOfEvent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "is_open")
    private boolean isOpen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_id")
    private ScheduleEventType type;

    @ManyToMany
    @JoinTable(
            name = "event_participant",
            joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> participants = new HashSet<>();

    public ScheduleEvent() {}

    private ScheduleEvent(final Long id,
                          final String title,
                          final String description,
                          final String location,
                          final User owner,
                          final ScheduleEventType type,
                          final Set<User> participants,
                          final LocalDateTime startOfEvent,
                          final LocalDateTime endOfEvent,
                          final LocalDateTime createdAt,
                          final LocalDateTime modifiedAt,
                          final boolean isOpen) {


        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.owner = owner;
        this.type = type;
        this.participants = participants;
        this.startOfEvent = startOfEvent;
        this.endOfEvent = endOfEvent;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isOpen = isOpen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ScheduleEventType getType() {
        return type;
    }

    public void setType(ScheduleEventType type) {
        this.type = type;
    }

    public Set<User> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public void setParticipants(Set<User> participants) {
        this.participants = new HashSet<>(participants);
    }

    public LocalDateTime getStartOfEvent() {
        return startOfEvent;
    }

    public void setStartOfEvent(LocalDateTime startOfEvent) {
        this.startOfEvent = startOfEvent;
    }

    public LocalDateTime getEndOfEvent() {
        return endOfEvent;
    }

    public void setEndOfEvent(LocalDateTime endOfEvent) {
        this.endOfEvent = endOfEvent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleEvent event = (ScheduleEvent) o;

        return id != null ? id.equals(event.id) : event.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ScheduleEvent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startOfEvent=" + startOfEvent +
                ", endOfEvent=" + endOfEvent +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", isOpen=" + isOpen +
                ", owner=" + owner +
                ", type=" + type +
                ", participants=" + participants +
                '}';
    }

    public static ScheduleEventBuilder builder() {
        return new ScheduleEventBuilder();
    }

    public static class ScheduleEventBuilder {
        private Long id;
        private String title;
        private String description;
        private String location;
        private User owner;
        private ScheduleEventType type;
        private Set<User> participants = new HashSet<>();
        private LocalDateTime startOfEvent;
        private LocalDateTime endOfEvent;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime modifiedAt;
        private boolean isOpen;

        private ScheduleEventBuilder() {}

        public ScheduleEventBuilder withId(final Long id) {
            this.id = id;
            return this;
        }

        public ScheduleEventBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public ScheduleEventBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public ScheduleEventBuilder withLocation(final String location) {
            this.location = location;
            return this;
        }

        public ScheduleEventBuilder withOwner(final User person) {
            this.owner = person;
            return this;
        }

        public ScheduleEventBuilder withType(final ScheduleEventType eventType) {
            this.type = eventType;
            return this;
        }

        public ScheduleEventBuilder withParticipants(final Set<User> participants) {
            this.participants = participants;
            return this;
        }

        public ScheduleEventBuilder withStartDateTime(final LocalDateTime dateTime) {
            this.startOfEvent = dateTime;
            return this;
        }

        public ScheduleEventBuilder withEndDateTime(final LocalDateTime dateTime) {
            this.endOfEvent = dateTime;
            return this;
        }

        public ScheduleEventBuilder withOpenStatus(final boolean status) {
            this.isOpen = status;
            return this;
        }

        public ScheduleEvent build() {
            return new ScheduleEvent(
                    this.id,
                    this.title,
                    this.description,
                    this.location,
                    this.owner,
                    this.type,
                    this.participants,
                    this.startOfEvent,
                    this.endOfEvent,
                    this.createdAt,
                    this.modifiedAt,
                    this.isOpen
            );
        }
    }
}
