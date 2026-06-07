package puppy.code.Bala;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.MovementStrategy.MovimientoRecto;

public class BalaENormal extends BalaBase {
    public BalaENormal(float x, float y, float vx, float vy, Texture tx) {
        super(x, y, 25, 45, 2, tx, new MovimientoRecto(vx, vy));
    }
}
