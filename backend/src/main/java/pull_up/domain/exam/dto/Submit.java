package pull_up.domain.exam.dto;

public record Submit() {
    public record Request(
            Long examId,
            Integer problemNumber,
            Integer submitAnswer
    ){

    }
}
