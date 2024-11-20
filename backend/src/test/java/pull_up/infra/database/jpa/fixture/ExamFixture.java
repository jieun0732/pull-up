package pull_up.infra.database.jpa.fixture;

import lombok.RequiredArgsConstructor;
import pull_up.domain.exam.ExamType;
import pull_up.infra.database.jpa.entity.Exam;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public enum ExamFixture implements Fixture<Exam>{
    EVENLY_START(1L, false, 0, ExamType.EVENLY, LocalDateTime.of(2024, 11, 16, 12, 25), null, null),
    EVENLY_FINISHED(2L, true, 50, ExamType.EVENLY, LocalDateTime.of(2024, 11, 16, 12, 25), LocalDateTime.of(2024, 11, 16, 12, 35), Duration.ofMinutes(10)),
    BY_PROBLEM_TYPE_START(3L, false, 0, ExamType.BY_PROBLEM_TYPE, LocalDateTime.of(2024, 11, 16, 12, 25), null, null),
    BY_PROBLEM_TYPE_FINISHED(4L, true, 60, ExamType.BY_PROBLEM_TYPE, LocalDateTime.of(2024, 11, 16, 12, 25), LocalDateTime.of(2024, 11, 16, 12, 40), Duration.ofMinutes(15)),
    MOCK_EXAM_START(5L, true, 0, ExamType.MOCK_EXAM, LocalDateTime.of(2024, 11, 16, 12, 25), null, null),
    MOCK_EXAM_FINISHED(6L, true, 70, ExamType.MOCK_EXAM, LocalDateTime.of(2024, 11, 16, 12, 25), LocalDateTime.of(2024, 11, 16, 12, 45), Duration.ofMinutes(20));

    private final Long id;
    private final Boolean isFinished;
    private final Integer score;
    private final ExamType examType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Duration duration;

    public Exam get() {
        return new Exam(id, isFinished, score, examType, startTime, endTime, duration, null, null);
    }
}
