package com.essalud.sispoi.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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
public class StrategicAction {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStrategicAction;

    @Column(length = 2, nullable = false)
    private String code;

    @Column(length = 500, nullable = false)
    private String name;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @OneToOne(mappedBy = "strategicAction")
    private StrategicActionFile strategicActionFile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_strategic_objective", nullable = false, foreignKey = @ForeignKey(name = "FK_STRATEGIC_ACTION_STRATEGIC_OBJECTIVE"))
    private StrategicObjective strategicObjective;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }

}
