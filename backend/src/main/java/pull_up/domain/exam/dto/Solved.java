package pull_up.domain.exam.dto;

import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.ProblemSummation;
import pull_up.domain.exam.TempExam;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Solved() {
    public record MockExamResponse(
            Long examId,
            Boolean isMockExamGraded,
            Boolean tutorialFinished
    ) {

        public static MockExamResponse empty(boolean tutorialFinished) {
            return new MockExamResponse(null, false, tutorialFinished);
        }

        public static MockExamResponse toDto(boolean tutorialFinished, Exam exam) {
            return new MockExamResponse(exam.getId(), exam.getIsFinished(), tutorialFinished);
        }
    }

    public record ByEntryResponse(
            Entry entry,
            EvenlyExamInfo evenlyExamInfo,
            Integer problemTypeCount,
            List<ProblemTypeExamInfo> problemTypeExamInfos
    ) {
        public static ByEntryResponse toDto(Entry entry, Map<String, Exam> examMap) {
            Exam evenlyExam = examMap.get(ExamType.EVENLY.name());

            EvenlyExamInfo evenlyExamInfo = evenlyExam != null ?
                    new EvenlyExamInfo(evenlyExam.getId(), true, evenlyExam.getIsFinished(), evenlyExam.getLastSolvedProblem()) :
                    new EvenlyExamInfo(null, false, false, -1);

            int problemTypeCount = evenlyExam != null ? examMap.size() - 1 : examMap.size();  // evenly exam 제외
            List<ProblemTypeExamInfo> problemTypeExamInfoList = ProblemTypeExamInfo.toList(examMap);

            return new ByEntryResponse(entry,
                    evenlyExamInfo,
                    problemTypeCount,
                    problemTypeExamInfoList);
        }

        public record EvenlyExamInfo(
                Long examId,
                Boolean isStarted,
                Boolean isFinished,
                Integer lastSolvedProblemNumber
        ) {

        }

        public record ProblemTypeExamInfo(
                Long examId,
                Boolean isStarted,
                String problemType,
                Integer solvedProblemCount,
                Integer correctProblemCount,
                Integer incorrectProblemCount,
                Integer totalProblemCount
        ) {

            public static List<ProblemTypeExamInfo> toList(Map<String, Exam> problemMap) {
                List<ProblemTypeExamInfo> ret = new ArrayList<>();
                for (Map.Entry<String, Exam> examEntry : problemMap.entrySet()) {
                    if (examEntry.getKey().equals(ExamType.EVENLY.name())) continue;
                    ret.add(toDto(examEntry.getKey(), examEntry.getValue()));
                }
                return ret;
            }

            public static ProblemTypeExamInfo toDto(String problemType, Exam exam) {
                ProblemSummation problemSummation = exam.getProblemSummation();
                return new ProblemTypeExamInfo(exam.getId(),
                        !(exam instanceof TempExam),
                        problemType,
                        problemSummation.getSolvedProblemCount(),
                        problemSummation.getCorrectProblemCount(),
                        problemSummation.getIncorrectProblemCount(),
                        problemSummation.getTotalProblemCount());
            }
        }
    }
}
