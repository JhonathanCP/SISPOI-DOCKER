package com.essalud.sispoi.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedMonthlyGoal {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExecutedMonthlyGoal;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "id_operational_activity", foreignKey = @ForeignKey(name = "FK_EXECUTED_META_OPERATIONAL_ACTIVITY"))
    private OperationalActivity operationalActivity;

    @Min(1)
    @Max(12)
    @Column(nullable = false)
    private Integer goalOrder;

    @Column(nullable = false)
    private Double value;

}
