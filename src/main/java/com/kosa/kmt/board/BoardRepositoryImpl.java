package com.kosa.kmt.board;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BoardRepositoryImpl implements BoardRepository {

    private final EntityManager em;

    public BoardRepositoryImpl(EntityManager em){
        this.em = em;
    }
    /*
    보드 추가
     */
    @Override
    public Board saveBoard(Board board) {
        em.persist(board);
        return board;
    }

    /*
    보드 삭제
     */
    @Override
    public Board deleteBoard(Board board) {
        em.remove(board);
        return board;
    }

    /*
    보드의 이름 중복 여부 찾기
     */

    @Override
    public Optional<Board> findByName(String name) {
        List<Board> result = em.createQuery("SELECT b FROM Board b WHERE b.name = :name", Board.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }



    /*
    모든 모드 목록 가져오기
     */
    @Override
    public List<Board> findAll() {
        return em.createQuery("SELECT b FROM Board b", Board.class).getResultList();
    }
}