package pull_up.infra.database.entity.legacy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import pull_up.global.entity.BaseEntity;

import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@Entity
@Table(name = "member_legacy")
@SQLRestriction("is_deleted = false")
public class MemberL extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private boolean accessCheck;

    @Column
    private String role;

    @OneToMany(mappedBy = "memberL", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ExamL> examLList; // Member와 연결된 ExamInformation 리스트

    @OneToMany(mappedBy = "memberL", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<IncorrectAnswer> incorrectAnswers;

    @OneToMany(mappedBy = "memberL", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberAnswer> memberAnswers;

    protected MemberL() {}

    /**
     * 파라미터 생성자.
     */
    private MemberL(String name, String email, boolean accessCheck, String role) {
        this.name = name;
        this.email = email;
        this.accessCheck = accessCheck;
        this.role = role;
    }

    public static MemberL of(String name, String email, boolean accessCheck, String role) {
        return new MemberL(name, email, accessCheck, role);
    }

    public static MemberL of(String firstName, String lastName, String email, boolean accessCheck, String role) {
        return MemberL.of(getFullName(firstName,lastName), email, accessCheck, role);
    }

    /**
     <p>성과 이름을 붙여 객체 생성하는 메서드(한글은 반대로)</p>
     */
    private static String getFullName(String firstName, String lastName) {
        if (Pattern.matches("^[ㄱ-ㅎ가-힣]*$", firstName)) return lastName + firstName;
        else return firstName + " " + lastName;
    }

}
