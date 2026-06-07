package puppy.code.Nave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

import puppy.code.Bala.*;

public class NaveEGrande extends NaveBase {

    public NaveEGrande(float x, float y, Texture tNave, Texture tDebil, Sound sHerido, Texture tBala, Sound sBala) {
        super(6, tNave, tDebil, tBala, sHerido, sBala, x, y);
        this.MAX_SPEED = 300f;
        this.ACCELERATION = 800f;
        this.shootCooldownMax = 0.8f;
        this.spr.setSize(90, 110);
    }

    @Override
    public BalaBase disparar() {
        float bx = spr.getX() + spr.getWidth() / 2 - 15;
        float by = spr.getY() + spr.getHeight() / 2 - 15;

        float anguloGrados = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        float velocidadBala = 250f;
        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        BalaBase bala = new BalaEGrande(bx, by, vx, vy, texturaBala);
        bala.setRotation(spr.getRotation()); // encapsulamiento correcto

        shootCooldown = shootCooldownMax;
        return bala;
    }

    @Override
    public void actualizarSprite() {
        float tiempo = Gdx.graphics.getFrameId() / 60f;
        float color = 0.8f + 0.2f * (float) Math.sin(tiempo * 5);
        spr.setColor(color, color, color, 1f);
    }
}