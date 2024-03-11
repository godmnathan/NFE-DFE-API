package com.financeiro.nfedfe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "IL_NOTA_NACIONAL")
@SequenceGenerator(name = "SeqNotaEntrada", sequenceName = "SEQ_NOTA_ENTRADA", allocationSize = 1)
@Data
public class NotasFiscais {

    @Id
    @GeneratedValue(generator = "SeqNotaEntrada", strategy = GenerationType.SEQUENCE)
    private long id;
    private String schema;
    private String chave;
    private String nomeEmitente;
    private String cnpjEmitente;
    private BigDecimal valor;
    @Lob
    private byte[] xml;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
