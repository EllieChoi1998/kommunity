package com.kosa.kmt.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Post> findAllByOrderByPostDateDesc() {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p ORDER BY p.postDate DESC", Post.class);
        return query.getResultList();
    }

    @Override
    public List<Post> findAllByOrderByPostDateAsc() {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p ORDER BY p.postDate ASC", Post.class);
        return query.getResultList();
    }

}
