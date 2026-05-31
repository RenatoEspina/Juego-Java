package puppy.code;

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
    private ArrayList<BalaBase> balas = new ArrayList<>(); // Polimorfismo de balas

    // PATRÓN ABSTRACT FACTORY
    private FabricaEnemigos fabrica;

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
        // Configuración para 1080p (Full HD)
        camera.setToOrtho(false, 1920, 1080);

        // --- CARGA DE AUDIOS ---
        // Asegúrate de que estos archivos existan en tu carpeta assets
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        sonidoHerido = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        sonidoBala = Gdx.audio.newSound(Gdx.files.internal("disparo.ogg"));

        // Lógica de Música: Jefe vs Niveles normales
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

        // Nave Default
        texturaNaveDefault = new Texture(Gdx.files.internal("NaveDefault.png"));
        texturaNaveDefaultDebil = new Texture(Gdx.files.internal("NaveDefaultDebil.png"));
        texturaBalaDefault = new Texture(Gdx.files.internal("BulletNormal.png"));

        // Fusión Normal
        texturaNaveENormal = new Texture(Gdx.files.internal("NaveENormal.png"));
        texturaNaveENormalDebil = new Texture(Gdx.files.internal("NaveENormalDebil.png"));
        texturaBalaENormal = new Texture(Gdx.files.internal("BulletEnemigoNormal.png"));

        // Fusión Chica
        texturaNaveEChico = new Texture(Gdx.files.internal("NaveEChico.png"));
        texturaNaveEChicoDebil = new Texture(Gdx.files.internal("NaveEChicoDebil.png"));
        texturaBalaEChico = new Texture(Gdx.files.internal("BulletChica.png"));

        // Fusión Grande
        texturaNaveEGrande = new Texture(Gdx.files.internal("NaveEGrande.png"));
        texturaNaveEGrandeDebil = new Texture(Gdx.files.internal("NaveEGrandeDebil.png"));
        texturaBalaEGrande = new Texture(Gdx.files.internal("BulletEGrande.png"));

        // Enemigos
        texEnemigoNormal = new Texture(Gdx.files.internal("enemigoNormal.png"));
        texEnemigoNormalDebil = new Texture(Gdx.files.internal("enemigoNormalDebil.png"));
        texEnemigoGrande = new Texture(Gdx.files.internal("enemigoGrande.png"));
        texEnemigoGrandeDebil = new Texture(Gdx.files.internal("enemigoGrandeDebil.png"));
        texEnemigoChico = new Texture(Gdx.files.internal("enemigoChico.png"));
        texEnemigoChicoDebil = new Texture(Gdx.files.internal("enemigoChicoDebil.png"));
        texEnemigoJefe = new Texture(Gdx.files.internal("enemigoJefe.png"));
        texEnemigoJefeDebil = new Texture(Gdx.files.internal("enemigoJefeDebil.png"));

        // --- CREAR NAVE ---
        // Posicionada al centro horizontal, parte inferior
        nave = new NaveDefault(
            Gdx.graphics.getWidth() / 2f - 40, 50,
            texturaNaveDefault, texturaNaveDefaultDebil,
            sonidoHerido, texturaBalaDefault, sonidoBala
        );
        nave.setVidas(vidas);
        nave.setJuego(this);

        iniciarNivel();
    }

    private void iniciarNivel() {
        if (ronda <= 3) {
            fabrica = new FabricaNivelFacil(
                texEnemigoNormal, texEnemigoNormalDebil,
                texEnemigoGrande, texEnemigoGrandeDebil,
                texEnemigoChico, texEnemigoChicoDebil,
                texEnemigoJefe, texEnemigoJefeDebil
            );
        } else {
            fabrica = new FabricaNivelDificil(
                texEnemigoNormal, texEnemigoNormalDebil,
                texEnemigoGrande, texEnemigoGrandeDebil,
                texEnemigoChico, texEnemigoChicoDebil,
                texEnemigoJefe, texEnemigoJefeDebil
            );
        }

        Random r = new Random();

        if (ronda == 6) {
            float bossX = Gdx.graphics.getWidth() / 2f - 175;
            float bossY = Gdx.graphics.getHeight() / 2f - 175;

            enemigos.add(fabrica.crearEnemigoJefe(bossX, bossY, this));

        } else {
            // --- NIVELES 1 al 5 ---
            for (int i = 0; i < cantEnemigos; i++) {
                // Spawn aleatorio con margen
                float x = r.nextInt(Gdx.graphics.getWidth() - 100);
                float y = 100 + r.nextInt(Gdx.graphics.getHeight() - 200);
                EnemigoBase nuevo = null;

                if (ronda == 1) nuevo = fabrica.crearEnemigoChico(x, y);
                else if (ronda == 2) nuevo = fabrica.crearEnemigoNormal(x, y);
                else if (ronda == 3) nuevo = fabrica.crearEnemigoGrande(x, y);
                else nuevo = fabrica.crearEnemigoAleatorio(x, y); // Rondas 4 y 5 (Mix)

                if (nuevo != null) enemigos.add(nuevo);
            }
        }
    }

    // Método público para que el Jefe invoque esbirros
    public void agregarEnemigo(EnemigoBase enemigo) {
        this.enemigos.add(enemigo);
    }

    // Método público para que la Nave dispare
    public boolean agregarBala(BalaBase bb) {
        return balas.add(bb);
    }

    // Método para HUD (Texto arriba y grande)
    public void dibujaEncabezado() {
        int puntajeActual = GerentePuntuacion.getInstance().getScoreActual();
        int record = GerentePuntuacion.getInstance().getHighScore();

        // Fuente grande para 1080p
        game.getFont().getData().setScale(4f);

        // Dibujar en la parte SUPERIOR
        float alto = camera.viewportHeight - 50;

        game.getFont().draw(batch, "Vidas: " + nave.getVidas() + " | Ronda: " + ronda, 30, alto);
        game.getFont().draw(batch, "Score: " + puntajeActual, camera.viewportWidth - 400, alto);
        game.getFont().draw(batch, "High: " + record, camera.viewportWidth / 2f - 150, alto);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar cámara
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // DIBUJAR FONDO ESTIRADO A 1080p
        batch.draw(fondoGameplay, 0, 0, camera.viewportWidth, camera.viewportHeight);

        // 1. BALAS
        for (int i = 0; i < balas.size(); i++) {
            BalaBase b = balas.get(i);
            b.update();
            b.draw(batch); // IMPORTANTE: Dibujar bala

            for (int j = 0; j < enemigos.size(); j++) {
                EnemigoBase e = enemigos.get(j);
                if (b.checkCollision(e)) { // Dañar enemigo
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
            e.update(); // Mover y animar
            e.draw(batch); // Dibujar

            // Colisión entre enemigos
            for (int j = i + 1; j < enemigos.size(); j++) {
                e.checkCollision(enemigos.get(j));
            }

            // INTERACCIÓN CON NAVE (Overlap)
            if (nave.getArea().overlaps(e.getArea())) {
                int vidaActual = e.getVidas();
                int vidaMax = Math.max(1, e.getVidaMaxima());
                float porcentaje = (float)vidaActual / vidaMax;

                // A) FUSIÓN / ABSORCIÓN (Solo si está débil)
                if (vidaActual > 0 && porcentaje <= 0.25f) {

                    // CASO ESPECIAL: JEFE -> VICTORIA SECRETA
                    if (e instanceof EnemigoJefe) {
                        gameMusic.stop();
                        game.setScreen(new PantallaVictoriaSecreta(game));
                        dispose();
                        return;
                    }

                    // CASO NORMAL: TRANSFORMACIÓN
                    if (nave.conectar(e)) {
                        enemigos.remove(i);
                        i--;
                        continue; // Pasamos al siguiente
                    }
                }

                // B) COLISIÓN FÍSICA (DAÑO)
                if (nave.checkCollision(e)) {

                    if (e instanceof EnemigoJefe) {
                        // SI ES JEFE: NO LO BORRAMOS
                        // La nave rebota y pierde vida, pero el jefe se queda.
                    } else {
                        // SI ES NORMAL: EL ENEMIGO EXPLOTA
                        enemigos.remove(i);
                        i--;
                    }
                }
            }
        }

        // 3. NAVE
        nave.update(); // Lógica
        nave.draw(batch); // Dibujo

        // 4. HUD (Al final para que quede encima)
        dibujaEncabezado();

        // --- LOGICA DE FIN DE JUEGO ---
        if (nave.estaDestruido()) {
            gameMusic.stop();
            game.setScreen(new PantallaGameOver(game));
            dispose();
        }

        // --- LOGICA DE FIN DE NIVEL / VICTORIA ---
        if (enemigos.isEmpty()) {
            gameMusic.stop();

            if (ronda < 6) {
                // Siguiente nivel
                game.setScreen(new PantallaJuego(
                    game, ronda + 1, nave.getVidas(),
                    velXEnemigos + 20, velYEnemigos + 20, cantEnemigos + 3
                ));
            } else {
                // VICTORIA FINAL (Ronda 6 completada)
                GerentePuntuacion.getInstance().resetScore();
                game.setScreen(new PantallaVictoria(game));
            }
            dispose();
        }

        batch.end();
    }

    public void transformarEn(NaveBase nuevaNave) {
        this.nave = nuevaNave;
        this.nave.setJuego(this);
    }

    // GETTERS DE TEXTURAS (Para NaveBase y sus fusiones)
    public Texture getTexturaNaveENormal() { return texturaNaveENormal; }
    public Texture getTexturaNaveENormalDebil() { return texturaNaveENormalDebil; }
    public Texture getTexturaBalaENormal() { return texturaBalaENormal; }

    public Texture getTexturaNaveEChico() { return texturaNaveEChico; }
    public Texture getTexturaNaveEChicoDebil() { return texturaNaveEChicoDebil; }
    public Texture getTexturaBalaEChico() { return texturaBalaEChico; }

    public Texture getTexturaNaveEGrande() { return texturaNaveEGrande; }
    public Texture getTexturaNaveEGrandeDebil() { return texturaNaveEGrandeDebil; }
    public Texture getTexturaBalaEGrande() { return texturaBalaEGrande; }

    public Sound getSonidoHerido() { return sonidoHerido; }
    public Sound getSonidoBala() { return sonidoBala; }

    @Override public void show() { }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        if (fondoGameplay != null) fondoGameplay.dispose();

        // Naves
        if (texturaNaveDefault != null) texturaNaveDefault.dispose();
        if (texturaNaveDefaultDebil != null) texturaNaveDefaultDebil.dispose();
        if (texturaNaveENormal != null) texturaNaveENormal.dispose();
        if (texturaNaveENormalDebil != null) texturaNaveENormalDebil.dispose();
        if (texturaNaveEChico != null) texturaNaveEChico.dispose();
        if (texturaNaveEChicoDebil != null) texturaNaveEChicoDebil.dispose();
        if (texturaNaveEGrande != null) texturaNaveEGrande.dispose();
        if (texturaNaveEGrandeDebil != null) texturaNaveEGrandeDebil.dispose();

        // Balas
        if (texturaBalaDefault != null) texturaBalaDefault.dispose();
        if (texturaBalaENormal != null) texturaBalaENormal.dispose();
        if (texturaBalaEChico != null) texturaBalaEChico.dispose();
        if (texturaBalaEGrande != null) texturaBalaEGrande.dispose();

        // Enemigos
        if (texEnemigoNormal != null) texEnemigoNormal.dispose();
        if (texEnemigoNormalDebil != null) texEnemigoNormalDebil.dispose();
        if (texEnemigoGrande != null) texEnemigoGrande.dispose();
        if (texEnemigoGrandeDebil != null) texEnemigoGrandeDebil.dispose();
        if (texEnemigoChico != null) texEnemigoChico.dispose();
        if (texEnemigoChicoDebil != null) texEnemigoChicoDebil.dispose();
        if (texEnemigoJefe != null) texEnemigoJefe.dispose();
        if (texEnemigoJefeDebil != null) texEnemigoJefeDebil.dispose();

        if (explosionSound != null) explosionSound.dispose();
        if (sonidoHerido != null) sonidoHerido.dispose();
        if (sonidoBala != null) sonidoBala.dispose();
        if (gameMusic != null) gameMusic.dispose();
    }
}
