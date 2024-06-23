package br.com.empresa.votacao.domain.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "VOTO")
public class Voto extends AbstractEntity<Long> {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ASSOCIADO", referencedColumnName = "ID")
	private Associado associado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SESSAO", referencedColumnName = "ID")
	private Sessao sessao;
	
	@Column(name = "VOTO_ASSOCIADO")
	private boolean votoAssociado;
}
