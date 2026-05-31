package puppy.code.Bala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import puppy.code.MovementStrategy.*;
import puppy.code.Enemigo.*;

public abstract class BalaBase {

    private Sprite spr; // privado: encapsulamiento correcto
    protected int dano;
    protected boolean destroyed = false;
    protected EstrategiaMovimiento estrategia;

    public BalaBase(float x, float y, int w, int h, int dano, Texture tx, EstrategiaMovimiento estrategia) {
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.spr.setSize(w, h);
        this.spr.setOriginCenter();
        this.dano = dano;
        this.estrategia = estrategia;
    }

    // Método público para que NaveBase y subclases puedan rotar la bala
    // sin acceder directamente al Sprite interno.
    public void setRotation(float rotation) {
        spr.setRotation(rotation);
    }

    public void update() {
        if (estrategia != null) {
            estrategia.mover(spr, Gdx.graphics.getDeltaTime());
        }
        if (spr.getY() > Gdx.graphics.getHeight() || spr.getY() < 0 ||
            spr.getX() > Gdx.graphics.getWidth() || spr.getX() < 0) {
            destroyed = true;
        }
    }

    public void draw(SpriteBatch batch) { spr.draw(batch); }

    public boolean checkCollision(Atacable enemigo) {
        if (spr.getBoundingRectangle().overlaps(((EnemigoBase) enemigo).getArea())) {
            enemigo.recibirDano(this.dano);
            this.destroyed = true;
            return true;
        }
        return false;
    }

    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean d) { destroyed = d; }
}