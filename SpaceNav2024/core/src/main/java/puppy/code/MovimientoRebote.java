package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MovimientoRebote implements EstrategiaMovimiento {
    private float xSpeed;
    private float ySpeed;

    public MovimientoRebote(float xSpeed, float ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void mover(Sprite spr, float delta) {
        // Calcular la nueva posición propuesta
        float nuevaX = spr.getX() + xSpeed * delta;
        float nuevaY = spr.getY() + ySpeed * delta;

        // Lógica de Rebote en Eje X
        // Si se sale por la izquierda (< 0) o por la derecha (> ancho pantalla)
        if (nuevaX < 0 || nuevaX + spr.getWidth() > Gdx.graphics.getWidth()) {
            xSpeed *= -1; // Invertir dirección horizontal

            // Corregir posición para que no se quede "pegado" fuera de la pantalla
            nuevaX = spr.getX() + xSpeed * delta;
        }

        // Lógica de Rebote en Eje Y
        // Si se sale por abajo (< 0) o por arriba (> alto pantalla)
        if (nuevaY < 0 || nuevaY + spr.getHeight() > Gdx.graphics.getHeight()) {
            ySpeed *= -1; // Invertir dirección vertical

            // Corregir posición
            nuevaY = spr.getY() + ySpeed * delta;
        }

        // Aplicar la posición final validada
        spr.setPosition(nuevaX, nuevaY);
    }

    // --- Getters y Setters ---
    // Necesarios para que EnemigoBase pueda delegar la consulta de velocidad

    public float getXSpeed() { return xSpeed; }
    public void setXSpeed(float x) { this.xSpeed = x; }
    public float getYSpeed() { return ySpeed; }
    public void setYSpeed(float y) { this.ySpeed = y; }
}
