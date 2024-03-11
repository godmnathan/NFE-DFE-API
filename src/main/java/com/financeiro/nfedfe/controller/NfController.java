package com.financeiro.nfedfe.controller;

import com.financeiro.nfedfe.entity.NotasFiscais;
import com.financeiro.nfedfe.service.DistNfService;
import com.financeiro.nfedfe.service.NfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Nf")
@Slf4j
public class NfController {
    private final NfService nfService;
    private final DistNfService distNfService;

    public NfController(NfService nfService, DistNfService distNfService) {

        this.nfService = nfService;
        this.distNfService = distNfService;
    }

    @PostMapping
    public ResponseEntity<?> salvarNotaEntrada(@RequestBody NotasFiscais notasFiscais){
        try{
            nfService.salvarNf((List<NotasFiscais>) notasFiscais);
            return ResponseEntity.ok(notasFiscais);
        }
        catch (Exception e){
            log.info("Erro ao salvar Nota Entrada: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "consulta")
    public ResponseEntity<?> consultaNotasDist(){
        try{
            distNfService.consultaNotasDist();
            return ResponseEntity.ok(listarNotaEntradas());
        } catch (Exception e) {
            log.error("Erro ao listar as notas");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarNotaEntradas(){
        try{
            return ResponseEntity.ok(nfService.listarNf());
        }
        catch (Exception e){
            log.info("Erro ao listar notaEntradas: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> listarNotaEntradaById(@PathVariable("id") Long idNota){
        try{
            return ResponseEntity.ok(nfService.listarNfById(idNota));
        }
        catch (Exception e){
            log.info("Erro ao listar notaEntrada: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
