package spectum.openshiftdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
public class Config {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public RouterFunction<ServerResponse> route() {
		RequestPredicate reqestPredicate = RequestPredicates.GET("/").//
				and(RequestPredicates.accept(MediaType.TEXT_PLAIN));
		RequestPredicate aliveReqestPredicate = RequestPredicates.GET("/alive");
		RequestPredicate readyReqestPredicate = RequestPredicates.GET("/ready");
		return RouterFunctions.route(reqestPredicate, this::hello).//
				and(RouterFunctions.route(aliveReqestPredicate, this::alive))
				.and(RouterFunctions.route(readyReqestPredicate, this::ready));
	}

	private Mono<ServerResponse> hello(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("Yep\n"));
	}

	private Mono<ServerResponse> ready(ServerRequest request) {
		logger.info("Ready check");
		return ServerResponse.ok().build();
	}

	private Mono<ServerResponse> alive(ServerRequest request) {
		logger.info("alive check");
		return ServerResponse.ok().build();
	}

}
