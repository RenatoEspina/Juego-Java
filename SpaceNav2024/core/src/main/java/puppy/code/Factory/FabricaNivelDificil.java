package puppy.code.Factory;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.Enemigo.EnemigoBase;
import puppy.code.Enemigo.EnemigoBuilder;
import puppy.code.MovementStrategy.MovimientoEstatico;
import puppy.code.MovementStrategy.MovimientoRebote;
import puppy.code.Pantalla.PantallaJuego;

import java.util.Random;

public class FabricaNivelDificil implements FabricaEnemigos {

    private Texture texNormal, texNormalDebil;
    private Texture texGrande, texGrandeDebil;
    private Texture texChico, texChicoDebil;
    private Texture texJefe, texJefeDebil;

    public FabricaNivelDificil(Texture tN, Texture tND, Texture tG, Texture tGD,
                               Texture tC, Texture tCD, Texture tJ, Texture tJD) {
        this.texNormal = tN;      this.texNormalDebil = tND;
        this.texGrande = tG;      this.texGrandeDebil = tGD;
        this.texChico = tC;       this.texChicoDebil = tCD;
        this.texJefe = tJ;        this.texJefeDebil = tJD;
    }

    @Override
    public EnemigoBase crearEnemigoChico(float x, float y) {
        return new EnemigoBuilder()
            .tipo(EnemigoBuilder.TipoEnemigo.CHICO)
            .conEstrategia(new MovimientoRebote(500, 500))
            .conTexturas(texChico, texChicoDebil)
            .enPosicion(x, y)
            .build();
    }

    @Override
    public EnemigoBase crearEnemigoNormal(float x, float y) {
        return new EnemigoBuilder()
            .tipo(EnemigoBuilder.TipoEnemigo.NORMAL)
            .conEstrategia(new MovimientoRebote(250, 250))
            .conTexturas(texNormal, texNormalDebil)
            .enPosicion(x, y)
            .build();
    }

    @Override
    public EnemigoBase crearEnemigoGrande(float x, float y) {
        return new EnemigoBuilder()
            .tipo(EnemigoBuilder.TipoEnemigo.GRANDE)
            .conEstrategia(new MovimientoRebote(150, 150))
            .conTexturas(texGrande, texGrandeDebil)
            .enPosicion(x, y)
            .build();
    }

    @Override
    public EnemigoBase crearEnemigoAleatorio(float x, float y) {
        Random r = new Random();
        int suerte = r.nextInt(100);

        if (suerte < 50) return crearEnemigoNormal(x, y);
        else if (suerte < 80) return crearEnemigoChico(x, y);
        else return crearEnemigoGrande(x, y);
    }

    @Override
    public EnemigoBase crearEnemigoJefe(float x, float y, PantallaJuego juego) {
        return new EnemigoBuilder()
            .tipo(EnemigoBuilder.TipoEnemigo.JEFE)
            .conEstrategia(new MovimientoEstatico())
            .conTexturas(texJefe, texJefeDebil)
            .enPosicion(x, y)
            .conJuego(juego)
            .conFabrica(this)
            .build();
    }
}