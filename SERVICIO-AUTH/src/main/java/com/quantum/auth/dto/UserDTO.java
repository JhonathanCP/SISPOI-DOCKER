package com.quantum.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDTO {

    @EqualsAndHashCode.Include
    private Integer idUser;

    @NotNull
    @Size(max = 50)
    private String username;

    @NotNull
    @Email
    @Size(max = 250)
    private String email;

    @Size(max = 80)
    private String password;

    @NotNull
    private Boolean ldap = false;

    @NotNull
    private Boolean enabled = true;

    @NotNull
    private List<RoleDTO> roles;

    private List<DependencyDTO> dependencies;

}