package br.com.empresa.votacao.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.com.empresa.votacao.domain.entity.Pauta;
import br.com.empresa.votacao.domain.entity.Voto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SessaoDTO extends AbstractDTO<Long>  {

	private Pauta pauta; 
	private LocalDateTime inicio;
	private LocalDateTime fim;
	private List<Voto> votos;
}
