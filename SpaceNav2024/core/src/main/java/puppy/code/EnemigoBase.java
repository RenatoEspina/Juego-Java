package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class EnemigoBase implements Atacable, Actualizable {
    protected int vidas;
    protected int vidaMaxima;
    protected Sprite spr;
    protected boolean debil = false;

    // PATRÓN STRATEGY
    protected EstrategiaMovimiento estrategia;

    // Texturas
    protected Texture texNormal;
    protected Texture texDebil;

    public EnemigoBase(EstrategiaMovimiento estrategia, int vidas, Texture tNormal, Texture tDebil, int x, int y, int size) {
        this.estrategia = estrategia;
        this.vidas = vidas;
        this.vidaMaxima = vidas;
        this.texNormal = tNormal;
        this.texDebil = tDebil;

        this.spr = new Sprite(tNormal);
        this.spr.setSize(size, size);
        this.spr.setOriginCenter();
        this.spr.setPosition(x, y);
    }

    // --- TEMPLATE METHOD ---
    @Override
    public final void update() {
        float delta = Gdx.graphics.getDeltaTime();
        if (estrategia != null) {
            estrategia.mover(spr, delta);
        }
        gestionarCambioTextura();
        realizarComportamientoEspecifico();
    }

    private void gestionarCambioTextura() {
        float porcentaje = (float) vidas / vidaMaxima;
        if (porcentaje <= 0.25f && !debil) {
            if (texDebil != null) spr.setTexture(texDebil);
            debil = true;
        } else if (porcentaje > 0.25f && debil) {
            if (texNormal != null) spr.setTexture(texNormal);
            debil = false;
        }
    }

    public abstract void realizarComportamientoEspecifico();

    // --- COLISIÓN ENTRE ENEMIGOS ---
    public void checkCollision(EnemigoBase otro) {
        if (spr.getBoundingRectangle().overlaps(otro.getArea())) {
            int miVX = this.getXSpeed();
            int miVY = this.getYSpeed();
            int otroVX = otro.getXSpeed();
            int otroVY = otro.getYSpeed();

            if (miVX == 0) miVX += otroVX / 2;
            if (otroVX == 0) otroVX += miVX / 2;
            this.setXSpeed(-miVX);
            otro.setXSpeed(-otroVX);

            if (miVY == 0) miVY += otroVY / 2;
            if (otroVY == 0) otroVY += miVY / 2;
            this.setYSpeed(-miVY);
            otro.setYSpeed(-otroVY);
        }
    }

    // Estos métodos sacan la velocidad desde dentro de la estrategia
    public int getXSpeed() {
        if (estrategia instanceof MovimientoRebote) {
            return (int) ((MovimientoRebote) estrategia).getXSpeed();
        }
        return 0;
    }

    public int getYSpeed() {
        if (estrategia instanceof MovimientoRebote) {
            return (int) ((MovimientoRebote) estrategia).getYSpeed();
        }
        return 0;
    }

    public void setXSpeed(int x) {
        if (estrategia instanceof MovimientoRebote) {
            ((MovimientoRebote) estrategia).setXSpeed(x);
        }
    }

    public void setYSpeed(int y) {
        if (estrategia instanceof MovimientoRebote) {
            ((MovimientoRebote) estrategia).setYSpeed(y);
        }
    }
    public void draw(SpriteBatch batch) { spr.draw(batch); }
    public Rectangle getArea() { return spr.getBoundingRectangle(); }
    @Override public void recibirDano(int cantidad) { vidas -= cantidad; }
    @Override public boolean estaDestruido() { return vidas <= 0; }
    @Override public void actualizarSprite() {}
    @Override public boolean conectar(Atacable otro) { return false; }

    public int getVidas() { return vidas; }
    public int getVidaMaxima() { return vidaMaxima; }
    public void setVidas(int v) { vidas = v; }
    public float getX() { return spr.getX(); }
    public float getY() { return spr.getY(); }
}
