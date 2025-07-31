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
public class OperationalActivity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOperationalActivity;

    @Column(length = 16, nullable = false)
    private String sapCode;

    @Column(length = 3)
    private String correlativeCode;

    @Column(length = 1000, nullable = false)
    private String name;

    @Column(length = 5000, nullable = false)
    private String description;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "id_strategic_action", nullable = false, foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY_STRATEGIC_ACTION"))
    private StrategicAction strategicAction;

    @ManyToOne
    @JoinColumn(name = "id_formulation", nullable = false, foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY_FORMULATION"))
    private Formulation formulation;

    @ManyToOne
    @JoinColumn(name = "id_financial_fund", nullable = false, foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY_FINANCIAL_FUND"))
    private FinancialFund financialFund;

    @ManyToOne
    @JoinColumn(name = "id_management_center", nullable = false, foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY_MANAGEMENT_CENTER"))
    private ManagementCenter managementCenter;

    @ManyToOne
    @JoinColumn(name = "id_cost_center", nullable = false, foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY_COST_CENTER"))
    private CostCenter costCenter;

    @ManyToOne
    @JoinColumn(name = "id_measurement_type", foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY_MEASUREMENT_TYPE"))
    private MeasurementType measurementType;

    @Column(length = 250)
    private String measurementUnit;

    @OneToMany(mappedBy = "operationalActivity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals;

    @OneToMany(mappedBy = "operationalActivity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutedGoal> executedGoals;

    @OneToMany(mappedBy = "operationalActivity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyGoal> monthlyGoals;

    @OneToMany(mappedBy = "operationalActivity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutedMonthlyGoal> executedMonthlyGoals;

    @ManyToOne
    @JoinColumn(name = "id_priority", nullable = false, foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY_PRIORITY"))
    private Priority priority;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
    
    @Column(nullable = false)
    private Double goods;
    
    @Column(nullable = false)
    private Double remuneration;
    
    @Column(nullable = false)
    private Double services;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_activity_family", foreignKey = @ForeignKey(name = "FK_OPERATIONAL_ACTIVITY__ACTIVITY_FAMILY"))
    private ActivityFamily activityFamily;

}
