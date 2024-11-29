package pull_up.infra.database.jpa.dto;

import com.querydsl.core.types.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

class SearchParamTest {

    @Test
    @DisplayName("기본 SearchParam 생성 테스트")
    void testConstructBasicSearchParam() {
        // given
        SearchParam basicSearchParam = new SearchParam(SearchType.NONE, "EMPTY", Order.DESC, SortType.ID, Pageable.unpaged());

        // when
        SearchParam searchParam = SearchParam.getSearchParam(null, null, null, null, Pageable.unpaged());

        // then
        assertThat(searchParam).usingRecursiveComparison().isEqualTo(basicSearchParam);
    }
}