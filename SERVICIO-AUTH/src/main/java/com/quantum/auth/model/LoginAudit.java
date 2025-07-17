package com.quantum.auth.model;

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
public class LoginAudit {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLoginAudit;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = true, foreignKey = @ForeignKey(name = "FK_ACCESS_AUDIT_USER"))
    private User user;

    @Column(length = 70, nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean success;

    @Column(nullable = false)
    private LocalDateTime accessDateTime;

    @PrePersist
    protected void onCreate() {
        this.accessDateTime = LocalDateTime.now();
    }

    @Column(length = 45)
    private String ip;

}