package com.koreait.jejuguide.repository;

import com.koreait.jejuguide.domain.BoardPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    @Query("""
        select p from BoardPost p
        where (:q is null or :q = ''
           or lower(p.title)      like lower(concat('%', :q, '%'))
           or p.content           like concat('%', :q, '%')          
           or lower(p.authorName) like lower(concat('%', :q, '%')))
        order by p.id desc
    """)
    List<BoardPost> search(@Param("q") String q);
    
    @Query(value = """
    	    select p from BoardPost p
    	    where (:q is null or :q = ''
    	       or lower(p.title)      like lower(concat('%', :q, '%'))
    	       or p.content           like concat('%', :q, '%')
    	       or lower(p.authorName) like lower(concat('%', :q, '%')))
    	    order by p.id desc
    	""",
    	countQuery = """
    	    select count(p) from BoardPost p
    	    where (:q is null or :q = ''
    	       or lower(p.title)      like lower(concat('%', :q, '%'))
    	       or p.content           like concat('%', :q, '%')
    	       or lower(p.authorName) like lower(concat('%', :q, '%')))
    	""")
    Page<BoardPost> search(@Param("q") String q, Pageable pageable);
}
