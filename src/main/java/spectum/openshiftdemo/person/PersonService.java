package spectum.openshiftdemo.person;

import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
class PersonService {

	private final PersonRepository personRepository;

	PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	Flux<Person> getPersons() {
		return personRepository.findAll();
	}

	void initPersons() {
		Stream<Person> personStream = Stream.generate(this::createRandomPerson).limit(1000);
		Publisher<Person> personPublisher = Flux.fromStream(personStream);
		personRepository.saveAll(personPublisher);
	}

	private Person createRandomPerson() {
		Person person = new Person();
		return person;

	}

}
