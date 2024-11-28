package pull_up.infra.database.jpa.dto;

import com.querydsl.core.types.Order;
import org.springframework.data.domain.Pageable;

import javax.swing.*;

public record SearchParam(
        SearchType searchType,
        String keyword,
        Order sortOrder,
        SortType sortType,
        Pageable pageable
) {
    public static SearchParam getSearchParam(String searchType, String keyword, String sortOrder, String sortType, Pageable pageable) {
        SearchType searchType1 = null;
        Order sortOrder1 = null;
        SortType sortType1 = null;
        if (searchType == null) searchType1 = SearchType.NONE;
        else searchType1 = SearchType.valueOf(searchType);
        if (keyword == null || keyword.isEmpty() || keyword.isBlank()) keyword = "EMPTY";
        if (sortOrder == null) sortOrder1 = Order.ASC;
        else sortOrder1 = Order.valueOf(sortOrder);
        if (sortType == null) sortType1 = SortType.ID;
        else sortType1 = SortType.valueOf(sortType);

        return new SearchParam(searchType1,keyword,sortOrder1,sortType1,pageable);
    }
}
