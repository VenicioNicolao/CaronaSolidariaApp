package app.model;

public class Carona {
    private int id;
    private Usuario motorista;
    private String origem;
    private String destino;
    private String horario;

    public Carona(int id, Usuario motorista, String origem, String destino, String horario) {
        this.id = id;
        this.motorista = motorista;
        this.origem = origem;
        this.destino = destino;
        this.horario = horario;
    }

    public Carona(Usuario motorista, String origem, String destino, String horario) {
        this.id = 0;
        this.motorista = motorista;
        this.origem = origem;
        this.destino = destino;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public Usuario getMotorista() {
        return motorista;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public String getHorario() {
        return horario;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Carona de " + origem + " para " + destino +
                " às " + horario + ", com " + motorista.getNome();
    }
}