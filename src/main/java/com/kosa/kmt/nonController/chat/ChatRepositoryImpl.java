package com.kosa.kmt.nonController.chat;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ChatRepositoryImpl implements ChatRepository {

    private final EntityManager em;

    public ChatRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    public Chat save(Chat chat)  {
        em.persist(chat);
        return chat;
    }

    @Override
    public List<Chat> findAll() {
        return em.createQuery("SELECT c FROM Chat c", Chat.class).getResultList();
    }

    @Override
    public void executeCleanup() {
        em.createStoredProcedureQuery("cleanup_chats").execute();
    }
}
