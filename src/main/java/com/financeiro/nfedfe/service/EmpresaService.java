package com.financeiro.nfedfe.service;

import com.financeiro.nfedfe.entity.Empresa;
import com.financeiro.nfedfe.exception.SistemaException;
import com.financeiro.nfedfe.repository.EmpresaRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository){
        this.repository = repository;
    }
    public Empresa salvarEmpresa(Empresa empresa){
        validarEmpresa(empresa);
        log.info("Salvando a Empresa...");
        return repository.save(empresa);
    }

    public void deletarEmpresaById(Long idEmpresa){
        repository.deleteById(idEmpresa);
    }
    public List<Empresa> listarEmpresas(){
        return repository.findAll();
    }

    public Empresa listarEmpresaById(Long idEmpresa){

        return repository.findById(idEmpresa)
                .orElseThrow(() -> new SistemaException("Empresa não encontrada com ID: " + idEmpresa));
    }
    private void validarEmpresa(@NotNull Empresa empresa) {
        // TO-DO
        Optional.ofNullable(empresa.getCpfCnpj())
                .filter(cpfCnpj -> !cpfCnpj.isEmpty())
                .orElseThrow(() ->
                        new SistemaException("O campo CPF/CNPJ é obrigatório."));
        Optional.ofNullable(empresa.getCertificadoDigital())
                .filter(certificadoDigital -> certificadoDigital.length > 0)
                .orElseThrow(() ->
                        new SistemaException("O campo Certificado Digital é obrigatório."));
    }
}
