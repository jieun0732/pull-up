package pull_up.infra.database.fixture.legacy;

import lombok.RequiredArgsConstructor;
import pull_up.infra.database.entity.legacy.AnswerL;
import pull_up.infra.database.entity.legacy.ExamL;
import pull_up.infra.database.entity.legacy.MemberL;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static pull_up.infra.database.fixture.legacy.AnswerFixture.*;

@RequiredArgsConstructor
public enum ExamFixture {
    MATHEMATICS(List.of(
            NO_CHOSEN_1.get(), NO_CHOSEN_2.get(), NO_CHOSEN_3.get(), NO_CHOSEN_4.get(), NO_CHOSEN_5.get()),
            "수리", "골고루", "속력",
            LocalDateTime.of(2024, 11, 13, 17, 39),
            LocalDateTime.of(2024, 11, 13, 22, 3),
            0);

    private final List<AnswerL> answerLS;
    private final String entry;
    private final String category;
    private final String type;
    private final LocalDateTime createdDate;
    private final LocalDateTime solvedTime;
    private final Integer score;

    public ExamL get(MemberL memberL) {
        ExamL examL = ExamL.of(memberL, entry, category, type, createdDate, solvedTime, Duration.between(createdDate, solvedTime), score);
        examL.setAnswerLS(answerLS);
        answerLS.forEach(answeredProblem -> answeredProblem.setExamL(examL));
        memberL.setExamLList(List.of(examL));
        return examL;
    }
}
