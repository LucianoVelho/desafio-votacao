package br.com.empresa.votacao.domain.dto;

import java.io.Serializable;
import java.util.UUID;

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
public abstract class PessoaDTO extends AbstractDTO<UUID> {
	
	@NotNull(message = "Campo nome" + ValidationMessagesConstant.NOT_NULL)
	@NotBlank(message = "Campo nome" + ValidationMessagesConstant.NOT_BLANK)
	@Size(min = 1, max = 100)
	private String nome;
	
	@NotNull(message = "Campo CPF" + ValidationMessagesConstant.NOT_NULL)
	@NotBlank(message = "Campo CPF" + ValidationMessagesConstant.NOT_BLANK)
	@Size(min = 11, max = 14)
	private String CPF;

}
