package com.cos.book.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity//서버 실행시 Object Relation Mapping 이 된다.(테이블이 생성된다)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

	@Id//pk
	@GeneratedValue(strategy  = GenerationType.IDENTITY)//1씩 증가
	private Long id;
	private String title;
	private String author;
}
