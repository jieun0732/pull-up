package pull_up.api.problem.dto;

import java.io.Serializable;

public record ProblemTypeResultDto(String entry, int totalProblems, int correctProblems) implements Serializable {

    public static ProblemTypeResultDto of(String entry, int totalProblems, int correctProblems) {
        return new ProblemTypeResultDto(entry, totalProblems, correctProblems);
    }
}