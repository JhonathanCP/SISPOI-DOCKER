package com.essalud.sispoi.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StrategicObjective {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStrategicObjective;

    @Column(length = 2, nullable = false)
    private String code;

    @Column(length = 500, nullable = false)
    private String name;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @OneToOne(mappedBy = "strategicObjective")
    private StrategicObjectiveFile strategicObjectiveFile;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }

    @Min(2000)
    @Max(2200)
    @Column(nullable = false)
    private Integer startYear;

    @Min(2000)
    @Max(2200)
    @Column(nullable = false)
    private Integer endYear;

}
