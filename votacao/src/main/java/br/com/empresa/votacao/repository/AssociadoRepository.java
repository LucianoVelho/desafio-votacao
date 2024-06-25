package br.com.empresa.votacao.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.empresa.votacao.domain.entity.Associado;

public interface AssociadoRepository  extends JpaRepository<Associado, UUID> {
	Optional<Associado> findByCpf(String cpf);
}
