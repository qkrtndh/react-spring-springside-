package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.sjavac.Log;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

//단위테스트
//controller관련 로직만 처리(controller, filter,controlleradvice)

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;// 주소호출해서 테스트하게 해주는 도구

	@MockBean // 가짜가 IoC환경에 bean 등록됨
	private BookService bookService;

	@Test
	public void save_테스트() throws Exception {
		// given 테스트를 하기 위한 준비 json으로 된 테스트 데이터 준비
		Book book = new Book(null, "spring", "soo");
		String content = new ObjectMapper().writeValueAsString(book);// object를 json으로 바꾸는 함수

		// 가짜 service라 동작이 제대로 되지 않을 것이므로 결과를 지정한다.
		when(bookService.저장하기(book)).thenReturn(new Book(1L, "spring", "soo"));

		// when 테스트 실행
		ResultActions resultActions = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));

		// then(검증)
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("spring"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_테스트() throws Exception {
		
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L,"ssss","ssss"));
		books.add(new Book(2L,"dddd","dddd"));
		when(bookService.모두가져오기()).thenReturn(books);
		
		//when
		ResultActions resultActions = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8));
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$",Matchers.hasSize(2)))
		.andExpect(jsonPath("$.[0].title").value("ssss"))
		.andDo(MockMvcResultHandlers.print());
		
	}
	@Test
	public void findById_테스트() throws Exception {
		Long id = 1L;
		when(bookService.한건가져오기(id)).thenReturn(new Book(1L,"java","soo"));
		
		//when
		ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8));
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("java"))
		.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void update_테스트() throws Exception {
		Long id = 1L;
		Book book = new Book(null, "1234", "soo");
		String content = new ObjectMapper().writeValueAsString(book);	
		
		when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "1234", "soo"));
		
		//when
		ResultActions resultActions = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("1234"))
		.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void delete_테스트() throws Exception {
		Long id = 1L;
			
		
		when(bookService.삭제하기(id)).thenReturn("ok");
		
		//when
		ResultActions resultActions = mockMvc.perform(delete("/book/{id}",id)
				.accept(MediaType.TEXT_PLAIN));
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = resultActions.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		assertEquals("ok", result);
	}
}

