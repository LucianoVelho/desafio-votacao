package br.com.empresa.votacao.service;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.empresa.votacao.TestUtil;
import br.com.empresa.votacao.constant.ErrorMessagesConstant;
import br.com.empresa.votacao.constant.ValidacaoCPFConstant;
import br.com.empresa.votacao.domain.dto.AssociadoDTO;
import br.com.empresa.votacao.domain.dto.VerificaCpfDTO;
import br.com.empresa.votacao.domain.entity.Associado;
import br.com.empresa.votacao.exceptions.exception.NotFoundException;
import br.com.empresa.votacao.repository.AssociadoRepository;
import br.com.empresa.votacao.repository.PautaRepository;

@SpringBootTest
public class AssociadoServiceImplUnitTest extends TestUtil {
	
    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssociadoServiceImpl associadoServiceImpl;
    
    AssociadoDTO associadoDTO = AssociadoDTO.builder().nome("Nome da Associado").CPF(gerarCPF()).build();
    Associado associado = Associado.builder().nome(associadoDTO.getNome()).cpf(associadoDTO.getCPF()).build();
    
    
    @Test
    public void testCreateAssociadoSuccess() {
        AssociadoDTO associadoDTO = new AssociadoDTO();
        associadoDTO.setNome("Nome da Associado");
        associadoDTO.setCPF(gerarCPF());

        Associado associado = new Associado();
        associado.setId(UUID.randomUUID());
        associado.setNome(associadoDTO.getNome());
        associado.setCpf(associadoDTO.getCPF());

        when(associadoRepository.findByCpf(associado.getCpf())).thenReturn(Optional.empty());
        when(associadoRepository.save(any())).thenReturn(associado);
        when(modelMapper.map(any(), eq(Associado.class))).thenReturn(associado);
        when(modelMapper.map(any(), eq(AssociadoDTO.class))).thenReturn(associadoDTO);

        AssociadoDTO result = associadoServiceImpl.create(associadoDTO);

        assertNotNull(result);
        assertEquals(associado.getCpf(), result.getCPF());
        verify(associadoRepository, times(1)).save(any());
    }
    
    @Test
    public void testCreateAssociadoCpfAlreadyExists() {

        when(associadoRepository.findByCpf(associado.getCpf())).thenReturn(Optional.of(associado));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
        	associadoServiceImpl.create(associadoDTO);
        });

        assertEquals(ErrorMessagesConstant.CPF_CADASTRADO + associadoDTO.getCPF(), exception.getMessage());
        verify(associadoRepository, never()).save(any());
    }
    
    @Test
    public void testBuscarTodosAssociados() {
        List<Associado> associados = Arrays.asList(associado);
        List<AssociadoDTO> associadosDTO = Arrays.asList(associadoDTO);

        when(associadoRepository.findAll()).thenReturn(associados);
        when(modelMapper.map(associado, AssociadoDTO.class)).thenReturn(associadoDTO);

        List<AssociadoDTO> result = associadoServiceImpl.buscarTodosAssociados();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(associadoDTO.getId(), result.get(0).getId());
        verify(associadoRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPorId() {
        UUID id = associado.getId();

        when(associadoRepository.findById(id)).thenReturn(Optional.of(associado));
        when(modelMapper.map(associado, AssociadoDTO.class)).thenReturn(associadoDTO);

        AssociadoDTO result = associadoServiceImpl.buscarPorId(id);

        assertNotNull(result);
        assertEquals(associadoDTO.getId(), result.getId());
        verify(associadoRepository, times(1)).findById(id);
    }

    @Test
    public void testBuscarPorIdNotFound() {
        UUID id = UUID.randomUUID();

        when(associadoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
        	associadoServiceImpl.buscarPorId(id);
        });

        String expectedMessage = ErrorMessagesConstant.ASSOOCIADO_NOT_FOUND + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(associadoRepository, times(1)).findById(id);
    }

    @Test
    public void testVerificaCPF_AbleToVote() {
        String cpf = gerarCPF();

        when(associadoRepository.findByCpf(cpf)).thenReturn(Optional.of(associado));

        VerificaCpfDTO result = associadoServiceImpl.verificaCPF(cpf);

        assertNotNull(result);
        assertEquals(ValidacaoCPFConstant.ABLE_TO_VOTE, result.getStatus());
        verify(associadoRepository, times(1)).findByCpf(cpf);
    }

    @Test
    public void testVerificaCPF_UnableToVote() {
        String cpf = gerarCPF();

        when(associadoRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        VerificaCpfDTO result = associadoServiceImpl.verificaCPF(cpf);

        assertNotNull(result);
        assertEquals(ValidacaoCPFConstant.UNABLE_TO_VOTE, result.getStatus());
        verify(associadoRepository, times(1)).findByCpf(cpf);
    }
    

}
