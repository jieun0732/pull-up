package pull_up.infra.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.ProblemSummation;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.exam.exception.ExamException;
import pull_up.global.entity.BaseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static pull_up.domain.exam.ExamType.EVENLY;

@Entity
@Table(name = "exam")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Exam extends BaseEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean isFinished;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer score;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExamType examType;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY)
    private List<Answer> answers;

    private Exam(ExamType examType, Member member) {
        this.isFinished = false;
        this.score = 0;
        this.examType = examType;
        this.startTime = LocalDateTime.now();
        this.member = member;
    }

    public static Exam start(ExamType examType, Member member, List<Problem> problemList) {
        return switch (examType) {
            case EVENLY -> startEvenlyExam(member, problemList);
            case BY_PROBLEM_TYPE -> throw new UnsupportedOperationException();
            case MOCK_EXAM -> throw new UnsupportedOperationException();
        };
    }

    static Exam startEvenlyExam(Member member, List<Problem> problemList) {
        Exam exam = new Exam(EVENLY, member);
        List<Problem> problems = selectEvenlyProblems(problemList);
        List<Answer> answers = new ArrayList<>(problems.size());
        for (int i = 1; i <= problems.size(); i++) {
            answers.add(Answer.makeEmptyAnswer(exam, problems.get(i - 1), i));
        }
        exam.answers = answers;
        return exam;
    }

    // 유형별로 1개의 문제씩 총 10개만 담기
    static List<Problem> selectEvenlyProblems(List<Problem> problemList) {
        List<Problem> problems = new ArrayList<>(10);
        HashSet<String> problemTypes = new HashSet<>();
        for (Problem problem : problemList) {
            if (problemTypes.contains(problem.getNormalProblemType())) continue;
            if (problems.size() >= 10) break;
            problems.add(problem);
            problemTypes.add(problem.getNormalProblemType());
        }
        return problems;
    }

    public Answer submit(Integer problemNumber, Integer submitAnswer) {
        Answer answer = getAnswerByProblemNumber(problemNumber);
        answer.mark(submitAnswer);
        score = calculateScore();
        duration = Duration.between(startTime, LocalDateTime.now());
        return answer;
    }

    private Integer calculateScore() {
        int correctCount = 0;
        for (Answer answer : answers) {
            if (answer.getIsSubmitted() && answer.getIsCorrect()) correctCount++;
        }
        return (int)((double) correctCount / answers.size() * 100);
    }

    public Answer getAnswerByProblemNumber(Integer problemNumber) {
        for (Answer answer : answers) {
            if (answer.getProblemNumber().equals(problemNumber)) return answer;
        }
        throw new ExamException(ExamErrorCode.PROBLEM_NUMBER_EXCEED);
    }

    public void end() {
        isFinished = getProblemSummation().getLeftProblemCount() == 0;
        calculateDuration();
    }

    public ProblemSummation getProblemSummation() {
        int leftProblemCount = 0, correctProblemCount = 0;
        for (Answer answer : answers) {
            if (!answer.getIsSubmitted()) {
                leftProblemCount++;
                continue;
            }
            if (answer.getIsCorrect()) correctProblemCount++;
        }
        return new ProblemSummation(answers.size(), leftProblemCount, correctProblemCount);
    }

    private void calculateDuration() {
        LocalDateTime now = LocalDateTime.now();
        if (notSaved()) {
            endTime = now;
            duration = Duration.between(startTime, endTime);
        } else {
            duration = duration.plus(Duration.between(endTime, now));
            endTime = now;
        }
    }

    private boolean notSaved() {
        return endTime == null;
    }
}
