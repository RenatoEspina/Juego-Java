package puppy.code.Enemigo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.MovementStrategy.EstrategiaMovimiento;

public class EnemigoNormal extends EnemigoBase {

    public EnemigoNormal(EstrategiaMovimiento estrategia, Texture tNormal, Texture tDebil, int x, int y) {
        // Tamaño 100 (Mediano)
        super(estrategia, 10, tNormal, tDebil, x, y, 100);
    }

    @Override
    public void realizarComportamientoEspecifico() {
        float velocidadRotacion = 90f;
        spr.rotate(velocidadRotacion * Gdx.graphics.getDeltaTime());
    }
}
