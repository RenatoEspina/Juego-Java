package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
public class EnemigoChico extends EnemigoBase {

    public EnemigoChico(EstrategiaMovimiento estrategia, Texture tNormal, Texture tDebil, int x, int y) {
        super(estrategia, 5, tNormal, tDebil, x, y, 60);
    }

    @Override
    public void realizarComportamientoEspecifico() {
        // COMPORTAMIENTO: "Vibración Nerviosa"
        // Rota aleatoriamente entre -5 y +5 grados en cada frame.
        // Esto da un efecto visual de que la nave tiembla o es inestable.
        float inestabilidad = 5f;
        spr.setRotation(MathUtils.random(-inestabilidad, inestabilidad));
    }
}

