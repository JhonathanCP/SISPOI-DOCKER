package com.essalud.sispoi.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ActivityDetail {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idActivityDetail;

    @Column(length = 500, nullable = false)
    private String name;

    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "id_dependency_type", nullable = false, foreignKey = @ForeignKey(name = "FK_ACTIVITY_DETAIL_DEPENDENCY_TYPE"))
    private DependencyType dependencyType;

    private Boolean head;

    @ManyToOne
    @JoinColumn(name = "id_strategic_action", nullable = false, foreignKey = @ForeignKey(name = "FK_ACTIVITY_DETAIL_STRATEGIC_ACTION"))
    private StrategicAction strategicAction;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }

}
