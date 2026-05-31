package puppy.code.MovementStrategy;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface EstrategiaMovimiento {
    // Solo pedimos el Sprite (quien se mueve) y el delta (tiempo)
    void mover(Sprite spr, float delta);
}
