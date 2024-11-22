package pull_up.infra.database.jpa.fixture;

import pull_up.domain.exam.TempExam;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.embedded.Problemsheet;
import pull_up.infra.database.jpa.entity.*;

import java.util.*;

public class FixtureRepository {

    public static List<Problem> getProblemList() {
        return getProblemList(ProblemFixture.values().length);
    }

    public static List<Problem> getProblemList(int size) {
        if (size > ProblemFixture.values().length) throw new IllegalArgumentException();

        List<Problem> problemList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            problemList.add(ProblemFixture.values()[i].get());
        }
        return problemList;
    }

    public static List<Problem> getProblemList(Entry entry) {
        List<Problem> problemList = new ArrayList<>();
        for (ProblemFixture problemFixture : ProblemFixture.values()) {
            if (problemFixture.get().getEntry() != entry) continue;
            problemList.add(problemFixture.get());
        }
        return problemList;
    }

    public static List<Problem> getProblemList(Entry entry, String problemType) {
        List<Problem> problemList = new ArrayList<>();
        for (ProblemFixture problemFixture : ProblemFixture.values()) {
            if (problemFixture.get().getEntry() != entry || !problemFixture.get().getProblemType().equals(problemType))
                continue;
            problemList.add(problemFixture.get());
        }
        return problemList;
    }

    public static Map<Integer, Long> getProblemsheet() {
        List<Problem> problemList = FixtureRepository.getProblemList();
        Map<Integer, Long> problemMap = new HashMap<>();
        for (int i = 0; i < problemList.size(); i++)
            problemMap.put(i + 1, problemList.get(i).getId());
        return problemMap;
    }

    public static Map<Integer, Problem> getProblemSheetMap() {
        List<Problem> problemList = FixtureRepository.getProblemList();
        Examsheet examsheet = FixtureRepository.getExamsheet("모의고사");
        return Problemsheet.getProblemSheetMap(problemList, examsheet);
    }

    public static Examsheet getExamsheet(String examTitle) {
        return Examsheet.create(examTitle, getProblemsheet());
    }

    public static Exam getEvenlyExam(Long memberId, Entry entry) {
        Member member = getMember(memberId);
        return Exam.startEvenlyExam(member, getProblemList(entry));
    }

    public static Exam getProblemTypeExam(Long memberId, Entry entry, String problemType) {
        Member member = getMember(memberId);
        List<Problem> problemList = getProblemList(entry, problemType);
        return Exam.startByProblemTypeExam(member, problemList);
    }

    public static Exam getMockExam(Long memberId) {
        Member member = getMember(memberId);
        List<Problem> problemList = getProblemList();
        Examsheet examsheet = getExamsheet("모의고사");
        return Exam.startMockExam(member, problemList, examsheet);
    }

    public static Exam getEmptyProblemTypeExam(Entry entry, String problemType) {
        List<Problem> problemList = getProblemList(entry, problemType);
        return new TempExam(problemList.size());
    }

    public static Answer getEmptyEvenlyAnswer(Integer problemNumber, Long memberId, Entry entry) {
        Exam exam = getEvenlyExam(memberId, entry);
        return exam.getAnswers().stream()
                .filter(answer -> answer.getProblemNumber().equals(problemNumber))
                .findFirst().orElseThrow(IllegalStateException::new);
    }

    public static List<Exam> getExams(Long memberId, Entry entry, String problemType) {
        List<Exam> ret = new ArrayList<>();
        for (Problem problem : getProblemList(entry)) {
            if (!problem.getProblemType().equals(problemType)) continue;
            ret.add(getProblemTypeExam(memberId, entry, problemType));
        }
        return ret;
    }

    public static Map<String, Integer> getProblemTypesMap(Entry entry, String problemType) {
        Map<String, Integer> ret = new HashMap<>();
        for (ProblemFixture problemFixture : ProblemFixture.values()) {
            if (!problemFixture.get().getEntry().equals(entry) || !problemFixture.get().getProblemType().equals(problemType))
                continue;
            String problemTypeKey = problemFixture.get().getProblemType();

            if (!ret.containsKey(problemTypeKey)) ret.put(problemTypeKey, 1);
            else ret.put(problemTypeKey, ret.get(problemTypeKey) + 1);
        }
        return ret;
    }

    public static Set<String> getProblemTypes(Entry entry) {
        HashSet<String> problemTypes = new HashSet<>();
        for (ProblemFixture problemFixture : ProblemFixture.values()) {
            if (problemFixture.get().getEntry() != entry) continue;
            problemTypes.add(problemFixture.get().getProblemType());
        }
        return problemTypes;
    }

    private static Member getMember(Long memberId) {
        for (MemberFixture memberFixture : MemberFixture.values()) {
            if (memberFixture.get().getId().equals(memberId)) return memberFixture.get();
        }
        throw new IllegalArgumentException();
    }

    private static Problem getProblem(Long problemId) {
        for (ProblemFixture problemFixture : ProblemFixture.values()) {
            if (problemFixture.get().getId().equals(problemId)) return problemFixture.get();
        }
        throw new IllegalArgumentException();
    }
}
