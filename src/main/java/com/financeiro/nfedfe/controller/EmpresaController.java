package com.financeiro.nfedfe.controller;

import com.financeiro.nfedfe.entity.Empresa;
import com.financeiro.nfedfe.service.EmpresaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empresa")
@Slf4j
public class EmpresaController {
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {

        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<?> salvarEmpresa(@RequestBody Empresa empresa){
        try{
            empresaService.salvarEmpresa(empresa);
            return ResponseEntity.ok(empresa);
        }
        catch (Exception e){
            log.info("Erro ao salvar empresa: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarEmpresaById(@PathVariable("id") Long idEmpresa){
        try{
            empresaService.deletarEmpresaById(idEmpresa);
            return ResponseEntity.ok("Empresa deletada com sucesso.");
        }
        catch (Exception e){
            log.info("Erro ao deletar empresa: ",e);
            return ResponseEntity.badRequest().body("Erro ao deletar empresa: "+e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarEmpresas(){
        try{
            return ResponseEntity.ok(empresaService.listarEmpresas());
        }
        catch (Exception e){
            log.info("Erro ao listar empresas: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> listarEmpresaById(@PathVariable("id") Long idEmpresa){
        try{
            return ResponseEntity.ok(empresaService.listarEmpresaById(idEmpresa));
        }
        catch (Exception e){
            log.info("Erro ao listar empresa: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
