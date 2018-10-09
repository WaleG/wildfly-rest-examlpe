package org.example.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.config.adapter.LocalDateTimeAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity()
@EqualsAndHashCode()
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Getter
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Setter
    private Long projectId;

    @Version
    @JsonIgnore
    private Integer version;

    @Size(max = 35)
    @Column(nullable = false)
    @NotNull
    @Setter
    private String name;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @Column(nullable = false)
    @Setter
    private LocalDateTime startDate;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @Column(nullable = true)
    @Setter
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "project",
            targetEntity = User.class,
            fetch = FetchType.EAGER)
    @Setter
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    public Project() {
    }

    public Project(String name, LocalDateTime startDate) {
        this.name = name;
        this.startDate = startDate;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.setProject(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.setProject(null);
    }

    @JsonIgnore
    public boolean isFinished() {
        return this.endDate != null;
    }
}