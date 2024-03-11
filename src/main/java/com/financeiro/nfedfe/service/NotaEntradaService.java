package com.financeiro.nfedfe.service;

import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.exception.NfeException;
import com.financeiro.nfedfe.entity.Empresa;
import com.financeiro.nfedfe.entity.NotaEntrada;
import com.financeiro.nfedfe.exception.SistemaException;
import com.financeiro.nfedfe.repository.NotaEntradaRepository;
import lombok.extern.slf4j.Slf4j;
import com.financeiro.nfedfe.service.DistribuicaoService;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class NotaEntradaService {

    private final NotaEntradaRepository repository;


    public NotaEntradaService(NotaEntradaRepository repository){

        this.repository = repository;

    }
    public void salvarNotaEntrada(List<NotaEntrada> notaEntrada){
         repository.saveAll(notaEntrada);
    }

    public List<NotaEntrada> listarNotas(){

        return repository.findAll();
    }
    public NotaEntrada listarNotaById(Long idNota){

        return repository.findById(idNota)
                .orElseThrow(() -> new SistemaException("Nota não encontrada com ID: " + idNota));
    }

}
