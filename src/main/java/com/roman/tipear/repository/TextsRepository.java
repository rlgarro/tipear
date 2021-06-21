package com.roman.tipear.repository;

import com.roman.tipear.model.entity.Texts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TextsRepository extends JpaRepository<Texts, Long> {
    @Query("from Texts t where t.title = ?1")
    Texts findByTitle(String title);
}
