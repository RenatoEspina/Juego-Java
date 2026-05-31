package puppy.code.Factory;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.Enemigo.EnemigoBase;
import puppy.code.Enemigo.EnemigoBuilder;
import puppy.code.MovementStrategy.MovimientoRebote;
import puppy.code.Pantalla.PantallaJuego;

public class FabricaNivelFacil implements FabricaEnemigos {

    private Texture texNormal, texNormalDebil;
    private Texture texGrande, texGrandeDebil;
    private Texture texChico, texChicoDebil;
    private Texture texJefe, texJefeDebil;

    public FabricaNivelFacil(Texture tN, Texture tND, Texture tG, Texture tGD,
                             Texture tC, Texture tCD, Texture tJ, Texture tJD) {
        this.texNormal = tN; this.texNormalDebil = tND;
        this.texGrande = tG; this.texGrandeDebil = tGD;
        this.texChico = tC;  this.texChicoDebil = tCD;
        this.texJefe = tJ;   this.texJefeDebil = tJD;
    }

    @Override
    public EnemigoBase crearEnemigoChico(float x, float y) {
        return new EnemigoBuilder()
            .tipo(EnemigoBuilder.TipoEnemigo.CHICO)
            .conEstrategia(new MovimientoRebote(300, 300))
            .conTexturas(texChico, texChicoDebil)
            .enPosicion(x, y)
            .build();
    }

    @Override
    public EnemigoBase crearEnemigoNormal(float x, float y) {
        return new EnemigoBuilder()
            .tipo(EnemigoBuilder.TipoEnemigo.NORMAL)
            .conEstrategia(new MovimientoRebote(150, 150))
            .conTexturas(texNormal, texNormalDebil)
            .enPosicion(x, y)
            .build();
    }

    @Override
    public EnemigoBase crearEnemigoGrande(float x, float y) {
        return new EnemigoBuilder()
            .tipo(EnemigoBuilder.TipoEnemigo.GRANDE)
            .conEstrategia(new MovimientoRebote(80, 80))
            .conTexturas(texGrande, texGrandeDebil)
            .enPosicion(x, y)
            .build();
    }

    @Override
    public EnemigoBase crearEnemigoAleatorio(float x, float y) {
        return crearEnemigoNormal(x, y);
    }

    @Override
    public EnemigoBase crearEnemigoJefe(float x, float y, PantallaJuego juego) {
        // El jefe no aplica en nivel fácil
        return null;
    }
}