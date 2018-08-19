package spectum.openshiftdemo.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import spectum.openshiftdemo.similarity.SimilarityFinder;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {
	private static final ZoneId DEFAULT_TIME_ZONE = ZoneId.systemDefault();
	private final LocalDate referenceDate = LocalDate.of(2000, 1, 1);
	private final Clock clock = Clock.fixed(referenceDate.atStartOfDay().atZone(DEFAULT_TIME_ZONE).toInstant(),
			DEFAULT_TIME_ZONE);
	private final Person testPerson = new Person();

	@Mock
	private PersonRepository personRepository;
	@Mock
	private SimilarityFinder similarityFinder;

	private PersonService personService;

	@Before
	public void initService() {
		personService = new PersonService(personRepository, similarityFinder, clock);
	}

	@Test
	public void testGetPersons() throws Exception {
		when(personRepository.findAll()).thenReturn(Flux.just(testPerson));
		Flux<Person> persons = personService.getPersons();
		StepVerifier.create(persons).assertNext(person -> {
			assertEquals(testPerson, person);
		}).expectComplete().verify();

	}

	@Test
	public void testGetRandomValue() throws Exception {
		List<String> list = Arrays.asList("A", "B", "C");
		Set<String> hits = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			String value = personService.getRandomValue(list);
			assertNotNull(value);
			hits.add(value);
		}
		for (String string : list) {
			assertTrue(hits.contains(string));
		}

	}

	@Test
	public void testGetRandomBirthDate() throws Exception {
		for (int i = 0; i < 100; i++) {
			LocalDate birthDate = personService.getRandomBirthDate();
			assertNotNull(birthDate);
			assertTrue(birthDate.isBefore(referenceDate) || birthDate.isEqual(referenceDate));
		}

	}

	@Test
	public void testGetRandomCitiesOfLiving() throws Exception {
		LocalDate birthDate = LocalDate.of(1970, 6, 1);
		List<String> citiesOfLiving = personService.getRandomCitiesOfLiving(birthDate);
		assertNotNull(citiesOfLiving);
	}

	@Test
	public void testInitPersons() throws Exception {
		when(personRepository.saveAll(ArgumentMatchers.<Publisher<Person>>any())).//
				thenReturn(Flux.just(testPerson));

		Flux<Person> persons = personService.initPersons();
		StepVerifier.create(persons).//
				expectNext(testPerson).//
				expectComplete().verify();
	}

	@Test
	public void testCreateRandomPerson() throws Exception {
		assertNotNull(personService.createRandomPerson());
	}

}
