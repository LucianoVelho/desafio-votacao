package br.com.empresa.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.empresa.votacao.domain.entity.Sessao;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

}
