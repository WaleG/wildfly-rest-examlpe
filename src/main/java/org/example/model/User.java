package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.config.adapter.LocalDateAdapter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;

@Entity()
@EqualsAndHashCode()
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"firstname", "surname", "dateOfBirth"}))
@Getter
public class User implements Serializable {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long userId;

    @Version
    @JsonIgnore
    private Integer version;

    @Size(max = 35)
    @Column(nullable = false)
    @Setter
    private String firstname;

    @Size(max = 35)
    @Column(nullable = false)
    @Setter
    private String surname;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @Column(nullable = false)
    @Setter
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private UserStatus status = UserStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    @Setter
    @JsonBackReference
    private Project project;

    @Column(insertable=false, updatable=false)
    private Long projectId;

    public User() {
    }
}