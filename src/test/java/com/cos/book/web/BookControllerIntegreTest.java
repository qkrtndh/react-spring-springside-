package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//통합테스트
//webEnvironment = WebEnvironment.MOCK = 실제 톰켓이 아닌 다른 톰켓으로 테스트
//webEnvironment = WebEnvironment.RANDOM_POR = 실제 서블릿(톰켓)으로 테스트

//모든 bean 을 IoC 올리고 테스트
@Slf4j
@Transactional // 각 테스트 함수가 종료될 때 마다 트랜잭션을 rollback
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc // mockMvc를 메모리에 띄우기 위해 필요함.
public class BookControllerIntegreTest {
	@Autowired
	private MockMvc mockMvc;//

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER table book ALTER COLUMN id restart with 1").executeUpdate();
	}

	@Test
	public void save_테스트() throws Exception {
		// given 테스트를 하기 위한 준비 json으로 된 테스트 데이터 준비
		Book book = new Book(null, "spring", "soo");
		String content = new ObjectMapper().writeValueAsString(book);// object를 json으로 바꾸는 함수

		// when 테스트 실행
		ResultActions resultActions = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then(검증)
		resultActions.andExpect(status().isCreated()).andExpect(jsonPath("$.title").value("spring"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findAll_테스트() throws Exception {

		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "ssss", "ssss"));
		books.add(new Book(2L, "dddd", "dddd"));
		books.add(new Book(3L, "cccc", "cccc"));
		bookRepository.saveAll(books);
		// when
		ResultActions resultActions = mockMvc.perform(get("/book").accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8));

		// then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.[2].title").value("cccc"))
				.andExpect(jsonPath("$", Matchers.hasSize(3))).andExpect(jsonPath("$.[0].id").value(1L))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void findById_테스트() throws Exception {

		// given
		Long id = 2L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "ssss", "ssss"));
		books.add(new Book(2L, "dddd", "dddd"));
		books.add(new Book(3L, "cccc", "cccc"));
		bookRepository.saveAll(books);

		// when
		ResultActions resultActions = mockMvc
				.perform(get("/book/{id}", id).accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8));

		// then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("dddd"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void update_테스트() throws Exception {
		Long id = 3L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "ssss", "ssss"));
		books.add(new Book(2L, "dddd", "dddd"));
		books.add(new Book(3L, "cccc", "cccc"));
		bookRepository.saveAll(books);
		Book book = new Book(null, "1234", "soo");
		String content = new ObjectMapper().writeValueAsString(book);

		

		// when
		ResultActions resultActions = mockMvc.perform(put("/book/{id}", id).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("1234")).andExpect(jsonPath("$.id").value(3L))
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	public void delete_테스트() throws Exception {
		Long id = 1L;
		
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
