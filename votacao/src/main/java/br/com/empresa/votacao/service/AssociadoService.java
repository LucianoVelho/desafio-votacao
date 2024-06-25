package br.com.empresa.votacao.service;

import java.util.List;
import java.util.UUID;

import br.com.empresa.votacao.domain.dto.AssociadoDTO;
import br.com.empresa.votacao.domain.dto.VerificaCpfDTO;

public interface AssociadoService {
	
	AssociadoDTO create(AssociadoDTO associadoDTO);
	
	List<AssociadoDTO> buscarTodosAssociados();

	AssociadoDTO buscarPorId(UUID id);
	
	VerificaCpfDTO verificaCPF(String CPF);

}
