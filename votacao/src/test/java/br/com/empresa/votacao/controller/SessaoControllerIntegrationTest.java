package br.com.empresa.votacao.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.domain.dto.SessaoDTO;
import br.com.empresa.votacao.service.SessaoServiceImpl;



@ExtendWith(SpringExtension.class) 
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SessaoControllerIntegrationTest {
	
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessaoServiceImpl sessaoService;
    
    @Value("${api.sessao.endpoint}")
    private String sessaoEndpoint;
    
    @Value("${api.sessao.aberta.endpoint}")
    private String sessaoAbertaEndpoint;
    
    @Test
    public void testCreateSessao() throws Exception {
         
        SessaoDTO sessaoDTO = new SessaoDTO();
        sessaoDTO.setPauta(1L);
        sessaoDTO.setInicio(LocalDateTime.now());

        when(sessaoService.create(any())).thenReturn(sessaoDTO);

        mockMvc.perform(post(sessaoEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pauta").value(1));

        verify(sessaoService, times(1)).create(any(SessaoDTO.class));
    }
    
    @Test
    public void testFindAllSessoes() throws Exception {
        mockMvc.perform(get(sessaoEndpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testBuscarSessaoPorId() throws Exception {
    	Long id = 1L; 
        SessaoDTO sessaoDTO = new SessaoDTO();
        sessaoDTO.setId(id);
        sessaoDTO.setPauta(1L);
        sessaoDTO.setInicio(LocalDateTime.now());
       
        when(sessaoService.buscaSessaoPorID(any())).thenReturn(sessaoDTO);
        
        mockMvc.perform(get(sessaoEndpoint+"/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id)); 
    }

    @Test
    public void testVerificaSessaoAtiva() throws Exception {
        Long id = 1L; 

        mockMvc.perform(get(sessaoAbertaEndpoint+"/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false")); 
    }

    @Test
    public void testDeletaSessao() throws Exception {
        Long id = 1L; 

        mockMvc.perform(delete(sessaoEndpoint + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
