package com.financeiro.nfedfe.repository;

import com.financeiro.nfedfe.entity.NotaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaEntradaRepository extends JpaRepository<NotaEntrada, Long> {
}
