package br.com.empresa.votacao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.empresa.votacao.constant.ErrorMessagesConstant;
import br.com.empresa.votacao.constant.LogMessagesConstant;
import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.domain.dto.SessaoDTO;
import br.com.empresa.votacao.domain.entity.Pauta;
import br.com.empresa.votacao.domain.entity.Sessao;
import br.com.empresa.votacao.exceptions.exception.NomeDuplicadoException;
import br.com.empresa.votacao.exceptions.exception.NotFoundException;
import br.com.empresa.votacao.repository.PautaRepository;
import br.com.empresa.votacao.repository.SessaoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PautaServiceImpl extends AbstractService implements PautaService {

	@Autowired
	private PautaRepository pautaRepository;
	
	@Autowired 
	private SessaoService sessaoService;

	@Transactional
	@Override
	public PautaDTO create(PautaDTO pautaDTO) {
		log.info(LogMessagesConstant.INICIANDO_CRIACAO_PAUTA, pautaDTO);
		verificarNomeDuplicado(pautaDTO.getNome());
		Pauta pauta = pautaRepository.save(criaPauta(pautaDTO));
		log.info(LogMessagesConstant.PAUTA_CRIADA_SUCESSO, pauta.getId());
		return modelMapper.map(pauta, PautaDTO.class);
	}
	
	public Pauta criaPauta(PautaDTO pautaDTO) {
		log.debug(LogMessagesConstant.CONVERTENDO_PAUTA_DTO, pautaDTO);
		Pauta pauta = modelMapper.map(pautaDTO, Pauta.class);
		return pauta;
	}

	@Override
	public List<PautaDTO> buscarTodasPautas() {
		log.info(LogMessagesConstant.BUSCANDO_TODAS_PAUTAS);
		List<PautaDTO> pautas = convertToDTO(pautaRepository, PautaDTO.class);
		log.info(LogMessagesConstant.ENCONTRADAS_PAUTAS, pautas.size());
		return pautas;
	}

    public void verificarNomeDuplicado(String nome) {
        if (pautaRepository.existsByNomeIgnoreCase(nome)) {
            throw new NomeDuplicadoException(nome); 
        }
    }

	@Override
	public PautaDTO buscarPorId(Long id) {
		log.info(LogMessagesConstant.BUSCANDO_PAUTA_ID);
        Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessagesConstant.PAUTA_NOT_FOUND + id));
        log.info(LogMessagesConstant.PAUTA_ENCONTRADA, pauta.getId());
        return modelMapper.map(pauta, PautaDTO.class);
	}

}
