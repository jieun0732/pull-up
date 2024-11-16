package pull_up.infra.database.fixture;

import pull_up.domain.problem.Entry;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;

import java.util.ArrayList;
import java.util.List;

public class FixtureFactory {

    public static Member getMember(Long id) {
        return null;
    }

    public static List<Problem> getProblemList(int size) {
        if (size > ProblemFixture.values().length) throw new IllegalArgumentException();

        List<Problem> problemList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            problemList.add(ProblemFixture.values()[i].get());
        }
        return problemList;
    }

    public static List<Problem> getProblemList(int size, Entry entry) {
        if (size > ProblemFixture.values().length / (Entry.values().length - 1)) throw new IllegalArgumentException();

        List<Problem> problemList = new ArrayList<>(size);
        for (ProblemFixture problemFixture : ProblemFixture.values()) {
            if (problemFixture.get().getEntry() != entry) continue;
            problemList.add(problemFixture.get());
        }
        return problemList;
    }

}
