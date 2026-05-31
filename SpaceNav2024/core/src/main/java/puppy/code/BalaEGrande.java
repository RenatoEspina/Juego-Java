package puppy.code;
import com.badlogic.gdx.graphics.Texture;

public class BalaEGrande extends BalaBase {
    public BalaEGrande(float x, float y, float vx, float vy, Texture tx) {
        super(x, y, 50, 80, 5, tx, new MovimientoRecto(vx, vy));
    }
}
