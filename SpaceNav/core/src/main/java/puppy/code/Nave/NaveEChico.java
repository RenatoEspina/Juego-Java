package puppy.code.Nave;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import puppy.code.Bala.*;

public class NaveEChico extends NaveBase {

    public NaveEChico(float x, float y, Texture tNave, Texture tDebil, Sound sHerido, Texture tBala, Sound sBala) {
        super(2, tNave, tDebil, tBala, sHerido, sBala, x, y);
        this.MAX_SPEED = 700f;
        this.ACCELERATION = 2000f;
        this.shootCooldownMax = 0.1f;
        this.spr.setSize(70, 90);
    }

    @Override
    public BalaBase disparar() {
        float bx = spr.getX() + spr.getWidth() / 2 - 4;
        float by = spr.getY() + spr.getHeight() / 2 - 4;

        float anguloGrados = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        float velocidadBala = 700f;
        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        BalaBase bala = new BalaEChico(bx, by, vx, vy, texturaBala);
        bala.setRotation(spr.getRotation());

        shootCooldown = shootCooldownMax;
        return bala;
    }

    @Override
    public void actualizarSprite() {
        if (!herido) {
            float jitterX = MathUtils.random(-1f, 1f);
            float jitterY = MathUtils.random(-1f, 1f);
            spr.setPosition(spr.getX() + jitterX, spr.getY() + jitterY);
        }
    }
}