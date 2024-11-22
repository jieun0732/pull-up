package pull_up.domain.exam.dto;

import org.apache.catalina.core.FrameworkListener;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.ProblemSummation;
import pull_up.domain.exam.TempExam;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.*;

public record SolvedInfo() {
    public record Response(
            Entry entry,
            Long evenlyExamId,
            Boolean isEvenlyExamStarted,
            Boolean isEvenlyExamFinished,
            Integer lastSolvedEvenlyExamProblemNumber,
            Integer problemTypeCount,
            List<ProblemTypeInfo> problemTypeInfos
    ) {
        public record ProblemTypeInfo(
                Long examId,
                Boolean isStarted,
                String problemType,
                Integer solvedProblemCount,
                Integer correctProblemCount,
                Integer incorrectProblemCount,
                Integer totalProblemCount
        ) {

            public static List<ProblemTypeInfo> toList(Map<String, Exam> problemMap) {
                List<ProblemTypeInfo> ret = new ArrayList<>();
                for (Map.Entry<String, Exam> examEntry: problemMap.entrySet()) {
                    if (examEntry.getKey().equals(ExamType.EVENLY.name())) continue;
                    ret.add(toDto(examEntry.getKey(), examEntry.getValue()));
                }
                return ret;
            }

            public static ProblemTypeInfo toDto(String problemType, Exam exam) {
                ProblemSummation problemSummation = exam.getProblemSummation();
                return new ProblemTypeInfo(exam.getId(),
                        !(exam instanceof TempExam),
                        problemType,
                        problemSummation.getSolvedProblemCount(),
                        problemSummation.getCorrectProblemCount(),
                        problemSummation.getIncorrectProblemCount(),
                        problemSummation.getTotalProblemCount());
            }
        }

        public static Response toDto(Entry entry, Map<String, Exam> examMap) {
            Exam evenlyExam = examMap.get(ExamType.EVENLY.name());
            int problemTypeCount = evenlyExam != null? examMap.size() - 1 : examMap.size();  // evenly exam 제외
            List<ProblemTypeInfo> problemTypeInfoList = ProblemTypeInfo.toList(examMap);

            if (evenlyExam == null) return new Response(entry,
                    null,
                    false,
                    false,
                    -1,
                    problemTypeCount,
                    problemTypeInfoList);

            return new Response(entry,
                    evenlyExam.getId(),
                    true,
                    evenlyExam.getIsFinished(),
                    evenlyExam.getLastSolvedProblem() != null? evenlyExam.getLastSolvedProblem() : -1,
                    problemTypeCount,
                    problemTypeInfoList);
        }
    }
}
