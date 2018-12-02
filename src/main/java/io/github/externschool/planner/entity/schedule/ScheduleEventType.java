package io.github.externschool.planner.entity.schedule;

import io.github.externschool.planner.entity.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Danil Kuznetsov (kuznetsov.danil.v@gmail.com)
 */
@Entity
@Table(name = "schedule_event_type")
public class ScheduleEventType {

    //TODO need to extract to base entity class and change strategy to sequence
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String name;

    @Column(name = "count_participant")
    private Integer countOfParticipant;

    @ManyToMany
    @JoinTable(
            name = "event_type_owner_role",
            joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name")})
    private Set<Role> owners = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "event_type_participant_role",
            joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name")})
    private Set<Role> participants = new HashSet<>();

    public ScheduleEventType() {}

    public ScheduleEventType(final String name, final Integer countOfParticipant) {
        this.name = name;
        this.countOfParticipant = countOfParticipant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCountOfParticipant() {
        return countOfParticipant;
    }

    public void setCountOfParticipant(Integer countOfParticipant) {
        this.countOfParticipant = countOfParticipant;
    }

    public Set<Role> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    public Set<Role> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public Set<Role> addOwner(Role owner) {
        owners.add(owner);

        return owners;
    }

    public Set<Role> addParticipant(Role participant) {
        participants.add(participant);

        return participants;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleEventType)) return false;
        final ScheduleEventType that = (ScheduleEventType) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCountOfParticipant(), that.getCountOfParticipant());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getCountOfParticipant());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScheduleEventType{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", countOfParticipant=").append(countOfParticipant);
        sb.append(", owners=").append(getOwners().size());
        sb.append(", participants=").append(getParticipants().size());
        sb.append('}');
        return sb.toString();
    }
}
