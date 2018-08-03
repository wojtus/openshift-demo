package spectum.openshiftdemo.person;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

interface PersonRepository extends ReactiveCrudRepository<Person, String> {

}
