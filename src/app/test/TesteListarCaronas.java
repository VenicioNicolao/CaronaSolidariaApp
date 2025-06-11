package app.test;

import app.dao.CadastroDeCaronas;
import app.model.Carona;

import java.util.List;

public class TesteListarCaronas {

    public static void main(String[] args) {
        CadastroDeCaronas cadastro = new CadastroDeCaronas();
        List<Carona> caronas = cadastro.listarCaronas();

        if (caronas.isEmpty()) {
            System.out.println("Nenhuma carona cadastrada.");
        } else {
            for (Carona c : caronas) {
                System.out.printf("Motorista: %s (%s) - De %s para %s às %s%n",
                        c.getMotorista().getNome(),
                        c.getMotorista().getTelefone(),
                        c.getOrigem(),
                        c.getDestino(),
                        c.getHorario());
            }
        }
    }
}