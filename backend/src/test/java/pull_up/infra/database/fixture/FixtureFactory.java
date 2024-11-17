package pull_up.infra.database.fixture;

import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FixtureFactory {

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

    public static Exam getExam(Long memberId, ExamType examType, Entry entry) {
        Member member = getMember(memberId);
        return Exam.start(examType, member, getAllProblem(entry));
    }

    public static Answer getEmptyAnswer(Integer problemNumber, Long memberId, ExamType examType, Entry entry) {
        Exam exam = getExam(memberId, examType, entry);
        return exam.getAnswers().stream()
                .filter(answer -> answer.getProblemNumber().equals(problemNumber))
                .findFirst().orElseThrow(IllegalStateException::new);
    }

    private static Member getMember(Long memberId) {
        for (MemberFixture memberFixture : MemberFixture.values()) {
            if (memberFixture.get().getId().equals(memberId)) return memberFixture.get();
        }
        throw new IllegalArgumentException();
    }

    private static List<Problem> getAllProblem(Entry entry) {
        return Arrays.stream(ProblemFixture.values()).map(ProblemFixture::get).filter(problem -> problem.getEntry().equals(entry)).toList();
    }

    private static Problem getProblem(Long problemId) {
        for (ProblemFixture problemFixture : ProblemFixture.values()) {
            if (problemFixture.get().getId().equals(problemId)) return problemFixture.get();
        }
        throw new IllegalArgumentException();
    }
}
