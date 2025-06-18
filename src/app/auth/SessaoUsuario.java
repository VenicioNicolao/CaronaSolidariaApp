package app.auth;

public class SessaoUsuario {
    private static String loginUsuario;
    private static String nomeUsuario;
    private static String emailUsuario;

    public static void setUsuario(String login, String nome, String email) {
        loginUsuario = login;
        nomeUsuario = nome;
        emailUsuario = email;
    }

    public static String getLogin() {
        return loginUsuario;
    }

    public static String getNome() {
        return nomeUsuario;
    }

    public static String getEmail() {
        return emailUsuario;
    }
}