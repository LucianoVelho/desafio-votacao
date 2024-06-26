package br.com.empresa.votacao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.domain.dto.SessaoDTO;
import br.com.empresa.votacao.domain.entity.Sessao;
import br.com.empresa.votacao.service.SessaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
public class SessaoController {
	
	@Autowired
	private SessaoService sessaoService;
	
	@PostMapping("${api.sessao.endpoint}")
	public ResponseEntity<SessaoDTO> createPauta(@Valid @RequestBody SessaoDTO sessaoDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(sessaoService.create(sessaoDTO));
	}
	
	@GetMapping("${api.sessao.endpoint}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity <List<SessaoDTO>>  findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(sessaoService.buscarTodosSessoes());
	}
	
    @GetMapping("${api.sessao.endpoint}/{id}")
    public ResponseEntity<SessaoDTO> buscarPautaPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(sessaoService.buscaSessaoPorID(id));
    }
    
    @GetMapping("${api.sessao.aberta.endpoint}/{id}")
    public ResponseEntity<Boolean> verificaSessaoAtiva(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(sessaoService.isSessaoAberta(id));
    }
    
    @DeleteMapping("${api.sessao.endpoint}/{id}")
    public void deletaSessao(@PathVariable Long id) {
    	sessaoService.deletaSessao(id);
    }

}
