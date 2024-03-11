package com.financeiro.nfedfe.service;

import com.financeiro.nfedfe.entity.NotasFiscais;
import com.financeiro.nfedfe.exception.SistemaException;
import com.financeiro.nfedfe.repository.NfRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NfService {

    private final NfRepository repository;


    public NfService(NfRepository repository){

        this.repository = repository;

    }
    public void salvarNf(List<NotasFiscais> notasFiscais){
         repository.saveAll(notasFiscais);
    }

    public List<NotasFiscais> listarNf(){

        return repository.findAll();
    }
    public NotasFiscais listarNfById(Long idNf){

        return repository.findById(idNf)
                .orElseThrow(() -> new SistemaException("Nota não encontrada com ID: " + idNf));
    }

}
