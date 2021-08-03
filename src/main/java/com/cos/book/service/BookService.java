package com.cos.book.service;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

//서비스는 빈 등록 유무를 잘 정해야함.
//기능을 정의할 ㅅ 있고 트랜잭션을 관리할 수 있기 때문에.
@Service
@RequiredArgsConstructor // final 이 붙은 변수들의 생성자를 자동으로 만들어준다. 자동으로 DI됨
public class BookService {
	private final BookRepository bookRepository;
	
	@Transactional//함수 종료시 commit, rollback을 결정
	public Book 저장하기(Book book) {
		 return bookRepository.save(book);
	}
	
	@Transactional(readOnly = true)//jpa에는 변경감지 기능이 있다. readonly걸면 그 기능이 꺼짐= 내부 연산이 줄어든다. update시의 정합성 유지
	//따라서 필요하진 않지만 유용성 때문에 걸어둔다. 단 insert의 팬텀데이터는 못막는다.
	public Book 한건가져오기(Long id) {
		 return bookRepository.findById(id).orElseThrow(()->{
			 return new IllegalArgumentException("id를 확인해주세요");
		});
	}
	
	@Transactional(readOnly = true)//select시마다 하는  것이 좋다.
	public List<Book> 모두가져오기() {
		 return bookRepository.findAll();
	}
	
	@Transactional
	public Book 수정하기(Long id,Book book) {
		//더티 체킹
		//db에 있는 실제 값을 가져오면 영속화됨
		Book bookEntity = bookRepository.findById(id).orElseThrow(()->{
			 return new IllegalArgumentException("id를 확인해주세요");
		});
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());
		 return bookEntity;
	}//함수종료 -> 트랜잭션 종료 -> 영속화 되어있는 데이터를 DB로 갱신(flush) -> commit == 더티체킹
	
	public String 삭제하기(Long id) {
		bookRepository.deleteById(id);
		 return "ok";
	}
}
