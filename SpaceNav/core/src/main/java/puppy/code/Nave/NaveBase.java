package puppy.code.Nave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import puppy.code.Enemigo.*;
import puppy.code.Pantalla.*;
import puppy.code.Bala.*;

public abstract class NaveBase implements Atacable, Actualizable {
    protected int vidas;
    protected Sprite spr;
    protected Texture texturaNave;
    protected Texture texturaNaveDebil;
    protected Texture texturaBala;
    protected Sound sonidoHerido;
    protected Sound sonidoBala;
    protected PantallaJuego juego;

    protected boolean transformada = false;
    protected boolean destruida = false;
    protected boolean herido = false;
    protected int tiempoHerido = 0;
    protected int tiempoHeridoMax = 50;

    protected float xVel = 0f;
    protected float yVel = 0f;
    protected float MAX_SPEED = 500f;
    protected float ACCELERATION = 1500f;
    protected float FRICTION = 800f;
    protected float shootCooldown = 0f;
    protected float shootCooldownMax = 0.25f;
    protected int lastFacingDir = 1;

    public boolean estaTransformada() {
        return transformada;
    }

    public NaveBase(int vidas, Texture texturaNave, Texture texturaNaveDebil, Texture texturaBala,
                    Sound sonidoHerido, Sound sonidoBala, float x, float y) {
        this.spr = new Sprite(texturaNave);
        this.vidas = vidas;
        this.texturaNave = texturaNave;
        this.texturaNaveDebil = texturaNaveDebil;
        this.texturaBala = texturaBala;
        this.sonidoHerido = sonidoHerido;
        this.sonidoBala = sonidoBala;
        this.spr = new Sprite(texturaNave);
        spr.setSize(70, 90);
        spr.setOriginCenter();
        spr.setPosition(x, y);
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        if (shootCooldown > 0f) shootCooldown -= delta;

        if (!herido) {
            float ax = 0f, ay = 0f;

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)  || Gdx.input.isKeyPressed(Input.Keys.A)) ax = -1f;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) ax =  1f;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)    || Gdx.input.isKeyPressed(Input.Keys.W)) ay =  1f;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)  || Gdx.input.isKeyPressed(Input.Keys.S)) ay = -1f;

            if (ax != 0f && ay != 0f) {
                float inv = 1f / (float) Math.sqrt(2.0);
                ax *= inv;
                ay *= inv;
            }

            xVel += ax * ACCELERATION * delta;
            yVel += ay * ACCELERATION * delta;

            if (ax == 0f) xVel = aplicarFriccion(xVel, delta);
            if (ay == 0f) yVel = aplicarFriccion(yVel, delta);

            float speed = (float) Math.sqrt(xVel * xVel + yVel * yVel);
            if (speed > MAX_SPEED) {
                float scale = MAX_SPEED / speed;
                xVel *= scale;
                yVel *= scale;
            }

            float newX = spr.getX() + xVel * delta;
            float newY = spr.getY() + yVel * delta;
            newX = MathUtils.clamp(newX, 0, Gdx.graphics.getWidth()  - spr.getWidth());
            newY = MathUtils.clamp(newY, 0, Gdx.graphics.getHeight() - spr.getHeight());

            spr.setPosition(newX, newY);
            actualizarRotacion();

            boolean disparar = Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                            || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT);
            if (disparar && shootCooldown <= 0f) {
                BalaBase bala = disparar();
                if (bala != null && juego != null) {
                    juego.agregarBala(bala);
                    if (sonidoBala != null) sonidoBala.play();
                }
            }
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        aplicarTexturaSegunVida();
        actualizarSprite();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean checkCollision(EnemigoBase enemigo) {
        if (!herido && enemigo.getArea().overlaps(spr.getBoundingRectangle())) {
            if (estaTransformada()) {
                return true;
            }

            if (xVel == 0) xVel += enemigo.getXSpeed() / 2f;
            if (enemigo.getXSpeed() == 0) enemigo.setXSpeed(enemigo.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            enemigo.setXSpeed(-enemigo.getXSpeed());

            if (yVel == 0) yVel += enemigo.getYSpeed() / 2f;
            if (enemigo.getYSpeed() == 0) enemigo.setYSpeed(enemigo.getYSpeed() + (int) yVel / 2);
            yVel = -yVel;
            enemigo.setYSpeed(-enemigo.getYSpeed());

            vidas--;
            if (sonidoHerido != null) {
                long id = sonidoHerido.play(1.0f);
                sonidoHerido.setPitch(id, 0.5f);
            }
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean conectar(Atacable otro) {
        if (otro instanceof EnemigoBase && juego != null) {
            EnemigoBase enemigo = (EnemigoBase) otro;
            float porcentaje = (float) enemigo.getVidas() / Math.max(1, enemigo.getVidaMaxima());

            if (porcentaje <= 0.25f) {
                Sound sH = juego.getSonidoHerido();
                Sound sB = juego.getSonidoBala();
                NaveBase nueva = null;

                if (enemigo instanceof EnemigoChico) {
                    nueva = new NaveEChico(spr.getX(), spr.getY(),
                        juego.getTexturaNaveEChico(), juego.getTexturaNaveEChicoDebil(),
                        sH, juego.getTexturaBalaEChico(), sB);
                } else if (enemigo instanceof EnemigoGrande) {
                    nueva = new NaveEGrande(spr.getX(), spr.getY(),
                        juego.getTexturaNaveEGrande(), juego.getTexturaNaveEGrandeDebil(),
                        sH, juego.getTexturaBalaEGrande(), sB);
                } else if (enemigo instanceof EnemigoNormal) {
                    nueva = new NaveENormal(spr.getX(), spr.getY(),
                        juego.getTexturaNaveENormal(), juego.getTexturaNaveENormalDebil(),
                        sH, juego.getTexturaBalaENormal(), sB);
                }

                if (nueva != null) {
                    nueva.setVidas(this.vidas + 2);
                    nueva.setJuego(juego);
                    juego.transformarEn(nueva);
                    this.transformada = true;
                    return true;
                }
            }
        }
        return false;
    }

    public BalaBase disparar() {
        float bx = spr.getX() + spr.getWidth()  / 2 - 5;
        float by = spr.getY() + spr.getHeight() / 2 - 5;

        float anguloGrados   = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        float velocidadBala = 400f;
        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        BalaDefault bala = new BalaDefault(bx, by, vx, vy, texturaBala);
        bala.setRotation(spr.getRotation());

        shootCooldown = shootCooldownMax;
        return bala;
    }

    private float aplicarFriccion(float vel, float delta) {
        if (vel > 0f)      { vel -= FRICTION * delta; if (vel < 0f) vel = 0f; }
        else if (vel < 0f) { vel += FRICTION * delta; if (vel > 0f) vel = 0f; }
        return vel;
    }

    private void actualizarRotacion() {
        float eps = 2f;
        if (Math.abs(xVel) > eps || Math.abs(yVel) > eps) {
            float ang = (float) Math.toDegrees(Math.atan2(yVel, xVel));
            spr.setRotation(ang - 90f);
        }
    }

    protected void aplicarTexturaSegunVida() {
        if (texturaNaveDebil == null) return;
        float p = (float) vidas / Math.max(1, getVidaMaximaEstimada());
        if (p <= 0.25f) spr.setRegion(texturaNaveDebil);
        else            spr.setRegion(texturaNave);
    }

    protected int getVidaMaximaEstimada() { return Math.max(vidas, 10); }

    public void setJuego(PantallaJuego juego) { this.juego = juego; }
    public Rectangle getArea()                { return spr.getBoundingRectangle(); }
    public void setVidas(int v)               { vidas = v; }
    public int  getVidas()                    { return vidas; }
    @Override public void recibirDano(int cantidad) { vidas -= cantidad; }
    @Override public boolean estaDestruido()        { return !herido && destruida; }
    public boolean estaHerido()                     { return herido; }
    @Override public abstract void actualizarSprite();
}