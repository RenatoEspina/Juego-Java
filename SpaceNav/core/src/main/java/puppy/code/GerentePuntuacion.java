package puppy.code;

public class GerentePuntuacion {
    // 1. La instancia estática única (privada)
    private static GerentePuntuacion instancia;

    // 2. Variables de estado
    private int highScore;
    private int scoreActual;

    // 3. Constructor Privado (Nadie externo puede hacer 'new GerentePuntuacion')
    private GerentePuntuacion() {
        highScore = 0;
        scoreActual = 0;
    }

    // 4. Método de acceso global
    public static GerentePuntuacion getInstance() {
        if (instancia == null) {
            instancia = new GerentePuntuacion();
        }
        return instancia;
    }

    // Métodos de lógica de negocio
    public void agregarPuntaje(int puntos) {
        scoreActual += puntos;
        if (scoreActual > highScore) {
            highScore = scoreActual;
        }
    }

    public int getScoreActual() {
        return scoreActual;
    }

    public int getHighScore() {
        return highScore;
    }

    public void resetScore() {
        scoreActual = 0;
    }

    // Opcional: Setters si necesitas cargar partidas guardadas, etc.
    public void setHighScore(int score) {
        this.highScore = score;
    }
}
