package spectum.openshiftdemo.person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

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

}
