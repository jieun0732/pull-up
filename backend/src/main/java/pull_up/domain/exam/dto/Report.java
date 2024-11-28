package pull_up.domain.exam.dto;

import pull_up.domain.exam.ProblemSummation;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;

public record Report() {
    public record mockExam(
            Long examId,
            String name,
            ScoreInfo scoreInfo,
            DurationInfo durationInfo,
            VulnerableEntryInfo vulnerableEntryInfo
    ) {
        public static mockExam toDto(Exam exam) {
            ProblemSummation problemSummation = exam.getProblemSummation();
            return new mockExam(exam.getId(),
                    exam.getMember().getName(),
                    ScoreInfo.toDto(exam, problemSummation),
                    DurationInfo.toDto(exam),
                    VulnerableEntryInfo.toDto(problemSummation));
        }

        public record ScoreInfo(
                Double averageScore,
                Integer myScore,
                Integer topRate
        ) {

            public static ScoreInfo toDto(Exam exam, ProblemSummation problemSummation) {
                return new ScoreInfo(exam.getExamsheet().getAverageScore(),
                        exam.getScore(),
                        exam.getTopRate(problemSummation));
            }
        }

        public record DurationInfo(
                Integer timeLimit,
                Integer averageDurationMinute,
                Integer myDurationMinute
        ) {

            public static DurationInfo toDto(Exam exam) {
                return new DurationInfo(
                        exam.getTimeLimit().toMinutesPart(),
                        exam.getExamsheet().getAverageDuration().toMinutesPart(),
                        exam.getDuration().toMinutesPart());
            }
        }
        public record VulnerableEntryInfo(
                List<String> vulnerableEntry,
                Integer incorrectLanguageCount,
                Integer incorrectReasoningCount,
                Integer incorrectMathCount,
                Integer totalLanguageCount,
                Integer totalReasoningCount,
                Integer totalMathCount
        ) {

            public static VulnerableEntryInfo toDto(ProblemSummation problemSummation) {
                return new VulnerableEntryInfo(
                        problemSummation.getVulnerableEntry().stream().map(Entry::getKorean).toList(),
                        problemSummation.getEntrySummations().get(Entry.LANGUAGE).getIncorrectCount(),
                        problemSummation.getEntrySummations().get(Entry.REASONING).getIncorrectCount(),
                        problemSummation.getEntrySummations().get(Entry.MATH).getIncorrectCount(),
                        problemSummation.getEntrySummations().get(Entry.LANGUAGE).getTotalCount(),
                        problemSummation.getEntrySummations().get(Entry.REASONING).getTotalCount(),
                        problemSummation.getEntrySummations().get(Entry.MATH).getTotalCount());
            }
        }
    }
}
