package br.com.empresa.votacao.domain.dto;

import java.util.List;

import br.com.empresa.votacao.constant.ValidationMessagesConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PautaDTO extends AbstractDTO<Long> {
	
	@NotNull(message = "Campo nome" + ValidationMessagesConstant.NOT_NULL)
	@NotBlank(message = "Campo nome" + ValidationMessagesConstant.NOT_BLANK)
	@Size(min = 1, max = 100)
	private String nome;
	
	@Size(max = 255)
	private String descricao;
	
}
