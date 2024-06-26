package br.com.empresa.votacao.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.empresa.votacao.constant.ErrorMessagesConstant;
import br.com.empresa.votacao.domain.dto.SessaoDTO;
import br.com.empresa.votacao.domain.entity.Pauta;
import br.com.empresa.votacao.domain.entity.Sessao;
import br.com.empresa.votacao.exceptions.exception.NotFoundException;
import br.com.empresa.votacao.repository.PautaRepository;
import br.com.empresa.votacao.repository.SessaoRepository;
import jakarta.transaction.Transactional;

@Service
public class SessaoServiceImpl extends AbstractService implements SessaoService {

	@Autowired
	private SessaoRepository sessaoRepository;

	@Autowired
	private PautaRepository pautaRepository;

	@Transactional
	@Override
	public SessaoDTO create(SessaoDTO sessaoDTO) {
		sessaoDTO = ajustarFimSeNecessario(sessaoDTO);
		Long pautaId = sessaoDTO.getPauta();
		Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(
				() -> new NotFoundException(ErrorMessagesConstant.PAUTA_NOT_FOUND + pautaId));
		Sessao sessao = builderSessao(pauta, sessaoDTO);
		return builderSessaoDTO(sessaoRepository.save(sessao));
	}

	public Sessao builderSessao(Pauta pauta, SessaoDTO sessaoDTO) {
		return Sessao.builder().pauta(pauta).inicio(sessaoDTO.getInicio()).fim(sessaoDTO.getFim()).build();

	}
	
	public SessaoDTO builderSessaoDTO( Sessao sessao) {
		return SessaoDTO.builder().id(sessao.getId()).pauta(sessao.getPauta().getId()).inicio(sessao.getInicio()).fim(sessao.getFim()).build();

	}

	public Sessao criaSessao(SessaoDTO dto) {
		Sessao sessao = modelMapper.map(dto, Sessao.class);
		return sessao;
	}

	@Override
	public List<SessaoDTO> buscarTodosSessoes() {
		List <SessaoDTO> dtos = new ArrayList<>();
		List<Sessao> sessoes = sessaoRepository.findAll();
		for(Sessao sessao : sessoes) {
			dtos.add(builderSessaoDTO(sessao));
		}
		return dtos;
	}

	@Override
	public void deletaSessao(Long id) {
		Sessao sessao = getSessao(id);
		sessaoRepository.deleteById(sessao.getId());
	}

	public SessaoDTO ajustarFimSeNecessario(SessaoDTO sessaoDTO) {
		if (sessaoDTO.getFim() == null) {
			sessaoDTO.setFim(sessaoDTO.getInicio().plusMinutes(1));
		}
		return sessaoDTO;

	}

	@Override
	public SessaoDTO buscaSessaoPorID(Long id) {
		Sessao sessao = getSessao(id);
		SessaoDTO dto = builderSessaoDTO(sessao);
		return dto;
	}

	public Sessao getSessao(Long id) {
		Optional<Sessao> optionalSessao = sessaoRepository.findById(id);
		if (optionalSessao.isEmpty()) {
			throw new NotFoundException(ErrorMessagesConstant.SESSAO_NOT_FOUND + id);
		}

		Sessao sessao = optionalSessao.get();
		return sessao;
	}
	
	@Override
	public boolean isSessaoAberta(Long id) {
		Sessao sessao = getSessao(id);
		LocalDateTime agora = LocalDateTime.now();
		return agora.isAfter(sessao.getInicio()) && agora.isBefore(sessao.getFim());
	}

}
