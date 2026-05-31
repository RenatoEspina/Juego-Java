package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public class NaveEGrande extends NaveBase {
    public NaveEGrande(float x, float y, Texture tNave, Texture tDebil, Sound sHerido, Texture tBala, Sound sBala) {
        super(6, tNave, tDebil, tBala, sHerido, sBala, x, y); // 6 Vidas (Tanque)
        this.MAX_SPEED = 300f;      // Lenta
        this.ACCELERATION = 800f;   // Le cuesta arrancar
        this.shootCooldownMax = 0.8f; // Dispara lento
        this.spr.setSize(90, 110);   // Grande
    }

    @Override
    public BalaBase disparar() {
        // 1. Calcular punto de salida
        float bx = spr.getX() + spr.getWidth() / 2 - 15;
        float by = spr.getY() + spr.getHeight() / 2 - 15;

        // 2. Calcular ángulo
        float anguloGrados = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        // 3. Calcular velocidad (Lenta pero fuerte)
        float velocidadBala = 250f;
        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        // 4. Instanciar y rotar
        BalaBase bala = new BalaEGrande(bx, by, vx, vy, texturaBala);
        bala.spr.setRotation(spr.getRotation()); // Asegúrate que 'spr' sea público/protected en BalaBase

        shootCooldown = shootCooldownMax;
        return bala;
    }

    @Override
    public void actualizarSprite() {
        // Efecto de Pulso de Color
        float tiempo = Gdx.graphics.getFrameId() / 60f; // Tiempo basado en frames
        // Oscila el color entre gris claro y blanco
        float color = 0.8f + 0.2f * (float)Math.sin(tiempo * 5);

        spr.setColor(color, color, color, 1f);
    }
}
