package com.wellmind.dto.empresa;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmpresaDTO {

    @NotBlank(message = "{validation.nome.not.blank}")
    @Size(min = 3, max = 200, message = "{validation.nome.size}")
    private String nomeEmpresa;

    @NotBlank(message = "{validation.cnpj.not.blank}")
    @Pattern(regexp = "^\\d{14}$", message = "{validation.cnpj.pattern}")
    private String cnpj;

    @Size(max = 300, message = "{validation.endereco.size}")
    private String endereco;

    @Size(max = 20, message = "{validation.telefone.size}")
    private String telefone;

    @Email(message = "{validation.email.invalid}")
    @Size(max = 100, message = "{validation.email.size}")
    private String emailContato;
}
