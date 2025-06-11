package service;
import app.model.Carona;

import java.util.ArrayList;
import java.util.List;

public class CaronaService {
    private List<Carona> caronas = new ArrayList<>();

    public void adicionarCarona(Carona carona) {
        caronas.add(carona);
    }

    public List<Carona> listarCaronas() {
        return caronas;
    }
}
