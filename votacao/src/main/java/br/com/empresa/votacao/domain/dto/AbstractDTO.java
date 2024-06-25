package br.com.empresa.votacao.domain.dto;

import java.io.Serializable;

import br.com.empresa.votacao.constant.ValidationMessagesConstant;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Null;
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
public abstract class AbstractDTO<I extends Serializable> {
	
    @Null(message = "Campo ID" + ValidationMessagesConstant.MUST_BE_NULL)
	protected I id;

}
