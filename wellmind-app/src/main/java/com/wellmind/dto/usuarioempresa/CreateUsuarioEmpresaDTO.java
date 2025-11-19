package com.wellmind.dto.usuarioempresa;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUsuarioEmpresaDTO {

    @NotNull(message = "{validation.usuario.not.null}")
    private Long idUsuario;

    @NotNull(message = "{validation.empresa.not.null}")
    private Long idEmpresa;

    @NotBlank(message = "{validation.cargo.not.blank}")
    @Size(min = 2, max = 100, message = "{validation.cargo.size}")
    private String cargo;

    @NotNull(message = "{validation.datavinculo.not.null}")
    @PastOrPresent(message = "{validation.datavinculo.past}")
    private LocalDate dataVinculo;
}
