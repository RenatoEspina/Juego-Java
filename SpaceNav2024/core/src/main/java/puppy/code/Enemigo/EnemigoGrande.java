package puppy.code.Enemigo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import puppy.code.MovementStrategy.EstrategiaMovimiento;

public class EnemigoGrande extends EnemigoBase {
    private float tiempoVida = 0;
    public EnemigoGrande(EstrategiaMovimiento estrategia, Texture tNormal, Texture tDebil, int x, int y) {
        super(estrategia, 15, tNormal, tDebil, x, y, 180);
    }

    @Override
    public void realizarComportamientoEspecifico() {
        tiempoVida += Gdx.graphics.getDeltaTime();
        float escala = 1.0f + 0.1f * MathUtils.sin(tiempoVida * 3.0f);
        spr.setScale(escala);
        spr.rotate(10 * Gdx.graphics.getDeltaTime());
    }
}


