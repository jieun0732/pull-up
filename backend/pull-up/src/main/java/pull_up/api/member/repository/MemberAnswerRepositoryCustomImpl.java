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
    public List<MemberAnswer> findByMemberIdAndOptionalFilters(Long memberId, String entry,
        String category, String type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MemberAnswer> query = cb.createQuery(MemberAnswer.class);
        Root<MemberAnswer> root = query.from(MemberAnswer.class);

        List<Predicate> predicates = new ArrayList<>();

        // memberId는 필수 조건이므로 항상 추가합니다.
        predicates.add(cb.equal(root.get("memberId"), memberId));

        // 조건이 null이 아니고 빈 값이 아닐 경우에만 필터링
        if (entry != null && !entry.isEmpty()) {
            predicates.add(cb.equal(root.get("problemEntry"), entry));
        }

        if (category != null && !category.isEmpty()) {
            predicates.add(cb.equal(root.get("problemCategory"), category));
        }

        if (type != null && !type.isEmpty()) {
            predicates.add(cb.equal(root.get("problemType"), type));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
