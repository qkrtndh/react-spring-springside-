package com.cos.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository 생략 가능. 자동 bean 등록, CRUD 자체 내장
public interface BookRepository extends JpaRepository<Book, Long>{
	
}
