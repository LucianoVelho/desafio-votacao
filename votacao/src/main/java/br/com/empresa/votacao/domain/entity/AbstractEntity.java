package br.com.empresa.votacao.domain.entity;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class AbstractEntity<UUID extends Serializable> implements Serializable{
	
	private static final long serialVersion = 1L;

	protected UUID id;

}
