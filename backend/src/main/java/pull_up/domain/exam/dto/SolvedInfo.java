package pull_up.domain.exam.dto;

import pull_up.domain.exam.TempExam;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.*;

public record SolvedInfo() {
    public record Response(
            Entry entry,
            Boolean isEvenlyExamStarted,
            Boolean isEvenlyExamFinished,
            Integer problemTypeCount,
            List<ProblemTypeInfo> problemTypeInfos
    ) {
        public record ProblemTypeInfo(
                Long examId,
                Boolean isStarted,
                String problemType,
                Integer solvedProblemCount,
                Integer totalProblemCount
        ) {

            public static List<ProblemTypeInfo> toList(Map<String, Exam> problemMap) {
                List<ProblemTypeInfo> ret = new ArrayList<>();
                for (Map.Entry<String, Exam> examEntry: problemMap.entrySet()) {
                    ret.add(toDto(examEntry.getKey(), examEntry.getValue()));
                }
                return ret;
            }

            public static ProblemTypeInfo toDto(String problemType, Exam exam) {
                return new ProblemTypeInfo(exam.getId(),
                        !(exam instanceof TempExam),
                        problemType,
                        exam.getProblemSummation().getSolvedProblemCount(),
                        exam.getProblemSummation().getTotalProblemCount());
            }
        }

        public static Response toDto(Entry entry, List<Exam> exams, Map<String, Integer> problemTypes) {
            Exam evenlyExam = null;
            Map<String, Exam> problemTypeExamMap = new HashMap<>();

            for (Exam exam : exams) {
                switch (exam.getExamType()) {
                    case EVENLY -> {
                        evenlyExam = exam;
                    }
                    case BY_PROBLEM_TYPE -> {
                        problemTypeExamMap.put(exam.getAnswers().get(0).getProblem().getProblemType(), exam);
                    }
                    case MOCK_EXAM -> {
                        throw new IllegalArgumentException("Illegal Problem Type : " + exam.getExamType());
                    }
                }
            }

            for (Map.Entry<String, Integer> problemTypeEntry : problemTypes.entrySet()) {
                if (problemTypeExamMap.containsKey(problemTypeEntry.getKey())) continue;
                problemTypeExamMap.put(problemTypeEntry.getKey(), new TempExam(problemTypeEntry.getValue()));
            }

            return new Response(entry,
                    evenlyExam != null,
                    evenlyExam != null && evenlyExam.getIsFinished(),
                    problemTypeExamMap.size(),
                    ProblemTypeInfo.toList(problemTypeExamMap));
        }
    }
}
