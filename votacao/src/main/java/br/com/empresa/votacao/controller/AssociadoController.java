package br.com.empresa.votacao.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.empresa.votacao.constant.ValidacaoCPFConstant;
import br.com.empresa.votacao.constant.ValidationMessagesConstant;
import br.com.empresa.votacao.domain.dto.AssociadoDTO;
import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.domain.dto.VerificaCpfDTO;
import br.com.empresa.votacao.service.AssociadoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
public class AssociadoController {
	
	@Autowired
	private AssociadoService associadoService;
	
	@PostMapping("${api.associado.endpoint}")
	public ResponseEntity<AssociadoDTO> createAssociado(@Valid @RequestBody AssociadoDTO associadoDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(associadoService.create(associadoDTO));
	}
	
	@GetMapping("${api.associado.endpoint}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity <List<AssociadoDTO>>  findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(associadoService.buscarTodosAssociados());
	}
	
    @GetMapping("${api.associado.endpoint}/{id}")
    public ResponseEntity<AssociadoDTO> buscarAssociadoPorId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(associadoService.buscarPorId(id));
    }
    
    @GetMapping("${api.associado.cpf.endpoint}/{cpf}")
    public ResponseEntity<VerificaCpfDTO> buscarAssociadoPorCpf(@PathVariable String cpf) {
    	VerificaCpfDTO dto = associadoService.verificaCPF(cpf);
    	if(ValidacaoCPFConstant.ABLE_TO_VOTE.contains(dto.getStatus())) {
    		return ResponseEntity.status(HttpStatus.OK).body(associadoService.verificaCPF(cpf));
    	}else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(associadoService.verificaCPF(cpf));
    	}
        
    }

}
