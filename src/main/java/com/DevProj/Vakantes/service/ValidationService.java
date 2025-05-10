package com.DevProj.Vakantes.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /**
     * Valida se um email está em formato válido
     * @param email Email a ser validado
     * @return true se o email for válido, false caso contrário
     */
    public boolean isEmailValido(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Valida se um CPF está em formato válido e com dígitos verificadores corretos
     * @param cpf CPF a ser validado
     * @return true se o CPF for válido, false caso contrário
     */
    public boolean isCPFValido(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o primeiro dígito verificador
        if (dv1 != (cpf.charAt(9) - '0')) {
            return false;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o segundo dígito verificador
        return dv2 == (cpf.charAt(10) - '0');
    }

    /**
     * Valida se um CNPJ está em formato válido e com dígitos verificadores corretos
     * @param cnpj CNPJ a ser validado
     * @return true se o CNPJ for válido, false caso contrário
     */
    public boolean isCNPJValido(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cnpj.length(); i++) {
            if (cnpj.charAt(i) != cnpj.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int[] multiplicadores1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * multiplicadores1[i];
        }
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o primeiro dígito verificador
        if (dv1 != (cnpj.charAt(12) - '0')) {
            return false;
        }

        // Calcula o segundo dígito verificador
        int[] multiplicadores2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * multiplicadores2[i];
        }
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o segundo dígito verificador
        return dv2 == (cnpj.charAt(13) - '0');
    }
}