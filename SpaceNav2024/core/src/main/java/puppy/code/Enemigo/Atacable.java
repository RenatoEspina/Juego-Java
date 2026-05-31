package puppy.code.Enemigo;

public interface Atacable {
    void recibirDano(int cantidad);
    boolean estaDestruido();
    boolean conectar(Atacable otro);
}
