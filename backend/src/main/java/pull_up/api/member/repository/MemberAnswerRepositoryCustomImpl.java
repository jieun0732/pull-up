package pull_up.api.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import pull_up.api.member.entity.MemberAnswer;

public class MemberAnswerRepositoryCustomImpl implements MemberAnswerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MemberAnswer> findByMemberAndOptionalFilters(Long memberId, String entry,
        String category, String type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MemberAnswer> query = cb.createQuery(MemberAnswer.class);
        Root<MemberAnswer> root = query.from(MemberAnswer.class);

        List<Predicate> predicates = new ArrayList<>();

        // 'member' 객체의 'id'를 사용하여 memberId 필터링
        predicates.add(cb.equal(root.get("member").get("id"), memberId));

        // 조건이 null이 아니고 빈 값이 아닐 경우에만 필터링
        if (entry != null && !entry.isEmpty()) {
            predicates.add(cb.equal(root.get("problem").get("entry"), entry));
        }

        if (category != null && !category.isEmpty()) {
            predicates.add(cb.equal(root.get("problem").get("category"), category));
        }

        if (type != null && !type.isEmpty()) {
            predicates.add(cb.equal(root.get("problem").get("type"), type));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));


        return entityManager.createQuery(query).getResultList();
    }
}
