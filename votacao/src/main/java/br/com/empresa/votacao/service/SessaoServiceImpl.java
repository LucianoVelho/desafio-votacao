package br.com.empresa.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.empresa.votacao.domain.dto.SessaoDTO;
import br.com.empresa.votacao.domain.entity.Sessao;
import br.com.empresa.votacao.repository.SessaoRepository;
import jakarta.transaction.Transactional;

@Service
public class SessaoServiceImpl extends AbstractService implements SessaoService {
	
	@Autowired
	private SessaoRepository sessaoRepository;
	
	@Transactional
	@Override
	public Sessao create(SessaoDTO sessaoDTO) {
		return sessaoRepository.save(criaSessao(sessaoDTO));
	}

	public Sessao criaSessao(SessaoDTO dto) {
		Sessao sessao = modelMapper.map(dto, Sessao.class);
		return sessao;
	}
	

}
