package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public class NaveENormal extends NaveBase {

    public NaveENormal(float x, float y, Texture texturaNave, Texture texturaNaveDebil, Sound sonidoHerido, Texture texturaBala, Sound sonidoBala) {
        super(3, texturaNave, texturaNaveDebil, texturaBala, sonidoHerido, sonidoBala, x, y);
        this.MAX_SPEED = 600f;         // más rápida
        this.ACCELERATION = 1800f;     // acelera más fuerte
        this.FRICTION = 1000f;         // se desliza más
        this.shootCooldownMax = 0.2f;  // dispara más seguido
        this.spr.setSize(90, 110);      // visualmente más grande
    }
    @Override
    public BalaBase disparar() {
        // 1. Calcular punto de salida
        float bx = spr.getX() + spr.getWidth() / 2 - 7;
        float by = spr.getY() + spr.getHeight() / 2 - 7;

        // 2. Calcular ángulo
        float anguloGrados = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        // 3. Calcular velocidad (Velocidad media)
        float velocidadBala = 450f;
        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        // 4. Instanciar y rotar
        BalaBase bala = new BalaENormal(bx, by, vx, vy, texturaBala);
        bala.spr.setRotation(spr.getRotation());

        shootCooldown = shootCooldownMax;
        return bala;
    }
    @Override
    public void actualizarSprite() {
        // Inclinación exagerada al moverse
        if (xVel > 50) {
            spr.setRotation(-15); // Se inclina a la derecha
        } else if (xVel < -50) {
            spr.setRotation(15);  // Se inclina a la izquierda
        } else {
            spr.setRotation(0);
        }
    }
}
