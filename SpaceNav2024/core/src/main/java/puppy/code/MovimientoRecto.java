package puppy.code;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class MovimientoRecto implements EstrategiaMovimiento {
    private float velocidadX;
    private float velocidadY;

    public MovimientoRecto(float velocidadX, float velocidadY) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }

    @Override
    public void mover(Sprite spr, float delta) {
        // Fórmula física simple: Posición = Posición + Velocidad * Tiempo
        float nuevaX = spr.getX() + velocidadX * delta;
        float nuevaY = spr.getY() + velocidadY * delta;

        spr.setPosition(nuevaX, nuevaY);
    }

    // Getters y Setters necesarios si quieres que el enemigo rebote
    // (Al chocar con la pared, cambias el signo de la velocidad)
    public float getVelocidadX() { return velocidadX; }
    public float getVelocidadY() { return velocidadY; }
    public void setVelocidadX(float v) { this.velocidadX = v; }
    public void setVelocidadY(float v) { this.velocidadY = v; }
}
