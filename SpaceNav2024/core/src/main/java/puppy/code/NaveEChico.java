package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class NaveEChico extends NaveBase {
    public NaveEChico(float x, float y, Texture tNave, Texture tDebil, Sound sHerido, Texture tBala, Sound sBala) {
        super(2, tNave, tDebil, tBala, sHerido, sBala, x, y); // 2 Vidas (Frágil)
        this.MAX_SPEED = 700f;      // Muy rápida
        this.ACCELERATION = 2000f;
        this.shootCooldownMax = 0.1f; // Dispara muy rápido (Ametralladora)
        this.spr.setSize(70, 90);   // Pequeña
    }

    @Override
    public BalaBase disparar() {
        // 1. Calcular centro de salida
        float bx = spr.getX() + spr.getWidth() / 2 - 4;
        float by = spr.getY() + spr.getHeight() / 2 - 4;

        // 2. Calcular ángulo y velocidad (Igual que en NaveBase)
        float anguloGrados = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        // Velocidad específica de la bala chica (Muy rápida)
        float velocidadBala = 700f;

        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        // 3. Instanciar la bala pasando las velocidades
        BalaBase bala = new BalaEChico(bx, by, vx, vy, texturaBala);

        // 4. Rotar visualmente la bala
        bala.spr.setRotation(spr.getRotation());

        shootCooldown = shootCooldownMax;
        return bala;
    }

    @Override
    public void actualizarSprite() {
        // Efecto de Vibración: Da sensación de energía inestable
        if (!herido) { // Solo si no está herida (porque herida ya tiembla)
            float jitterX = MathUtils.random(-1f, 1f);
            float jitterY = MathUtils.random(-1f, 1f);
            // Modificamos ligeramente la posición visual sin cambiar la lógica (xVel/yVel)
            spr.setPosition(spr.getX() + jitterX, spr.getY() + jitterY);
        }
    }
}
