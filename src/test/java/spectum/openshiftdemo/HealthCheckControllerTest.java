package spectum.openshiftdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import spectum.openshiftdemo.HealthCheckController;
import spectum.openshiftdemo.person.PersonRepository;

@RunWith(SpringRunner.class)
@WebFluxTest(HealthCheckController.class)
public class HealthCheckControllerTest {

	@Autowired
	private WebTestClient webClient;

	@MockBean
	private PersonRepository personRepository;

	@Test
	public void testHello() throws Exception {
		webClient.get().uri("/hello").//
				exchange().expectStatus().isOk().//
				expectBody(String.class).isEqualTo("Yep\n");

	}

	@Test
	public void testReady() throws Exception {
		webClient.get().uri("/ready").//
				exchange().expectStatus().isOk().//
				expectBody(String.class).isEqualTo("ready");
	}

	@Test
	public void testAlive() throws Exception {
		webClient.get().uri("/alive").//
				exchange().expectStatus().isOk().//
				expectBody(String.class).isEqualTo("alive");
	}

}
