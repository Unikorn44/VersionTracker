// https://spring.io/guides/gs/testing-web/
// https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-server-htmlunit
// https://dzone.com/articles/spring-test-thymeleaf-viewspackage fr.versiontracker2.api.controller;

package fr.versiontracker2.api.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {


	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;

	/*
	@InjectMocks
	private MainController mainController;

	@Autowired
	private FilterChainProxy springSecurityFilter;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		assertNotNull(wac);
		assertNotNull(mainController);
		// Process mock annotations
		MockitoAnnotations.openMocks(this);
		// Setup Spring test in webapp-mode (same config as spring-boot)
		mockMvc = MockMvcBuilders.standaloneSetup(mainController).addFilters(springSecurityFilter).build();
	}
	*/
	@BeforeEach
	public void setup() {
		assertNotNull(wac);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	

	/**
	 * Test de la page index
	 */
	@Test
	void indexTest() {
		try {
			
			MvcResult result = mockMvc.perform(get("/index")).andExpect(status().isOk())
					.andExpect(view().name("index")).andReturn();
			System.out.println(result.getResponse().getContentAsString());
			assertTrue(result.getResponse().getContentAsString().contains("Bienvenue"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Ca aurait du passer !");
		}
	}
	/*
	@Test
	void triApplicationTest() {
		try {
			MvcResult result = this.mockMvc.perform(get("/triApplication")).andExpect(status().is3xxRedirection())
					// .andExpect(view().name("triApplication"))
					.andReturn();
			// assertTrue(result.getResponse().getContentAsString().contains("XMLRush"));
			assertNotNull(wac);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Ca aurait du passer (allumer RushServer, avant) !");
		}
	}

	public MockHttpSession makeAuthSession(String username, String... roles) {
		if (!StringUtils.hasText(username)) {
			username = "admin";
		}
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		Collection<GrantedAuthority> authorities = new HashSet<>();
		if (roles != null && roles.length > 0) {
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
		}
		// Authentication authToken = new
		// UsernamePasswordAuthenticationToken("azeckoski", "password", authorities); //
		// causes a NPE when it tries to access the Principal
		Principal principal = new NamedOAuthPrincipal(username, authorities, "key", "signature", "HMAC-SHA-1",
				"signaturebase", "token");
		Authentication authToken = new UsernamePasswordAuthenticationToken(principal, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authToken);
		return session;
	}

	@EqualsAndHashCode(callSuper = true)
	public static class NamedOAuthPrincipal extends ConsumerCredentials implements Principal {
		public String name;
		public Collection<GrantedAuthority> authorities;

		public NamedOAuthPrincipal(String name, Collection<GrantedAuthority> authorities, String consumerKey,
				String signature, String signatureMethod, String signatureBaseString, String token) {
			super(consumerKey, signature, signatureMethod, signatureBaseString, token);
			this.name = name;
			this.authorities = authorities;
		}

		@Override
		public String getName() {
			return name;
		}

		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}
	}
	*/
}
