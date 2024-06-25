package br.com.empresa.votacao.domain.dto;

import java.io.Serializable;
import java.util.List;

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
public class AssociadoDTO extends PessoaDTO {
	
	private List <Voto> votos;

}
