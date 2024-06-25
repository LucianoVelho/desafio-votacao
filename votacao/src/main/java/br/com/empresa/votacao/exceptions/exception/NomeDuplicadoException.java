package br.com.empresa.votacao.exceptions.exception;

import br.com.empresa.votacao.constant.ErrorMessagesConstant;

public class NomeDuplicadoException extends RuntimeException {
	  public NomeDuplicadoException(String nome) {
		    super(ErrorMessagesConstant.NOME_DUPLICADO + nome);
		  }
}
