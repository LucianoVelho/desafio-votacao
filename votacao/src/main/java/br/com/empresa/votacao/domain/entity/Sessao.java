package br.com.empresa.votacao.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Sessao {
	Pauta pauta; 
	LocalDateTime inicio;
	LocalDateTime fim;
	List<Voto> votos;
}
