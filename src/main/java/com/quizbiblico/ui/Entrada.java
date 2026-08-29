package com.quizbiblico.ui;

import java.util.Scanner;

public class Entrada {

    private final Scanner scanner = new Scanner(System.in);

    public String lerTexto(String rotulo) {
        System.out.print(rotulo);
        return scanner.nextLine().trim();
    }

    public int lerInteiro(String rotulo, int minimo, int maximo) {
        while (true) {
            String digitado = lerTexto(rotulo);

            try {
                int numero = Integer.parseInt(digitado);

                if (numero < minimo || numero > maximo) {
                    System.out.println("  >> Digite um numero entre " + minimo + " e " + maximo + ".");
                    continue;
                }

                return numero;

            } catch (NumberFormatException e) {
                System.out.println("  >> '" + digitado + "' nao e um numero. Tente de novo.");
            }
        }
    }

    public int lerAlternativa(String rotulo) {
        while (true) {
            String digitado = lerTexto(rotulo).toUpperCase();

            switch (digitado) {
                case "A":
                    return 0;
                case "B":
                    return 1;
                case "C":
                    return 2;
                case "D":
                    return 3;
                default:
                    System.out.println("  >> Responda com A, B, C ou D.");
            }
        }
    }

    public void pausar() {
        lerTexto("\nPressione ENTER para continuar...");
    }

    public void fechar() {
        scanner.close();
    }
}