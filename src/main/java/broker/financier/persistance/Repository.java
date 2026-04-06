package broker.financier.persistance;

public interface Repository<E> {
    void sauvegarder(E pojo);
    E charger();
}
