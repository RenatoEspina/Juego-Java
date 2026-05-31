package puppy.code;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MovimientoEstatico implements EstrategiaMovimiento {

    @Override
    public void mover(Sprite spr, float delta) {
        // El jefe no se mueve por sí solo, se queda quieto.
    }

    // --- MÉTODOS VACÍOS PARA IGNORAR EMPUJONES ---
    // Cuando la nave choca, intenta "empujar" al enemigo usando setSpeed.
    // Al dejar estos métodos vacíos, el jefe ignora ese empuje.

    public float getXSpeed() {
        return 0;
    }

    public float getYSpeed() {
        return 0;
    }

    public void setXSpeed(float x) {
        // No hacer nada (Ignorar física de rebote)
    }

    public void setYSpeed(float y) {
        // No hacer nada (Ignorar física de rebote)
    }
}
