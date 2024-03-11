package com.financeiro.nfedfe.controller;

import com.financeiro.nfedfe.entity.NotaEntrada;
import com.financeiro.nfedfe.service.DistribuicaoService;
import com.financeiro.nfedfe.service.NotaEntradaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notaEntrada")
@Slf4j
public class NotaEntradaController {
    private final NotaEntradaService notaEntradaService;
    private final DistribuicaoService distribuicaoService;

    public NotaEntradaController(NotaEntradaService notaEntradaService, DistribuicaoService distribuicaoService) {

        this.notaEntradaService = notaEntradaService;
        this.distribuicaoService = distribuicaoService;
    }

    @PostMapping
    public ResponseEntity<?> salvarNotaEntrada(@RequestBody NotaEntrada notaEntrada){
        try{
            notaEntradaService.salvarNotaEntrada((List<NotaEntrada>) notaEntrada);
            return ResponseEntity.ok(notaEntrada);
        }
        catch (Exception e){
            log.info("Erro ao salvar Nota Entrada: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "consulta")
    public ResponseEntity<?> consultaNotasDist(){
        try{
            distribuicaoService.consultaNotasDist();
            return ResponseEntity.ok(listarNotaEntradas());
        } catch (Exception e) {
            log.error("Erro ao listar as notas");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarNotaEntradas(){
        try{
            return ResponseEntity.ok(notaEntradaService.listarNotas());
        }
        catch (Exception e){
            log.info("Erro ao listar notaEntradas: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> listarNotaEntradaById(@PathVariable("id") Long idNota){
        try{
            return ResponseEntity.ok(notaEntradaService.listarNotaById(idNota));
        }
        catch (Exception e){
            log.info("Erro ao listar notaEntrada: ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
