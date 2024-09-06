package pull_up.api.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.global.common.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "member")
@SQLRestriction("is_deleted = false")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String name;

    @Setter
    @Column
    private String email;

    @Setter
    @Column
    private boolean accessCheck;

    @Setter
    @Column
    private String role;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<ExamInformation> examInformations; // Member와 연결된 ExamInformation 리스트

    protected Member() {
    }

    /**
     * 파라미터 생성자.
     */
    private Member(String name, String email, boolean accessCheck, String role) {
        this.name = name;
        this.email = email;
        this.accessCheck = accessCheck;
        this.role = role;
    }

    /**
     * 파라미터로부터 멤버 엔티티 객체를 생성하는 함수.
     */
    public static Member of(String name, String email, boolean accessCheck, String role) {
        return new Member(name, email, accessCheck, role);
    }
}
