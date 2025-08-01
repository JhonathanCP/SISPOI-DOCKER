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
public class Formulation {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFormulation;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dependency", nullable = false, foreignKey = @ForeignKey(name = "FK_FORMULATION_DEPENDENCY"))
    private Dependency dependency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_formulation_state", nullable = false, foreignKey = @ForeignKey(name = "FK_FORMULATION_FORMULATION_STATE"))
    private FormulationState formulationState;

    @OneToOne(mappedBy = "formulation")
    private FormulationSupportFile formulationSupportFile;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }

    @Min(2000)
    @Max(2100)
    @Column(nullable = false)
    private Integer year;

    @Min(1)
    @Max(8)
    private Integer modification;

    @Min(1)
    @Max(5)
    private Integer quarter;

    @Min(1)
    @Max(12)
    private Integer month;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_formulation_type", foreignKey = @ForeignKey(name = "FK_FORMULATION_FORMULATION_TYPE"))
    private FormulationType formulationType;

    private Double budget;

}
