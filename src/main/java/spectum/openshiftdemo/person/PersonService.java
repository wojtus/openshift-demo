package spectum.openshiftdemo.person;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import spectum.openshiftdemo.similarity.SimilarPair;
import spectum.openshiftdemo.similarity.SimilarityFinder;

@Service
class PersonService {

	private static final List<String> CITIES = Arrays.asList("Berlin", "Hamburg", "München", "Köln",
			"Frankfurt am Main", "Stuttgart", "Düsseldorf", "Dortmund", "Essen", "Leipzig");
	private static final List<String> FORENAMES = Arrays.asList("Mia", "Ben", "Emma", "Jonas", "Hannah", "Leon",
			"Sophia", "Elias", "Anna", "Fynn");
	private static final List<String> SURNAMES = Arrays.asList("Müller", "Schmidt", "Schneider", "Fischer", "Weber",
			"Meyer", "Wagner", "Becker", "Schulz", "Hoffmann");
	private final PersonRepository personRepository;

	private final Clock clock;
	private final SimilarityFinder similarityFinder;

	@Autowired
	PersonService(PersonRepository personRepository, SimilarityFinder similarityFinder) {
		this(personRepository, similarityFinder, Clock.systemDefaultZone());

	}

	PersonService(PersonRepository personRepository, SimilarityFinder similarityFinder, Clock clock) {
		this.personRepository = personRepository;
		this.clock = clock;
		this.similarityFinder = similarityFinder;
	}

	Flux<Person> getPersons() {
		return personRepository.findAll();
	}

	Flux<SimilarPair> getSimlarPersons() {
		Flux<Person> all = personRepository.findAll();
		Flux<SimilarPair> similarPairs = all.collectList().map(similarityFinder::getSimilarPairs)
				.flatMapIterable(Function.identity());
		return similarPairs;
	}

	Flux<Person> initPersons() {
		Stream<Person> personStream = Stream.generate(this::createRandomPerson).limit(10);
		Publisher<Person> personPublisher = Flux.fromStream(personStream);
		Flux<Person> all = personRepository.saveAll(personPublisher);
		return all;
	}

	Person createRandomPerson() {
		String forename = getRandomForename();
		String surname = getRandomSurname();
		LocalDate birthDate = getRandomBirthDate();
		List<String> citiesOfLiving = getRandomCitiesOfLiving(birthDate);

		Person person = new Person();
		person.setForename(forename);
		person.setSurname(surname);
		person.setCitiesOfLiving(citiesOfLiving);
		person.setBirthDate(birthDate);
		return person;

	}

	List<String> getRandomCitiesOfLiving(LocalDate birthDate) {
		long yearsOfLiving = ChronoUnit.YEARS.between(birthDate, LocalDate.now(clock));
		double averageYearsOfLivingInACity = Math.abs(ThreadLocalRandom.current().nextGaussian() * 4 + 16);
		int numberOfCities = (int) (yearsOfLiving / averageYearsOfLivingInACity) + 1;
		List<String> citiesOfLiving = Stream.generate(() -> getRandomValue(CITIES)).limit(numberOfCities)
				.collect(Collectors.toList());
		return citiesOfLiving;
	}

	LocalDate getRandomBirthDate() {
		Random random = ThreadLocalRandom.current();
		while (true) {
			int year = LocalDate.now(clock).getYear() - random.nextInt(100) - 1;
			int month = random.nextInt(13);
			int dayOfMonth = random.nextInt(32);
			try {
				return LocalDate.of(year, month, dayOfMonth);
			} catch (DateTimeException e) {
				// ignore
			}
		}
	}

	private String getRandomSurname() {
		return getRandomValue(SURNAMES);
	}

	private String getRandomForename() {
		return getRandomValue(FORENAMES);
	}

	<T> T getRandomValue(List<T> list) {
		double stdev = (double) list.size() / 2;
		int randomIndex = (int) Math.abs(ThreadLocalRandom.current().nextGaussian() * stdev);
		int index = Math.min(randomIndex, list.size() - 1);
		T value = list.get(index);
		return value;
	}

}
