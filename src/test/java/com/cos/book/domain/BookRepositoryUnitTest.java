package com.cos.book.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

//단위테스트(DB관련 bean 이 Ico등록)

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY)//가짜 db로 테스트 .NONE이면 실제 db로 테스트
@DataJpaTest//Repository를 ioc 등록
public class BookRepositoryUnitTest {
	//mock할필요 없음
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void save_테스트() {
		Book book = new Book(null,"1","1");
		
		Book bookEntity = bookRepository.save(book);
		
		assertEquals("1", bookEntity.getTitle());
	}
}
