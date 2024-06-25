package br.com.empresa.votacao.service;

import br.com.empresa.votacao.domain.dto.SessaoDTO;
import br.com.empresa.votacao.domain.entity.Sessao;

public interface SessaoService {
	
	Sessao create(SessaoDTO sessaoDTO);
	
}
