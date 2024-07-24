package com.kosa.kmt.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeOrHateRepositoryImpl implements PostLikeOrHateRepository {

    @PersistenceContext
    private final EntityManager em;


}
