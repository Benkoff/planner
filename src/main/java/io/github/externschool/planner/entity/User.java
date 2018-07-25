package io.github.externschool.planner.entity;

import io.github.externschool.planner.entity.schedule.ScheduleEvent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    private Long version;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<ScheduleEvent> ownEvents = new HashSet<>();

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<ScheduleEvent> relatedEvents = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_id", unique = true)
    private VerificationKey verificationKey;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public VerificationKey getVerificationKey() {
        return verificationKey;
    }

    public void addVerificationKey(VerificationKey verificationKey) {
        this.verificationKey = verificationKey;
        if (verificationKey != null) {
            verificationKey.setUser(this);
        }
    }

    public void removeVerificationKey() {
        if (verificationKey != null) {
            verificationKey.setUser(null);
        }
        this.verificationKey = null;
    }

    public void addOwnEvent(ScheduleEvent event) {
        this.ownEvents.add(event);
        event.setOwner(this);
    }

    public void removeOwnEvent(ScheduleEvent event) {
        this.ownEvents.remove(event);
        event.setOwner(null);
    }

    public void addRelatedEvent(ScheduleEvent event) {
        this.relatedEvents.add(event);
        event.getParticipants().add(this);
    }

    public void removeRelatedEvent(ScheduleEvent event) {
        this.relatedEvents.remove(event);
        event.getParticipants().remove(this);
    }

}
