package br.com.empresa.votacao.service;

import java.util.List;

import br.com.empresa.votacao.domain.dto.AssociadoDTO;
import br.com.empresa.votacao.domain.dto.SessaoDTO;
import br.com.empresa.votacao.domain.entity.Sessao;

public interface SessaoService {
	
	SessaoDTO create(SessaoDTO sessaoDTO);
	
	List<SessaoDTO> buscarTodosSessoes();
	
	SessaoDTO buscaSessaoPorID(Long id);
	
	void deletaSessao(Long id);

	boolean isSessaoAberta(Long id);
	 
	
}
