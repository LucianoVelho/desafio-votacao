package br.com.empresa.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.empresa.votacao.domain.entity.Pauta;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
	
    @Query("SELECT COUNT(p) > 0 FROM Pauta p WHERE LOWER(p.nome) = LOWER(:nome)")
    boolean existsByNomeIgnoreCase(@Param("nome") String nome);
    
}
