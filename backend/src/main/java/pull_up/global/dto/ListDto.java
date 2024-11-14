package pull_up.global.dto;

import java.util.List;
import java.util.stream.Stream;

public record ListDto<T>(List<T> list) {
}
