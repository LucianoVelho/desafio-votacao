package br.com.empresa.votacao.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.com.empresa.votacao.constant.ValidationMessagesConstant;
import br.com.empresa.votacao.domain.entity.Pauta;
import br.com.empresa.votacao.domain.entity.Voto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	
	@NotNull(message = "Campo pauta" + ValidationMessagesConstant.NOT_NULL)
	private Long pauta; 
	
	@NotNull(message = "Campo inicio" + ValidationMessagesConstant.NOT_NULL)
	private LocalDateTime inicio;
	
	private LocalDateTime fim;

}
