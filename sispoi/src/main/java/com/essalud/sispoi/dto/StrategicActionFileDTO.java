package com.essalud.sispoi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class StrategicActionFileDTO {

    @EqualsAndHashCode.Include
    private Integer idStrategicActionFile;

    @NotNull
    @Size(max = 500)
    private String name;

    @Size(max = 15)
    private String fileExtension;

    private Boolean active = true;

    private LocalDateTime createTime;

    private byte[] file;

}
