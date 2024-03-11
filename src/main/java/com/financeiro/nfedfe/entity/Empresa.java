package com.financeiro.nfedfe.entity;

import br.com.swconsultoria.nfe.dom.enuns.AmbienteEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "IL_EMPRESA_NOTA_NACIONAL")
@SequenceGenerator(name = "SeqEmpresa", sequenceName = "SEQ_EMPRESA", allocationSize = 1)
@Data
public class Empresa {

    @Id
    @GeneratedValue(generator = "SeqEmpresa", strategy = GenerationType.SEQUENCE)
    private long id;
    private String cpfCnpj;
    private String razaoSocial;
    private String uf;
    @Enumerated(EnumType.STRING)
    private AmbienteEnum ambiente;
    @Lob
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] certificadoDigital;
    private String senhaCertificadoDigital;
    private String nsu;

}
