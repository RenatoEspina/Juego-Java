package puppy.code.Bala;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.MovementStrategy.MovimientoRecto;

public class BalaDefault extends BalaBase {
    public BalaDefault(float x, float y, float vx, float vy, Texture tx) {
        super(x, y, 16, 32, 1, tx, new MovimientoRecto(vx, vy));
    }
}
