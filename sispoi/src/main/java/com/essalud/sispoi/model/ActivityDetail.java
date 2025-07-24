package com.essalud.sispoi.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(length = 250)
    private String measurementUnit;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_formulation_type", foreignKey = @ForeignKey(name = "FK_FORMULATION_FORMULATION_TYPE"))
    private FormulationType formulationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_activity_family", foreignKey = @ForeignKey(name = "FK_ACTIVITY_DETAIL_ACTIVITY_FAMILY"))
    private ActivityFamily activityFamily;

    @OneToMany(mappedBy = "activityDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals;

    @OneToMany(mappedBy = "activityDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyGoal> monthlyGoals;

    @Min(2000)
    @Max(2200)
    @Column(nullable = false)
    private Integer year;

}
