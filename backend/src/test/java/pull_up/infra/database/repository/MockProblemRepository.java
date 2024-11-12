package pull_up.infra.database.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.problem.ProblemRepository;
import pull_up.infra.database.entity.Problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MockProblemRepository implements ProblemRepository {

    private List<Problem> problems = new ArrayList<>();

    @Override
    public List<Problem> findByCategory(String category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Problem> findByCategoryNot(String category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Problem> findByCategoryAndEntry(String category, String entryName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch(Iterable<Problem> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Problem getOne(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Problem getById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Problem getReferenceById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Problem> S save(S entity) {
        entity.setId(1L);
        this.problems.add(entity);

        return entity;
    }

    @Override
    public <S extends Problem> List<S> saveAll(Iterable<S> entities) {
        List<S> entityList = (List<S>) entities;
        for (int i = 0; i < entityList.size(); i++) entityList.get(i).setId((long) (i + 1));
        this.problems.addAll(entityList);
        return entityList;
    }

    @Override
    public Optional<Problem> findById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Problem> findAll() {
        return this.problems;
    }

    @Override
    public List<Problem> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Problem entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Problem> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        this.problems.clear();
    }

    @Override
    public List<Problem> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Problem> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ProblemDto> findByEntryAndCategory(String entry, String category) {
        throw new UnsupportedOperationException();
    }
}
