package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    private final EntityManager em;

    public CategoryRepositoryImpl(EntityManager em){
        this.em = em;
    }
    /*
    카테고리 추가
     */
    @Override
    public Category saveCategory(Category category) {
        em.persist(category);
        return category;
    }

    /*
    카테고리 삭제
     */
    @Override
    public Category deleteCategory(Category category) {
        em.remove(category);
        return category;
    }

    /*
    특정 보드 안에 특정 카테고리 찾기
     */

    @Override
    public Optional<Category> findByName(String name, Long boardId) {
        List<Category> result = em.createQuery("SELECT c FROM Category c WHERE c.name = :name AND c.board.boardId = :boardId", Category.class)
                .setParameter("name", name)
                .setParameter("boardId", boardId)
                .getResultList();
        return result.stream().findAny();
    }


    /*
    특정 보드 안의 모든 카테고리 조회
     */

    @Override
    public List<Category> findByBoard(Board board){
        return em.createQuery("SELECT c FROM Category c WHERE c.board.boardId = :boardId", Category.class)
                .setParameter("boardId", board.getBoardId())
                .getResultList();
    }

    @Override
    public List<Category> findByBoardId(Long boardId){
        return em.createQuery("SELECT c FROM Category c WHERE c.board.boardId = :boardId", Category.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        Category category = em.find(Category.class, categoryId);
        return Optional.ofNullable(category);
    }

    @Override
    public Optional<Category> findByIdAndBoard(Long id, Board board) {
        String query = "SELECT c FROM Category c WHERE c.categoryId = :categoryId AND c.board = :board";
        TypedQuery<Category> typedQuery = em.createQuery(query, Category.class);
        typedQuery.setParameter("categoryId", id);
        typedQuery.setParameter("board", board);
        try {
            Category category = typedQuery.getSingleResult();
            return Optional.ofNullable(category);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    /*
    모든 카테고리 목록 가져오기
     */
    @Override
    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }


}
