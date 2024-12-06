package pull_up.infra.database.jpa.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import pull_up.infra.database.jpa.dto.SearchParam;

import static pull_up.infra.database.jpa.entity.QProblem.problem;

public class DynamicQuery {
    public static OrderSpecifier<?> problemOrder(SearchParam searchParam) {
        return switch (searchParam.sortType()) {
            case ID -> new OrderSpecifier<>(searchParam.sortOrder(), problem.id);
            case ENTRY -> new OrderSpecifier<>(searchParam.sortOrder(), problem.entry);
            case PROBLEM_TYPE -> new OrderSpecifier<>(searchParam.sortOrder(), problem.problemType);
            case CREATED_DATE -> new OrderSpecifier<>(searchParam.sortOrder(), problem.createdTime);
            case UPDATED_DATE -> new OrderSpecifier<>(searchParam.sortOrder(), problem.updatedTime);
            case ATTEMPT -> new OrderSpecifier<>(searchParam.sortOrder(), problem.totalAttempts);
            case CORRECT_RATE -> new OrderSpecifier<>(searchParam.sortOrder(), problem.incorrectRate);
        };
    }

    public static BooleanExpression problemSearch(SearchParam searchParam) {
        if (searchParam.keyword().equals("EMPTY")) return null;
        return switch (searchParam.searchType()) {
            case NONE -> null;
            case PROBLEM_TYPE -> problem.problemType.likeIgnoreCase("%" + searchParam.keyword() + "%");
            case QUESTION -> problem.questionSummary.likeIgnoreCase("%" + searchParam.keyword() + "%");
        };
    }

}
