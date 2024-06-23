package br.com.empresa.votacao.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Voto {
	Associado associado;
	Sessao sessao;
	boolean voto;
}
