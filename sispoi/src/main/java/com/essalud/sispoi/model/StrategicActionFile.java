package com.essalud.sispoi.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class StrategicActionFile {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStrategicActionFile;

    @Column(length = 500, nullable = false)
    private String name;
    
    @Column(length = 15, nullable = false)
    private String fileExtension;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }

    @Column
    private byte[] file;

    // Relación bidireccional con StrategicAction
    @OneToOne
    @JoinColumn(name = "id_strategic_action", referencedColumnName = "idStrategicAction", nullable = false)
    private StrategicAction strategicAction;

}
