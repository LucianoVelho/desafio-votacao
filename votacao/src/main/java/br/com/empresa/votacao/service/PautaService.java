package br.com.empresa.votacao.service;

import java.util.List;

import br.com.empresa.votacao.domain.dto.PautaDTO;

public interface PautaService {
	
	PautaDTO create(PautaDTO pautaDTO);
	
	List<PautaDTO> buscarTodasPautas();

	PautaDTO buscarPorId(Long id);
	
}
