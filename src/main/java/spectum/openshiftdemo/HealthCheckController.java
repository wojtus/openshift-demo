package spectum.openshiftdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
class HealthCheckController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/hello")
	Mono<String> hello() {
		return Mono.just("Yep\n");
	}

	@GetMapping("/ready")
	Mono<String> ready() {
		logger.debug("Ready check");
		return Mono.just("ready");
	}

	@GetMapping("/alive")
	Mono<String> alive() {
		logger.debug("alive check");
		return Mono.just("alive");
	}

}
