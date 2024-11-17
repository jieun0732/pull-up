package pull_up.domain.problem;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntryTest {

    @Test
    @DisplayName("항목 변환 테스트")
    void testConvertEntry() {
        assertThat(Entry.getEntry("수리")).isEqualTo(Entry.MATH);
        assertThat(Entry.getEntry("math")).isEqualTo(Entry.MATH);
        assertThat(Entry.getEntry("MATH")).isEqualTo(Entry.MATH);
        assertThat(Entry.getEntry("언어")).isEqualTo(Entry.LANGUAGE);
        assertThat(Entry.getEntry("    언어")).isEqualTo(Entry.LANGUAGE);
        assertThat(Entry.getEntry("language")).isEqualTo(Entry.LANGUAGE);
        assertThat(Entry.getEntry("LANGUAGE")).isEqualTo(Entry.LANGUAGE);
        assertThat(Entry.getEntry("추리")).isEqualTo(Entry.REASONING);
        assertThat(Entry.getEntry("reasoning")).isEqualTo(Entry.REASONING);
        assertThat(Entry.getEntry("REASONING")).isEqualTo(Entry.REASONING);
        assertThat(Entry.getEntry("공간지각능력")).isEqualTo(Entry.SPATIAL);
        assertThat(Entry.getEntry("spatial")).isEqualTo(Entry.SPATIAL);
        assertThat(Entry.getEntry("SPATIAL")).isEqualTo(Entry.SPATIAL);
    }
}