package puppy.code.Nave;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

import puppy.code.Bala.*;

public class NaveENormal extends NaveBase {

    public NaveENormal(float x, float y, Texture texturaNave, Texture texturaNaveDebil, Sound sonidoHerido, Texture texturaBala, Sound sonidoBala) {
        super(3, texturaNave, texturaNaveDebil, texturaBala, sonidoHerido, sonidoBala, x, y);
        this.MAX_SPEED = 600f;
        this.ACCELERATION = 1800f;
        this.FRICTION = 1000f;
        this.shootCooldownMax = 0.2f;
        this.spr.setSize(90, 110);
    }

    @Override
    public BalaBase disparar() {
        float bx = spr.getX() + spr.getWidth() / 2 - 7;
        float by = spr.getY() + spr.getHeight() / 2 - 7;

        float anguloGrados = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        float velocidadBala = 450f;
        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        BalaBase bala = new BalaENormal(bx, by, vx, vy, texturaBala);
        bala.setRotation(spr.getRotation());

        shootCooldown = shootCooldownMax;
        return bala;
    }

    @Override
    public void actualizarSprite() {
        if (xVel > 50) {
            spr.setRotation(-15);
        } else if (xVel < -50) {
            spr.setRotation(15);
        } else {
            spr.setRotation(0);
        }
    }
}