package br.com.empresa.votacao;

public class TestUtil {
	
    public static String gerarCPF() {
        StringBuilder cpf = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            cpf.append((int) (Math.random() * 10));
        }

        int primeiroDV = calcularDV(cpf.toString(), 10);
        cpf.append(primeiroDV);

        int segundoDV = calcularDV(cpf.toString(), 11);
        cpf.append(segundoDV);

        return cpf.toString();
    }

    private static int calcularDV(String cpf, int pesoMaximo) {
        int soma = 0;
        int multiplicador = pesoMaximo;

        for (int i = 0; i < cpf.length(); i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * multiplicador;
            multiplicador--;
        }

        soma = 11 - soma % 11;

        if (soma == 0 || soma == 10) {
            soma = 0;
        }

        return soma;
    }
}
