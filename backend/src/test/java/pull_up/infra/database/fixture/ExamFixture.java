package pull_up.infra.database.fixture;

import lombok.RequiredArgsConstructor;
import pull_up.infra.database.entity.AnsweredProblem;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static pull_up.infra.database.fixture.AnsweredProblemFixture.*;

@RequiredArgsConstructor
public enum ExamFixture {
    MATHEMATICS(List.of(
            NO_CHOSEN_1.get(), NO_CHOSEN_2.get(), NO_CHOSEN_3.get(), NO_CHOSEN_4.get(), NO_CHOSEN_5.get()),
            "수리", "골고루", "속력",
            LocalDateTime.of(2024, 11, 13, 17, 39),
            LocalDateTime.of(2024, 11, 13, 22, 3),
            0);

    private final List<AnsweredProblem> answeredProblems;
    private final String entry;
    private final String category;
    private final String type;
    private final LocalDateTime createdDate;
    private final LocalDateTime solvedTime;
    private final Integer score;

    public Exam get(Member member) {
        Exam exam = Exam.of(member, entry, category, type, createdDate, solvedTime, Duration.between(createdDate, solvedTime), score);
        exam.setAnsweredProblem(answeredProblems);
        answeredProblems.forEach(answeredProblem -> answeredProblem.setExam(exam));
        member.setExamList(List.of(exam));
        return exam;
    }
}
