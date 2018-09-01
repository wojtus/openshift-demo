package spectum.openshiftdemo.person;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import spectum.openshiftdemo.similarity.SimilarPair;

@RunWith(SpringRunner.class)
@WebFluxTest(PersonController.class)
public class PersonControllerTest {

	@Autowired
	private WebTestClient webClient;
	@MockBean
	private PersonRepository personRepository;
	@MockBean
	private PersonService personService;

	@Test
	public void testGetAllPersons() throws Exception {
		webClient.get().uri("/person/all").accept(MediaType.APPLICATION_JSON).//
				exchange().expectStatus().isOk();
	}

	@Test
	public void testGetInitPersons() throws Exception {
		webClient.get().uri("/person/init").accept(MediaType.APPLICATION_JSON).//
				exchange().expectStatus().isOk();
	}

	@Test
	public void testGetSimilarity() throws Exception {
		Person personA = new Person();
		Person personB = new Person();
		Integer similarity = Integer.valueOf(1);
		SimilarPair similarPairA = new SimilarPair(personA, personB, similarity);
		Flux<SimilarPair> similarPersons = Flux.just(similarPairA);
		when(personService.getSimlarPersons()).thenReturn(similarPersons);
		Consumer<EntityExchangeResult<byte[]>> consumer = (ee) -> {
			String body = new String(ee.getResponseBody(), StandardCharsets.UTF_8);
			assertThat(body, containsString("personA"));
			assertThat(body, containsString("personB"));
		};
		webClient.get().uri("/person/similarity").accept(MediaType.APPLICATION_JSON).//
				exchange().expectStatus().isOk().expectBody().consumeWith(consumer);
	}

}
