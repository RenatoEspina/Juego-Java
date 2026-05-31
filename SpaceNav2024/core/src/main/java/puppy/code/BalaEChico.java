package puppy.code;
import com.badlogic.gdx.graphics.Texture;

public class BalaEChico extends BalaBase {
    public BalaEChico(float x, float y, float vx, float vy, Texture tx) {
        super(x, y, 12, 24, 1, tx, new MovimientoRecto(vx, vy));
    }
}
