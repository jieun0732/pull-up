package pull_up.infra.database.jpa.dto;

import com.querydsl.core.types.Order;
import org.springframework.data.domain.Pageable;

public record SearchParam(
        SearchType searchType,
        String keyword,
        Order sortOrder,
        SortType sortType,
        Pageable pageable
) {
    public static SearchParam getSearchParam(String searchType, String keyword, String sortOrder, String sortType, Pageable pageable) {
        return new SearchParam(
                searchType != null? SearchType.valueOf(searchType) : SearchType.NONE,
                !(keyword == null || keyword.isBlank())? keyword : "EMPTY",
                sortOrder != null? Order.valueOf(sortOrder): Order.DESC,
                sortType != null? SortType.valueOf(sortType) : SortType.ID,
                pageable);
    }
}
