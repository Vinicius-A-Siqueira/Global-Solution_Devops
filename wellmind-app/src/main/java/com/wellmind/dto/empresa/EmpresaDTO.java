package com.wellmind.dto.empresa;

import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaDTO {

    private Long idEmpresa;
    private String nomeEmpresa;
    private String cnpj;
    private String cnpjFormatado;
    private String endereco;
    private String telefone;
    private String emailContato;
    private Boolean ativa;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimaAtualizacao;
    private Long totalColaboradores;
}
