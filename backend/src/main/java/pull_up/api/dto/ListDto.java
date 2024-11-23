package pull_up.api.dto;

import java.util.List;
import java.util.stream.Stream;

public record ListDto<T>(List<T> list) {
}
