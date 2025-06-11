package app.test;

import app.model.Usuario;
import app.model.Carona;
import app.dao.CadastroDeCaronas;

public class TesteCadastroCarona {

    public static void main(String[] args) {
        // Criar um motorista
        Usuario motorista = new Usuario("João Silva", "11999999999");

        // Criar uma carona
        Carona carona = new Carona(motorista, "Centro", "Universidade", "18:00");

        // Criar o objeto para cadastrar carona no banco
        CadastroDeCaronas cadastro = new CadastroDeCaronas();

        // Salvar a carona
        cadastro.salvar(carona);
    }
}
