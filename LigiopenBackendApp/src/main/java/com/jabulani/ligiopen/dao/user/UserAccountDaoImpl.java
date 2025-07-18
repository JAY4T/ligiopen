package com.jabulani.ligiopen.dao.user;

import com.jabulani.ligiopen.model.user.Role;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class UserAccountDaoImpl implements UserAccountDao{
    private final EntityManager entityManager;
    @Autowired
    public UserAccountDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public UserAccount createUserAccount(UserAccount userAccount) {
        entityManager.persist(userAccount);
        return userAccount;
    }

    @Override
    public UserAccount updateUserAccount(UserAccount userAccount) {
        entityManager.merge(userAccount);
        return userAccount;
    }

    @Override
    public UserAccount getUserAccountById(Integer userId) {
        TypedQuery<UserAccount> query = entityManager.createQuery("from UserAccount where id = :userId", UserAccount.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    @Override
    public UserAccount getUserAccountByEmail(String email) {
        TypedQuery<UserAccount> query = entityManager.createQuery("from UserAccount where email = :email", UserAccount.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }


    @Override
    public Boolean existsByEmail(String email) {
        TypedQuery<UserAccount> query = entityManager.createQuery("from UserAccount where email = :email", UserAccount.class);
        query.setParameter("email", email);
        return !query.getResultList().isEmpty();
    }

    @Override
    public List<UserAccount> getAllUsers() {
        TypedQuery<UserAccount> query = entityManager.createQuery("from UserAccount", UserAccount.class);
        return query.getResultList();
    }

    @Override
    public List<UserAccount> getUsers(String username, String role) {
        // 1. Get the CriteriaBuilder and start a query for UserAccount
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserAccount> cq = cb.createQuery(UserAccount.class);
        Root<UserAccount> root = cq.from(UserAccount.class);

        // 2. Build predicates based on provided parameters
        List<Predicate> predicates = new ArrayList<>();

        if (username != null && !username.trim().isEmpty()) {
            // case-insensitive "like" match on username
            predicates.add(cb.like(
                    cb.lower(root.get("username")),
                    "%" + username.trim().toLowerCase() + "%"
            ));
        }

        if (role != null && !role.trim().isEmpty()) {
            try {
                // convert String to Role enum
                Role roleEnum = Role.valueOf(role.trim().toUpperCase());
                predicates.add(cb.equal(root.get("role"), roleEnum));
            } catch (IllegalArgumentException e) {
                // invalid role string: no matches
                return Collections.emptyList();
            }
        }

        // 3. Apply predicates (if any) and execute
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<UserAccount> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public UserAccount getUser(Integer userId) {
        TypedQuery<UserAccount> query = entityManager.createQuery("from UserAccount where id = :id", UserAccount.class);
        query.setParameter("id", userId);
        return query.getSingleResult();
    }

}
