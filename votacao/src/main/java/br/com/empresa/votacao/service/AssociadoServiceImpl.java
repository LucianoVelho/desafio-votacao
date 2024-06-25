package br.com.empresa.votacao.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.empresa.votacao.constant.ErrorMessagesConstant;
import br.com.empresa.votacao.constant.ValidacaoCPFConstant;
import br.com.empresa.votacao.domain.dto.AssociadoDTO;
import br.com.empresa.votacao.domain.dto.VerificaCpfDTO;
import br.com.empresa.votacao.domain.entity.Associado;
import br.com.empresa.votacao.domain.entity.Pauta;
import br.com.empresa.votacao.exceptions.exception.NotFoundException;
import br.com.empresa.votacao.repository.AssociadoRepository;
import jakarta.transaction.Transactional;

@Service
public class AssociadoServiceImpl extends AbstractService implements AssociadoService{
	
	@Autowired
	private AssociadoRepository associadoRepository;
	
	@Transactional
	@Override
	public AssociadoDTO create(AssociadoDTO associadoDTO) {
		associadoDTO.setCPF(associadoDTO.getCPF().replaceAll("\\D", ""));
		validaCPF(associadoDTO.getCPF());
		if(associadoRepository.findByCpf(associadoDTO.getCPF()).isPresent()) {
			throw new NotFoundException(ErrorMessagesConstant.CPF_CADASTRADO+associadoDTO.getCPF());
		}
		Associado associado = associadoRepository.save(modelMapper.map(associadoDTO, Associado.class));
		return modelMapper.map(associado, AssociadoDTO.class);
	}

	@Override
	public List<AssociadoDTO> buscarTodosAssociados() {
		List<Associado> associados = associadoRepository.findAll();
		List<AssociadoDTO> associadosDTO = convertToDTO(associados, AssociadoDTO.class);
		return associadosDTO;
	}

	@Override
	public AssociadoDTO buscarPorId(UUID id) {
		Associado associado = associadoRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessagesConstant.ASSOOCIADO_NOT_FOUND + id));
		return modelMapper.map(associado, AssociadoDTO.class);
	}

	@Override
	public VerificaCpfDTO verificaCPF(String CPF) {
		
        validaCPF(CPF);
        if(associadoRepository.findByCpf(CPF).isPresent()) {
        	return VerificaCpfDTO.builder().status(ValidacaoCPFConstant.ABLE_TO_VOTE).build();
        }
		return VerificaCpfDTO.builder().status(ValidacaoCPFConstant.UNABLE_TO_VOTE).build();
	}
	
	public boolean validaCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        for (int j = 9; j < 11; j++) {
            int soma = 0;
            for (int i = 0; i < j; i++) {
                soma += (cpf.charAt(i) - '0') * ((j + 1) - i);
            }
            int digito = (11 - (soma % 11)) % 10;
            if (cpf.charAt(j) - '0' != digito) {
            	throw new NotFoundException(ErrorMessagesConstant.CPF_INVALIDO + cpf);
            }
        }

        return true;
	}

}
