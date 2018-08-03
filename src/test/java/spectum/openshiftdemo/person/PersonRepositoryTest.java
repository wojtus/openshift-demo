package spectum.openshiftdemo.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersonRepositoryTest {

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void testSave() throws Exception {
		Person entity = new Person();
		entity.setName("ME");

		personRepository.save(entity).block();

		Flux<Person> persons = personRepository.findAll();

		StepVerifier.create(persons).assertNext(person -> {
			assertEquals("ME", person.getName());
			assertNotNull(person.getId());
		}).expectComplete().verify();

	}

}
