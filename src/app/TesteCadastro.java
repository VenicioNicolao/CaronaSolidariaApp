package app;

import app.dao.CadastroDeCaronas;
import app.model.Carona;
import app.model.Usuario;

import java.util.List;

public class TesteCadastro {
    public static void main(String[] args) {
        CadastroDeCaronas cadastro = new CadastroDeCaronas();

        Usuario motorista = new Usuario("João Silva", "11999999999");
        Carona carona = new Carona(motorista, "Centro", "Universidade", "18:00");

        cadastro.salvar(carona);

        System.out.println("Todas as caronas:");
        List<Carona> todas = cadastro.listarCaronas();
        for (Carona c : todas) {
            System.out.println(c.getOrigem() + " -> " + c.getDestino() + ", às " + c.getHorario() + ", com " + c.getMotorista().getNome());
        }

        System.out.println("\nCaronas com origem 'Centro':");
        List<Carona> porOrigem = cadastro.buscarPorOrigem("Centro");
        for (Carona c : porOrigem) {
            System.out.println(c.getOrigem() + " -> " + c.getDestino() + ", às " + c.getHorario() + ", com " + c.getMotorista().getNome());
        }

        System.out.println("\nCaronas com destino 'Universidade':");
        List<Carona> porDestino = cadastro.buscarPorDestino("Universidade");
        for (Carona c : porDestino) {
            System.out.println(c.getOrigem() + " -> " + c.getDestino() + ", às " + c.getHorario() + ", com " + c.getMotorista().getNome());
        }
    }
}