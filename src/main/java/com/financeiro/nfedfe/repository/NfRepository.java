package com.financeiro.nfedfe.repository;

import com.financeiro.nfedfe.entity.NotasFiscais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NfRepository extends JpaRepository<NotasFiscais, Long> {
}
