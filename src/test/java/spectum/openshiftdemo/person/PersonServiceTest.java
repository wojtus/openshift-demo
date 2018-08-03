package spectum.openshiftdemo.person;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {
	@Mock
	private PersonRepository personRepository;
	@InjectMocks
	private PersonService personService;

	@Test
	public void testGetPersons() throws Exception {
		Person testPerson = new Person();
		when(personRepository.findAll()).thenReturn(Flux.just(testPerson));
		Flux<Person> persons = personService.getPersons();
		StepVerifier.create(persons).assertNext(person -> {
			assertEquals(testPerson, person);
		}).expectComplete().verify();

	}

}
