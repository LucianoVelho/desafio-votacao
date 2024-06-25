package br.com.empresa.votacao.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.empresa.votacao.constant.ErrorMessagesConstant;
import br.com.empresa.votacao.constant.ValidationMessagesConstant;
import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.exceptions.exception.NotFoundException;
import br.com.empresa.votacao.service.PautaService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class) 
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PautaControllerIntegrationTest {
	
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PautaService pautaService;
    
    @Value("${api.pauta.endpoint}")
    private String pautaEndpoint;
    
    @Test
    public void testCreatePauta() throws Exception {
         
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setNome("Nome da Pauta");
        pautaDTO.setDescricao("Descrição da Pauta");

        PautaDTO pautaCriada = new PautaDTO();
        pautaCriada.setId(1L);
        pautaCriada.setNome(pautaDTO.getNome());
        pautaCriada.setDescricao(pautaDTO.getDescricao());
        when(pautaService.create(any())).thenReturn(pautaCriada);

        mockMvc.perform(post(pautaEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pautaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Nome da Pauta")))
                .andExpect(jsonPath("$.descricao", is("Descrição da Pauta"))); 

        verify(pautaService, times(1)).create(any(PautaDTO.class));
    }
    
    @Test
    public void testFindAllPautas() throws Exception {
    	
        List<PautaDTO> pautas = new ArrayList<>();
        PautaDTO pauta1 = new PautaDTO();
        pauta1.setId(1L);
        pauta1.setNome("Pauta 1");
        pauta1.setDescricao("Descrição da Pauta 1");
        pautas.add(pauta1);
        PautaDTO pauta2 = new PautaDTO();
        pauta2.setId(2L);
        pauta2.setNome("Pauta 2");
        pauta2.setDescricao("Descrição da Pauta 2");
        pautas.add(pauta2);
        when(pautaService.buscarTodasPautas()).thenReturn(pautas);

        mockMvc.perform(get(pautaEndpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Pauta 1"))) 
                .andExpect(jsonPath("$[1].id", is(2))) 
                .andExpect(jsonPath("$[1].descricao", is("Descrição da Pauta 2"))); 

        verify(pautaService, times(1)).buscarTodasPautas();
    }
    
    @Test
    public void testBuscarPautaPorId() throws Exception {

        Long pautaId = 1L;
        PautaDTO pauta = new PautaDTO();
        pauta.setId(pautaId);
        pauta.setNome("Pauta Teste");
        pauta.setDescricao("Descrição da Pauta Teste");
        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);

        mockMvc.perform(get(pautaEndpoint + "/{id}", pautaId)) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Pauta Teste"))) 
                .andExpect(jsonPath("$.descricao", is("Descrição da Pauta Teste"))); 

        verify(pautaService, times(1)).buscarPorId(pautaId);
    }
    
    @Test
    public void testHandleNotFoundException() throws Exception {
    	
        Long pautaId = 999L;
        doThrow(new NotFoundException(ErrorMessagesConstant.PAUTA_NOT_FOUND)).when(pautaService).buscarPorId(pautaId);

        mockMvc.perform(get(pautaEndpoint + "/{id}", pautaId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value(ErrorMessagesConstant.PAUTA_NOT_FOUND));

        verify(pautaService, times(1)).buscarPorId(pautaId);
    }
	@Test
	public void testNullIdValidation() throws Exception {
		String requestBody = "{\"id\":1}";

		mockMvc.perform(post(pautaEndpoint).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
				 .andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasItem("Campo nome não pode ser nulo")))
				.andExpect(jsonPath("$.errors", hasItem("Campo ID deve ser nulo")))
				.andExpect(jsonPath("$.errors", hasItem("Campo nome não pode estar em branco")));

	}
}
