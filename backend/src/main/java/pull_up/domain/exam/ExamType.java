package pull_up.domain.exam;

public enum ExamType {
    EVENLY, BY_PROBLEM_TYPE, MOCK_EXAM;

    public String getKorean() {
        switch (this) {
            case EVENLY -> {
                return "골고루";
            }
            case BY_PROBLEM_TYPE -> {
                return "유형별";
            }
            case MOCK_EXAM -> {
                return "모의고사";
            }
        }

        throw new IllegalStateException("not supported ExamType");
    }
}
