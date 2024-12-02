package pull_up.infra.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pull_up.domain.auth.Role;
import pull_up.domain.auth.SNSProvider;

import java.util.regex.Pattern;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean tutorialFinished;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String snsId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SNSProvider snsProvider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private Member(Boolean tutorialFinished, String name, String email, String snsId, SNSProvider snsProvider, Role role) {
        this.tutorialFinished = tutorialFinished;
        this.name = name;
        this.email = email;
        this.snsId = snsId;
        this.snsProvider = snsProvider;
        this.role = role;
    }

    public String getPrivateEmail() {
        if (email.split("@")[1].contains("private")) return "CONCEALED_EMAIL";
        return email;
    }

    public void finishTutorial() {
        this.tutorialFinished = true;
    }

    public static Member getDeletedMember() {
        Member member = new Member(false, "DELETED_USER", "DELETED_USER", "DELETED_USER", SNSProvider.NONE, Role.NONE);
        member.setId(-1L);
        return member;
    }

    public static Member getFirstLoginMember(String name, String email, String snsId, SNSProvider snsProvider) {
        return new Member(false, name, email, snsId, snsProvider, Role.USER);
    }

    public static Member getFirstLoginMember(String firstName, String lastName, String email, String snsId, SNSProvider snsProvider) {
        return getFirstLoginMember(getFullName(firstName,lastName), email, snsId, snsProvider);
    }

    private static String getFullName(String firstName, String lastName) {
        if (Pattern.matches("^[ㄱ-ㅎ가-힣]*$", firstName)) return lastName + firstName;
        else return firstName + " " + lastName;
    }
}
