package br.com.empresa.votacao.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.empresa.votacao.TestUtil;
import br.com.empresa.votacao.constant.ValidacaoCPFConstant;
import br.com.empresa.votacao.domain.dto.AssociadoDTO;
import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.domain.dto.VerificaCpfDTO;
import br.com.empresa.votacao.service.AssociadoService;
import br.com.empresa.votacao.service.PautaService;
import jakarta.validation.Valid;

@ExtendWith(SpringExtension.class) 
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AssociadoControllerIntegrationTest extends TestUtil {
	
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssociadoService associadoService;
    
    @Value("${api.associado.endpoint}")
    private String associadoEndpoint;
    
    @Value("${api.associado.cpf.endpoint}")
    private String associadoCpfEndpoint;
    
    @Test
	public void testCreateAssociado() throws JsonProcessingException, Exception {
    	
        AssociadoDTO associadoDTO = new AssociadoDTO();
        associadoDTO.setNome("Nome da Associado");
        associadoDTO.setCPF(gerarCPF());

        AssociadoDTO associadoCriada = new AssociadoDTO();
        associadoCriada.setId(UUID.randomUUID());
        associadoCriada.setNome(associadoDTO.getNome());
        associadoCriada.setCPF(associadoDTO.getCPF());
        when(associadoService.create(any())).thenReturn(associadoCriada);

        mockMvc.perform(post(associadoEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(associadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(associadoCriada.getId().toString())))
                .andExpect(jsonPath("$.nome", is(associadoCriada.getNome())))
                .andExpect(jsonPath("$.cpf", is(associadoDTO.getCPF()))); 

        verify(associadoService, times(1)).create(any(AssociadoDTO.class));
	
	}
    
    @Test
	public void testFindAll() throws Exception{
        List<AssociadoDTO> associados = new ArrayList<>();
        AssociadoDTO associado1 = AssociadoDTO.builder().id(UUID.randomUUID()).nome("Nome da Associado 1").CPF(gerarCPF()).build();
        AssociadoDTO associado2 = AssociadoDTO.builder().nome("Nome da Associado 2").CPF(gerarCPF()).build();
        associados.add(associado1);
        associados.add(associado2);
        when(associadoService.buscarTodosAssociados()).thenReturn(associados);

        mockMvc.perform(get(associadoEndpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(associado1.getId().toString())))
                .andExpect(jsonPath("$[0].nome", is(associado1.getNome()))) 
                .andExpect(jsonPath("$[0].cpf", is(associado1.getCPF()))) 
                .andExpect(jsonPath("$[1].id", is(associado2.getId()))) 
                .andExpect(jsonPath("$[1].nome", is(associado2.getNome()))) 
                .andExpect(jsonPath("$[1].cpf", is(associado2.getCPF()))); 

        verify(associadoService, times(1)).buscarTodosAssociados();
	}
	
    @Test
    public void testBuscarAssociadoPorId() throws Exception {
    	
        AssociadoDTO associado = AssociadoDTO.builder().id(UUID.randomUUID()).nome("Nome da Associado 1").CPF(gerarCPF()).build();
        when(associadoService.buscarPorId(associado.getId())).thenReturn(associado);

        mockMvc.perform(get(associadoEndpoint + "/{id}", associado.getId())) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(associado.getId().toString())))
                .andExpect(jsonPath("$.nome", is(associado.getNome()))) 
                .andExpect(jsonPath("$.cpf", is(associado.getCPF()))); 

        verify(associadoService, times(1)).buscarPorId(associado.getId());
  
    }
    @Test
    public void testBuscarAssociadoPorCpfAbleToVoto() throws Exception {
    	
    	VerificaCpfDTO dto = VerificaCpfDTO.builder().status(ValidacaoCPFConstant.ABLE_TO_VOTE).build();
        when(associadoService.verificaCPF(any())).thenReturn(dto);

        mockMvc.perform(get(associadoCpfEndpoint + "/{id}", gerarCPF())) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ValidacaoCPFConstant.ABLE_TO_VOTE))); 

    }
    @Test
    public void buscarAssociadoPorCpfUnableToVoto() throws Exception {
    	
    	VerificaCpfDTO dto = VerificaCpfDTO.builder().status(ValidacaoCPFConstant.UNABLE_TO_VOTE).build();
        when(associadoService.verificaCPF(any())).thenReturn(dto);

        mockMvc.perform(get(associadoCpfEndpoint + "/{id}", gerarCPF())) 
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(ValidacaoCPFConstant.UNABLE_TO_VOTE))); 

    }


}
