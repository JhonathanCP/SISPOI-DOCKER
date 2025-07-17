package com.quantum.auth.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class LoginAuditDTO {

    @EqualsAndHashCode.Include
    private Integer idLoginAudit;

    private UserDTO user;

    @NotNull
    @Size(max = 70)
    private String username;

    @NotNull
    private Boolean success;

    private LocalDateTime accessDateTime;

    private String ip;

}