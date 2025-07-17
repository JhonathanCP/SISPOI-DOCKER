package com.quantum.auth.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "user_data")
public class User {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(length = 50, nullable = false, unique = true)
    private String username;
    
    @Column(length = 60, nullable = false)
    private String password;

    @Column(length = 200, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean ldap;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", 
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "idUser"),
        inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "idRole"))
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_dependency", 
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "idUser"),
        inverseJoinColumns = @JoinColumn(name = "id_dependency", referencedColumnName = "idDependency"))
    private List<Dependency> dependencies;

}
