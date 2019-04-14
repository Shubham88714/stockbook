package com.virtusa.it.stockbookproductservice.category;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.virtusa.stockbookproductservice.StockBookProductServiceApplication;
import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.resource.CategoryResource;
import com.virtusa.stockbookproductservice.service.CategoryService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { StockBookProductServiceApplication.class })
public class CategoryIntegrationTest {

	@Autowired
	WebApplicationContext wac;

	private MockMvc mockMvc;

	@Mock
	CategoryService categoryService;

	@InjectMocks
	CategoryResource categoryResource;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	public String getUrlAfterSaving(Category category) throws Exception {
		return mockMvc
				.perform(MockMvcRequestBuilders.post("/api/category").contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(category)))
				.andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getHeader("Location");
	}

	// save category

	@Test
	public void saveCategory() throws Exception {
		Category category = new Category("a");
		Category category1 = new Category("b");

		getUrlAfterSaving(category);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/category").contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(category1))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn().getResponse().getHeader("Location");

	}

	// get list of categories

	@Test
	public void getListOfCategory() throws Exception {
		Category category = new Category("c");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/category").contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(category))).andExpect(MockMvcResultMatchers.status().isCreated());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("f")))

				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("b")))

				.andExpect(MockMvcResultMatchers.jsonPath("$[2].id", is(3)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].name", is("z")));

	}

	// get category by id

	@Test
	public void getCategoryByid() throws Exception {
		String uri = getUrlAfterSaving(new Category("d"));

		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(5)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", is("d")));
	}

	// update by id

	@Test
	public void UpdateById() throws Exception {

		Category category = new Category(1L, "f");
		String uri = getUrlAfterSaving(category);

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> " + uri);

		mockMvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(category))).andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", is("f")));
	}

	// delete category by id

	@Test
	public void deleteById() throws Exception {
		String uri = getUrlAfterSaving(new Category("z"));

		mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNoContent())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

	}

	// negative test cases

	@Test
	public void getByWrongid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/category/45784"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void deleteCategoryByWrongId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/category/45784"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void updateCategoryByWrongId() throws Exception {
		Category category = new Category(1L, "f");

		mockMvc.perform(MockMvcRequestBuilders.put("/api/category/468546").contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(category)));
	}
}
