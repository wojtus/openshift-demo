package spectum.openshiftdemo.person;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import spectum.openshiftdemo.similarity.SimilarPair;

@RestController
@RequestMapping("/person")
class PersonController {

	private final PersonService personService;

	PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/all")
	Flux<Person> getAllPersons() {
		return personService.getPersons();

	}

	@GetMapping("/init")
	Flux<Person> getInitPersons() {
		return personService.initPersons();

	}

	@GetMapping("/similarity")
	Flux<SimilarPair> getSimilarPersons() {
		return personService.getSimlarPersons();

	}

}
