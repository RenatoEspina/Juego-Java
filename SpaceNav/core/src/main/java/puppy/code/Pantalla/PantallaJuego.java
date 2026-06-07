package puppy.code.Pantalla;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.GerentePuntuacion;
import puppy.code.SpaceNavigation;
import puppy.code.Bala.BalaBase;
import puppy.code.Enemigo.EnemigoBase;
import puppy.code.Enemigo.EnemigoJefe;
import puppy.code.Factory.FabricaEnemigos;
import puppy.code.Factory.FabricaNivelDificil;
import puppy.code.Factory.FabricaNivelFacil;
import puppy.code.Menu.MenuPausa;
import puppy.code.Nave.NaveBase;
import puppy.code.Nave.NaveDefault;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;

    // Variables de juego
    private int ronda;
    private int velXEnemigos;
    private int velYEnemigos;
    private int cantEnemigos;

    // Entidades
    private NaveBase nave;
    private ArrayList<EnemigoBase> enemigos = new ArrayList<>();
    private ArrayList<BalaBase> balas = new ArrayList<>();

    // PATRÓN ABSTRACT FACTORY
    private FabricaEnemigos fabrica;

    // Menú de pausa
    private MenuPausa menuPausa;

    // --- RECURSOS GRÁFICOS ---
    private Texture fondoGameplay;

    // Texturas Nave y Balas Jugador
    private Texture texturaNaveDefault, texturaNaveDefaultDebil, texturaBalaDefault;
    private Texture texturaNaveENormal, texturaNaveENormalDebil, texturaBalaENormal;
    private Texture texturaNaveEChico, texturaNaveEChicoDebil, texturaBalaEChico;
    private Texture texturaNaveEGrande, texturaNaveEGrandeDebil, texturaBalaEGrande;

    // Texturas Enemigos
    private Texture texEnemigoNormal, texEnemigoNormalDebil;
    private Texture texEnemigoGrande, texEnemigoGrandeDebil;
    private Texture texEnemigoChico, texEnemigoChicoDebil;
    private Texture texEnemigoJefe, texEnemigoJefeDebil;

    // Audio
    private Sound sonidoHerido;
    private Sound sonidoBala;

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas,
                         int velXEnemigos, int velYEnemigos, int cantEnemigos) {
        this.game = game;
        this.ronda = ronda;
        this.velXEnemigos = velXEnemigos;
        this.velYEnemigos = velYEnemigos;
        this.cantEnemigos = cantEnemigos;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        // --- CARGA DE AUDIOS ---
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        sonidoHerido   = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        sonidoBala     = Gdx.audio.newSound(Gdx.files.internal("disparo.ogg"));

        if (ronda == 6) {
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("jefe-final.wav"));
            gameMusic.setVolume(0.5f);
        } else {
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
            gameMusic.setVolume(0.3f);
        }
        gameMusic.setLooping(true);
        gameMusic.play();

        // --- CARGA DE TEXTURAS ---
        fondoGameplay = new Texture(Gdx.files.internal("fondoGameplay.png"));

        texturaNaveDefault      = new Texture(Gdx.files.internal("NaveDefault.png"));
        texturaNaveDefaultDebil = new Texture(Gdx.files.internal("NaveDefaultDebil.png"));
        texturaBalaDefault      = new Texture(Gdx.files.internal("BulletNormal.png"));

        texturaNaveENormal      = new Texture(Gdx.files.internal("NaveENormal.png"));
        texturaNaveENormalDebil = new Texture(Gdx.files.internal("NaveENormalDebil.png"));
        texturaBalaENormal      = new Texture(Gdx.files.internal("BulletEnemigoNormal.png"));

        texturaNaveEChico       = new Texture(Gdx.files.internal("NaveEChico.png"));
        texturaNaveEChicoDebil  = new Texture(Gdx.files.internal("NaveEChicoDebil.png"));
        texturaBalaEChico       = new Texture(Gdx.files.internal("BulletChica.png"));

        texturaNaveEGrande      = new Texture(Gdx.files.internal("NaveEGrande.png"));
        texturaNaveEGrandeDebil = new Texture(Gdx.files.internal("NaveEGrandeDebil.png"));
        texturaBalaEGrande      = new Texture(Gdx.files.internal("BulletEGrande.png"));

        texEnemigoNormal      = new Texture(Gdx.files.internal("enemigoNormal.png"));
        texEnemigoNormalDebil = new Texture(Gdx.files.internal("enemigoNormalDebil.png"));
        texEnemigoGrande      = new Texture(Gdx.files.internal("enemigoGrande.png"));
        texEnemigoGrandeDebil = new Texture(Gdx.files.internal("enemigoGrandeDebil.png"));
        texEnemigoChico       = new Texture(Gdx.files.internal("enemigoChico.png"));
        texEnemigoChicoDebil  = new Texture(Gdx.files.internal("enemigoChicoDebil.png"));
        texEnemigoJefe        = new Texture(Gdx.files.internal("enemigoJefe.png"));
        texEnemigoJefeDebil   = new Texture(Gdx.files.internal("enemigoJefeDebil.png"));

        // --- CREAR NAVE ---
        nave = new NaveDefault(
            Gdx.graphics.getWidth() / 2f - 40, 50,
            texturaNaveDefault, texturaNaveDefaultDebil,
            sonidoHerido, texturaBalaDefault, sonidoBala
        );
        nave.setVidas(vidas);
        nave.setJuego(this);

        iniciarNivel();

        // --- MENÚ DE PAUSA ---
        menuPausa = new MenuPausa(
            game.getFont(),
            camera,
            () -> { /* reanudar: no hace nada extra, el flag ya se gestiona internamente */ },
            () -> {
                // Reiniciar
                gameMusic.stop();
                GerentePuntuacion.getInstance().resetScore();
                game.setScreen(new PantallaJuego(game, 1, 3, 1, 1, 6));
                dispose();
            },
            () -> {
                // Menú principal
                gameMusic.stop();
                game.setScreen(new PantallaMenuPrincipal(game));
                dispose();
            }
        );
    }

    private void iniciarNivel() {
        if (ronda <= 3) {
            fabrica = new FabricaNivelFacil(
                texEnemigoNormal, texEnemigoNormalDebil,
                texEnemigoGrande, texEnemigoGrandeDebil,
                texEnemigoChico,  texEnemigoChicoDebil,
                texEnemigoJefe,   texEnemigoJefeDebil
            );
        } else {
            fabrica = new FabricaNivelDificil(
                texEnemigoNormal, texEnemigoNormalDebil,
                texEnemigoGrande, texEnemigoGrandeDebil,
                texEnemigoChico,  texEnemigoChicoDebil,
                texEnemigoJefe,   texEnemigoJefeDebil
            );
        }

        Random r = new Random();

        if (ronda == 6) {
            float bossX = Gdx.graphics.getWidth()  / 2f - 175;
            float bossY = Gdx.graphics.getHeight() / 2f - 175;
            enemigos.add(fabrica.crearEnemigoJefe(bossX, bossY, this));
        } else {
            for (int i = 0; i < cantEnemigos; i++) {
                float x = r.nextInt(Gdx.graphics.getWidth() - 100);
                float y = 100 + r.nextInt(Gdx.graphics.getHeight() - 200);
                EnemigoBase nuevo = null;

                if (ronda == 1)      nuevo = fabrica.crearEnemigoChico(x, y);
                else if (ronda == 2) nuevo = fabrica.crearEnemigoNormal(x, y);
                else if (ronda == 3) nuevo = fabrica.crearEnemigoGrande(x, y);
                else                 nuevo = fabrica.crearEnemigoAleatorio(x, y);

                if (nuevo != null) enemigos.add(nuevo);
            }
        }
    }

    public void agregarEnemigo(EnemigoBase enemigo) { this.enemigos.add(enemigo); }
    public boolean agregarBala(BalaBase bb)          { return balas.add(bb); }

    public void dibujaEncabezado() {
        int puntajeActual = GerentePuntuacion.getInstance().getScoreActual();
        int record        = GerentePuntuacion.getInstance().getHighScore();

        game.getFont().getData().setScale(4f);
        float alto = camera.viewportHeight - 50;

        game.getFont().draw(batch, "Vidas: " + nave.getVidas() + " | Ronda: " + ronda, 30, alto);
        game.getFont().draw(batch, "Score: " + puntajeActual, camera.viewportWidth - 400, alto);
        game.getFont().draw(batch, "High: " + record, camera.viewportWidth / 2f - 150, alto);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Actualizar menú de pausa primero (detecta ESC)
        menuPausa.update();

        batch.begin();

        batch.draw(fondoGameplay, 0, 0, camera.viewportWidth, camera.viewportHeight);

        if (!menuPausa.isVisible()) {
            // ---- LÓGICA DE JUEGO ACTIVA ----

            // 1. BALAS
            for (int i = 0; i < balas.size(); i++) {
                BalaBase b = balas.get(i);
                b.update();
                b.draw(batch);

                for (int j = 0; j < enemigos.size(); j++) {
                    EnemigoBase e = enemigos.get(j);
                    if (b.checkCollision(e)) {
                        e.recibirDano(1);
                        if (e.estaDestruido()) {
                            explosionSound.play();
                            enemigos.remove(j);
                            j--;
                            GerentePuntuacion.getInstance().agregarPuntaje(10);
                        }
                        b.setDestroyed(true);
                    }
                }
                if (b.isDestroyed()) { balas.remove(i); i--; }
            }

            // 2. ENEMIGOS
            for (int i = 0; i < enemigos.size(); i++) {
                EnemigoBase e = enemigos.get(i);
                e.update();
                e.draw(batch);

                for (int j = i + 1; j < enemigos.size(); j++) {
                    e.checkCollision(enemigos.get(j));
                }

                if (nave.getArea().overlaps(e.getArea())) {
                    int vidaActual = e.getVidas();
                    int vidaMax    = Math.max(1, e.getVidaMaxima());
                    float porcentaje = (float) vidaActual / vidaMax;

                    if (vidaActual > 0 && porcentaje <= 0.25f) {
                        if (e instanceof EnemigoJefe) {
                            gameMusic.stop();
                            game.setScreen(new PantallaVictoriaSecreta(game));
                            dispose();
                            return;
                        }
                        if (nave.conectar(e)) {
                            enemigos.remove(i);
                            i--;
                            continue;
                        }
                    }

                    if (nave.checkCollision(e)) {
                        if (!(e instanceof EnemigoJefe)) {
                            enemigos.remove(i);
                            i--;
                        }
                    }
                }
            }

            // 3. NAVE
            nave.update();
            nave.draw(batch);

            // 4. HUD
            dibujaEncabezado();

            // FIN DE JUEGO
            if (nave.estaDestruido()) {
                gameMusic.stop();
                game.setScreen(new PantallaGameOver(game));
                dispose();
            }

            // VICTORIA / SIGUIENTE RONDA
            if (enemigos.isEmpty()) {
                gameMusic.stop();
                if (ronda < 6) {
                    game.setScreen(new PantallaJuego(
                        game, ronda + 1, nave.getVidas(),
                        velXEnemigos + 20, velYEnemigos + 20, cantEnemigos + 3
                    ));
                } else {
                    GerentePuntuacion.getInstance().resetScore();
                    game.setScreen(new PantallaVictoria(game));
                }
                dispose();
            }

        } else {
            // ---- JUEGO PAUSADO: solo dibujar estado congelado + HUD + menú ----
            for (EnemigoBase e : enemigos) e.draw(batch);
            for (BalaBase b : balas)       b.draw(batch);
            nave.draw(batch);
            dibujaEncabezado();
        }

        // Menú de pausa siempre encima
        menuPausa.draw(batch);

        batch.end();
    }

    public void transformarEn(NaveBase nuevaNave) {
        this.nave = nuevaNave;
        this.nave.setJuego(this);
    }

    // GETTERS DE TEXTURAS
    public Texture getTexturaNaveENormal()      { return texturaNaveENormal; }
    public Texture getTexturaNaveENormalDebil() { return texturaNaveENormalDebil; }
    public Texture getTexturaBalaENormal()      { return texturaBalaENormal; }

    public Texture getTexturaNaveEChico()       { return texturaNaveEChico; }
    public Texture getTexturaNaveEChicoDebil()  { return texturaNaveEChicoDebil; }
    public Texture getTexturaBalaEChico()       { return texturaBalaEChico; }

    public Texture getTexturaNaveEGrande()      { return texturaNaveEGrande; }
    public Texture getTexturaNaveEGrandeDebil() { return texturaNaveEGrandeDebil; }
    public Texture getTexturaBalaEGrande()      { return texturaBalaEGrande; }

    public Sound getSonidoHerido() { return sonidoHerido; }
    public Sound getSonidoBala()   { return sonidoBala; }

    @Override public void show()   {}
    @Override public void resize(int width, int height) {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        if (fondoGameplay != null)          fondoGameplay.dispose();
        if (texturaNaveDefault != null)     texturaNaveDefault.dispose();
        if (texturaNaveDefaultDebil != null) texturaNaveDefaultDebil.dispose();
        if (texturaNaveENormal != null)     texturaNaveENormal.dispose();
        if (texturaNaveENormalDebil != null) texturaNaveENormalDebil.dispose();
        if (texturaNaveEChico != null)      texturaNaveEChico.dispose();
        if (texturaNaveEChicoDebil != null) texturaNaveEChicoDebil.dispose();
        if (texturaNaveEGrande != null)     texturaNaveEGrande.dispose();
        if (texturaNaveEGrandeDebil != null) texturaNaveEGrandeDebil.dispose();
        if (texturaBalaDefault != null)     texturaBalaDefault.dispose();
        if (texturaBalaENormal != null)     texturaBalaENormal.dispose();
        if (texturaBalaEChico != null)      texturaBalaEChico.dispose();
        if (texturaBalaEGrande != null)     texturaBalaEGrande.dispose();
        if (texEnemigoNormal != null)       texEnemigoNormal.dispose();
        if (texEnemigoNormalDebil != null)  texEnemigoNormalDebil.dispose();
        if (texEnemigoGrande != null)       texEnemigoGrande.dispose();
        if (texEnemigoGrandeDebil != null)  texEnemigoGrandeDebil.dispose();
        if (texEnemigoChico != null)        texEnemigoChico.dispose();
        if (texEnemigoChicoDebil != null)   texEnemigoChicoDebil.dispose();
        if (texEnemigoJefe != null)         texEnemigoJefe.dispose();
        if (texEnemigoJefeDebil != null)    texEnemigoJefeDebil.dispose();
        if (explosionSound != null)         explosionSound.dispose();
        if (sonidoHerido != null)           sonidoHerido.dispose();
        if (sonidoBala != null)             sonidoBala.dispose();
        if (gameMusic != null)              gameMusic.dispose();
    }
}