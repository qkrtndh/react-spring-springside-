package com.cos.book.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

//service와 관련된 것만 메모리에 올림.
//서비스에 관련된 것이 bookreopository뿐임근데 얘를 메모리에 띄우면 연결된 db도 테스트하게 됨
//MockitoExtension로 가짜 객체로 만들 수 있다.

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

	@InjectMocks // 2. 이렇게하면 @mock로 등록된 모든 객체를 주입해준다. 따라서 아래의 repository를 주입받는다.
	private BookService bookService;

	@Mock // 1.최초에 bookservice는 빈 repository객체를 가지고 있다가 ioc주입받아 사용. 하지만 mock가상 환경이므로 주입받을
			// 수 없고 메모리에 mock로 띄운 상태
	private BookRepository bookRepository;

	@Test
	public void 저장하기_테스트() {
		Book book = new Book();
		book.setTitle("1");
		book.setAuthor("1");

		when(bookRepository.save(book)).thenReturn(book);
		
		Book bookEntity = bookService.저장하기(book);
		
		assertEquals(bookEntity,book);
	}

}
