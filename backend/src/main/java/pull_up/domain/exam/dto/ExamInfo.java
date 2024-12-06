package pull_up.domain.exam.dto;

import lombok.Getter;
import pull_up.domain.exam.ProblemSummation;
import pull_up.global.util.GlobalFormatter;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Member;

@Getter
public class ExamInfo {
    private final Long id;
    private final String examType;
    private final String userName;
    private final Integer totalProblemCount;
    private final Integer solvedProblemCount;
    private final Boolean isFinished;
    private final Integer correctProblemCount;
    private final Integer incorrectProblemCount;
    private final Integer score;
    private final String examsheetName;
    private final String startTime;
    private final String endTime;
    private final String duration;

    public ExamInfo(Exam exam) {
        Member member = exam.getMember();
        Examsheet examsheet = exam.getExamsheet();
        ProblemSummation problemSummation = exam.getProblemSummation();
        this.id = exam.getId();
        this.examType = exam.getExamType().getKorean();
        this.userName = member.getName();
        this.totalProblemCount = problemSummation.getTotalProblemCount();
        this.solvedProblemCount = problemSummation.getSolvedProblemCount();
        this.isFinished = exam.getIsFinished();
        this.correctProblemCount = problemSummation.getCorrectProblemCount();
        this.incorrectProblemCount = problemSummation.getIncorrectProblemCount();
        this.score = exam.getScore();
        this.examsheetName = examsheet != null? examsheet.getExamTitle() : "없음";
        this.startTime = exam.getStartTime().format(GlobalFormatter.KOREAN_TIME_FORMATTER);
        this.endTime = exam.getEndTime() != null? exam.getEndTime().format(GlobalFormatter.KOREAN_TIME_FORMATTER) : "종료되지 않음";
        this.duration = GlobalFormatter.formatMinuteSecond(exam.getDuration());
    }
}
