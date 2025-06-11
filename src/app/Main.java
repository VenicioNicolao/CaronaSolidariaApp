package app;

import app.dao.CadastroDeCaronas;
import app.model.Carona;
import app.model.Usuario;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CadastroDeCaronas cadastro = new CadastroDeCaronas();

        Usuario motorista = new Usuario("João Silva", "11999999999");
        Carona carona = new Carona(motorista, "Centro", "Universidade", "18:00");

        cadastro.salvar(carona);

        List<Carona> caronas = cadastro.listarCaronas();
        System.out.println("Todas as caronas:");
        for (Carona c : caronas) {
            System.out.println(c);
        }
    }
}