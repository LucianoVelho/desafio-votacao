package br.com.empresa.votacao.service;

import static org.hamcrest.CoreMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.empresa.votacao.domain.dto.PautaDTO;
import br.com.empresa.votacao.domain.entity.Pauta;
import br.com.empresa.votacao.exceptions.exception.NomeDuplicadoException;
import br.com.empresa.votacao.exceptions.exception.NotFoundException;
import br.com.empresa.votacao.repository.PautaRepository;

@SpringBootTest
public class PautaServiceImplUnitTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PautaServiceImpl pautaService;

    @Test
    void testCreatePautaSuccess() {

        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setNome("Nova Pauta");
        pautaDTO.setDescricao("Descrição da Nova Pauta");

        // Mocking
        Pauta pautaEntity = Pauta.builder().id(1L).nome("Nova Pauta").descricao("Descrição da Nova Pauta").build();
        when(modelMapper.map(any(), eq(Pauta.class))).thenReturn(pautaEntity);
        when(modelMapper.map(any(), eq(PautaDTO.class))).thenReturn(pautaDTO); 
        when(pautaRepository.save(any())).thenReturn(pautaEntity);
 
        PautaDTO createdPauta = pautaService.create(pautaDTO);

        assertEquals(pautaDTO.getNome(), createdPauta.getNome());
        assertEquals(pautaDTO.getDescricao(), createdPauta.getDescricao());

        verify(modelMapper, times(1)).map(eq(pautaDTO), eq(Pauta.class));
        verify(pautaRepository, times(1)).save(any());
    }
    
    @Test
    void testCreatePautaNomeDuplicadoException() {
    	
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setNome("Pauta Duplicada");

        when(pautaRepository.existsByNomeIgnoreCase(pautaDTO.getNome())).thenReturn(true);

        assertThrows(NomeDuplicadoException.class, () -> pautaService.create(pautaDTO));
        verify(pautaRepository, never()).save(any()); 
    }
    
    @Test
    void testBuscarTodasPautas() {
;
        Pauta pauta1 = Pauta.builder().id(1L).descricao("Pauta 1").build();
        Pauta pauta2 = Pauta.builder().id(2L).descricao("Pauta 2").build();
        List<Pauta> pautasEntities = Arrays.asList(pauta1, pauta2);

        when(pautaRepository.findAll()).thenReturn(pautasEntities);

        PautaDTO pautaDTO1 = PautaDTO.builder().id(1L).descricao("Pauta 1").build();
        PautaDTO pautaDTO2 = PautaDTO.builder().id(2L).descricao("Pauta 2").build();
        
        List<PautaDTO> expectedPautasDTO = Arrays.asList(pautaDTO1, pautaDTO2);
        when(modelMapper.map(pauta1, PautaDTO.class)).thenReturn(pautaDTO1);
        when(modelMapper.map(pauta2, PautaDTO.class)).thenReturn(pautaDTO2);

        List<PautaDTO> pautasRetornadas = pautaService.buscarTodasPautas();

        assertEquals(expectedPautasDTO.size(), pautasRetornadas.size());
        assertEquals(expectedPautasDTO.get(0).getId(), pautasRetornadas.get(0).getId());
        assertEquals(expectedPautasDTO.get(0).getNome(), pautasRetornadas.get(0).getNome());
        assertEquals(expectedPautasDTO.get(1).getId(), pautasRetornadas.get(1).getId());
        assertEquals(expectedPautasDTO.get(1).getNome(), pautasRetornadas.get(1).getNome());

        verify(pautaRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(), eq(PautaDTO.class));
    }
    
    @Test
    void testBuscarTodasPautasComListaVazia() {

        when(pautaRepository.findAll()).thenReturn(Collections.emptyList());

        List<PautaDTO> pautasRetornadas = pautaService.buscarTodasPautas();

        assertEquals(0, pautasRetornadas.size());

        verify(pautaRepository, times(1)).findAll();
        verifyNoInteractions(modelMapper);
    }
    
    @Test
    void testBuscarPautaPorIdSucesso() {
    	
        Long idPauta = 1L;
        Pauta pautaEntity = Pauta.builder().id(idPauta).descricao("Pauta Teste").build();
        when(pautaRepository.findById(idPauta)).thenReturn(Optional.of(pautaEntity));

        PautaDTO pautaDTO = PautaDTO.builder().id(idPauta).descricao("Pauta Teste").build();
        when(modelMapper.map(pautaEntity, PautaDTO.class)).thenReturn(pautaDTO);

        PautaDTO pautaRetornada = pautaService.buscarPorId(idPauta);
        assertNotNull(pautaRetornada);
        assertEquals(idPauta, pautaRetornada.getId());

        verify(pautaRepository, times(1)).findById(idPauta);
        verify(modelMapper, times(1)).map(pautaEntity, PautaDTO.class);
    }
    
    @Test
    void testBuscarPautaPorIdNotFound() {
        Long idPauta = 1L;
        
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            pautaService.buscarPorId(idPauta);
        });

        verify(pautaRepository, times(1)).findById(idPauta);
        verifyNoInteractions(modelMapper); 
    }
}
