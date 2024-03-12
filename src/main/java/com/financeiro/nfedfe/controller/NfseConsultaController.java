package com.financeiro.nfedfe.controller;

import com.financeiro.nfedfe.service.NfseConsultaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NfseConsultaController {

    private final NfseConsultaService nfseConsultaService;

    public NfseConsultaController(NfseConsultaService nfseConsultaService) {
        this.nfseConsultaService = nfseConsultaService;
    }

    @GetMapping("/consultarNfse/{nsu}")
    public String consultarNfse(@PathVariable String nsu) {
        return nfseConsultaService.consultarNfsePorNsu(nsu);
    }
}