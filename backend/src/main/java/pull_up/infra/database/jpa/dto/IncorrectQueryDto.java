package pull_up.infra.database.jpa.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Problem;

@Getter
public class IncorrectQueryDto {
    private final Long examId;
    private final Entry entry;
    private final ExamType examType;
    private final Integer problemNumber;
    private final String solvedDate;
    private final String questionSubstring;

    @QueryProjection
    public IncorrectQueryDto(Exam exam, Problem problem, Answer answer) {
        this.examId = exam.getId();
        this.entry =  problem.getEntry();
        this.examType =  exam.getExamType();
        this.problemNumber =  answer.getProblemNumber();
        this.solvedDate =  answer.getSolvedDate();
        this.questionSubstring =  problem.getQuestionAsString();
    }
}
