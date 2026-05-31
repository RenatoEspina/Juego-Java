package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class NaveBase implements Atacable, Actualizable {
    // --- VARIABLES ---
    protected int vidas;
    protected Sprite spr;
    protected Texture texturaNave;
    protected Texture texturaNaveDebil;
    protected Texture texturaBala;
    protected Sound sonidoHerido;
    protected Sound sonidoBala;
    protected PantallaJuego juego;

    // ¡ASEGÚRATE DE QUE ESTA VARIABLE ESTÉ AQUÍ!
    protected boolean transformada = false;

    protected boolean destruida = false;
    protected boolean herido = false;
    protected int tiempoHerido = 0;
    protected int tiempoHeridoMax = 50;

    protected float xVel = 0f;
    protected float yVel = 0f;
    protected float MAX_SPEED = 500f;
    protected float ACCELERATION = 1500f;
    protected float FRICTION = 1200f;
    protected float shootCooldown = 0f;
    protected float shootCooldownMax = 0.25f;
    protected int lastFacingDir = 1;

    // --- GETTER DEL ERROR ---
    public boolean estaTransformada() {
        return transformada;
    }

    public NaveBase(int vidas, Texture texturaNave, Texture texturaNaveDebil, Texture texturaBala, Sound sonidoHerido, Sound sonidoBala, float x, float y) {
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

    // --- LÓGICA DE JUEGO (Update) ---
    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        if (shootCooldown > 0f) shootCooldown -= delta;

        if (!herido) {
            // Input y Movimiento
            float ax = 0f, ay = 0f;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) ax = -1f;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ax = 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) ay = 1f;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) ay = -1f;

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
            newX = MathUtils.clamp(newX, 0, Gdx.graphics.getWidth() - spr.getWidth());
            newY = MathUtils.clamp(newY, 0, Gdx.graphics.getHeight() - spr.getHeight());

            spr.setPosition(newX, newY);
            actualizarRotacion();

            // Disparo
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && shootCooldown <= 0f) {
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
        actualizarSprite(); // Hook visual
    }

    // --- RENDERIZADO (Draw) ---
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    // --- COLISIÓN CON ENEMIGOS ---
    public boolean checkCollision(EnemigoBase enemigo) {
        if (!herido && enemigo.getArea().overlaps(spr.getBoundingRectangle())) {
            // AQUÍ SE USA EL MÉTODO QUE DABA ERROR
            if (estaTransformada()) {
                return true;
            }

            // Accedemos a la velocidad del enemigo mediante sus getters puente
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
                sonidoHerido.setPitch(id, 0.5f); // Pitch normal
            }
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            if (sonidoHerido != null) sonidoHerido.play();
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }

    // --- FUSIÓN (Conectar) ---
    @Override
    public boolean conectar(Atacable otro) {
        if (otro instanceof EnemigoBase && juego != null) {
            EnemigoBase enemigo = (EnemigoBase) otro;
            float porcentaje = (float) enemigo.getVidas() / Math.max(1, enemigo.getVidaMaxima());

            if (porcentaje <= 0.25f) {
                // Recursos comunes
                Sound sH = juego.getSonidoHerido();
                Sound sB = juego.getSonidoBala();

                NaveBase nueva = null;


                if (enemigo instanceof EnemigoChico) {
                    nueva = new NaveEChico(
                        spr.getX(), spr.getY(),
                        juego.getTexturaNaveEChico(),
                        juego.getTexturaNaveEChicoDebil(),
                        sH,
                        juego.getTexturaBalaEChico(),
                        sB
                    );
                }

                else if (enemigo instanceof EnemigoGrande) {
                    nueva = new NaveEGrande(
                        spr.getX(), spr.getY(),
                        juego.getTexturaNaveEGrande(),
                        juego.getTexturaNaveEGrandeDebil(),
                        sH,
                        juego.getTexturaBalaEGrande(),
                        sB
                    );
                }

                else if (enemigo instanceof EnemigoNormal) {
                    nueva = new NaveENormal(
                        spr.getX(), spr.getY(),
                        juego.getTexturaNaveENormal(),
                        juego.getTexturaNaveENormalDebil(),
                        sH,
                        juego.getTexturaBalaENormal(),
                        sB
                    );
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

    // --- UTILIDADES ---
    public BalaBase disparar() {
        float bx = spr.getX() + spr.getWidth() / 2 - 5;
        float by = spr.getY() + spr.getHeight() / 2 - 5;

        // 1. Obtener el ángulo de la nave
        // Sumamos 90 porque en LibGDX 0 grados es "Derecha", pero tu sprite mira "Arriba"
        float anguloGrados = spr.getRotation() + 90;
        float anguloRadianes = (float) Math.toRadians(anguloGrados);

        // 2. Calcular velocidad basada en el ángulo (Trigonometría básica)
        float velocidadBala = 400f; // Rapidez de la bala
        float vx = (float) Math.cos(anguloRadianes) * velocidadBala;
        float vy = (float) Math.sin(anguloRadianes) * velocidadBala;

        // 3. Crear la bala pasándole la velocidad calculada
        BalaDefault bala = new BalaDefault(bx, by, vx, vy, texturaBala);

        // 4. Rotar la bala para que coincida visualmente con la dirección
        // Accedemos al sprite de la bala (asegúrate que 'spr' sea protected en BalaBase o usa un getter)
        bala.spr.setRotation(spr.getRotation());

        shootCooldown = shootCooldownMax;
        return bala;
    }

    private float aplicarFriccion(float vel, float delta) {
        if (vel > 0f) { vel -= FRICTION * delta; if (vel < 0f) vel = 0f; }
        else if (vel < 0f) { vel += FRICTION * delta; if (vel > 0f) vel = 0f; }
        return vel;
    }
    private void actualizarRotacion() {
        float vx = xVel;
        float vy = yVel;
        float eps = 5f;

        // SOLO cambiamos la rotación si la nave tiene velocidad significativa
        if (Math.abs(vx) > eps || Math.abs(vy) > eps) {
            // Calculamos el ángulo matemático (atan2 devuelve ángulo donde 0 es Derecha)
            float ang = (float) Math.toDegrees(Math.atan2(vy, vx));

            // Restamos 90 grados porque tu sprite original mira hacia ARRIBA
            spr.setRotation(ang - 90f);
        }

        // ELIMINADO: El bloque 'else'.
        // Al no hacer nada cuando está quieta, el Sprite conserva
        // automáticamente la última rotación que tenía.
    }
    protected void aplicarTexturaSegunVida() {
        if (texturaNaveDebil == null) return;
        float p = (float) vidas / Math.max(1, getVidaMaximaEstimada());
        if (p <= 0.25f) spr.setRegion(texturaNaveDebil);
        else spr.setRegion(texturaNave);
    }
    protected int getVidaMaximaEstimada() { return Math.max(vidas, 10); }

    public void setJuego(PantallaJuego juego) { this.juego = juego; }
    public Rectangle getArea() { return spr.getBoundingRectangle(); }
    public void setVidas(int v) { vidas = v; }
    public int getVidas() { return vidas; }
    @Override public void recibirDano(int cantidad) { vidas -= cantidad; }
    @Override public boolean estaDestruido() { return !herido && destruida; }
    public boolean estaHerido() { return herido; }
    @Override public abstract void actualizarSprite();
}
