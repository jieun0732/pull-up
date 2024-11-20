package pull_up.infra.database.jpa.fixture;

public interface Fixture<T> {
    public T get();
}
