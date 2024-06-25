package br.com.empresa.votacao.controller;

import java.util.List;

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

import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.service.PautaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
public class PautaController {

	@Autowired
	private PautaService pautaService;
	
	@PostMapping("${api.pauta.endpoint}")
	public ResponseEntity<PautaDTO> createPauta(@Valid @RequestBody PautaDTO pautaDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.create(pautaDTO));
	}
	
	@GetMapping("${api.pauta.endpoint}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity <List<PautaDTO>>  findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarTodasPautas());
	}
	
    @GetMapping("${api.pauta.endpoint}/{id}")
    public ResponseEntity<PautaDTO> buscarPautaPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPorId(id));
    }
	
}
