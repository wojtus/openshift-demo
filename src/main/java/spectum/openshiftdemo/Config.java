package spectum.openshiftdemo;

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

	@Bean
	public RouterFunction<ServerResponse> route() {
		RequestPredicate reqestPredicate = RequestPredicates.GET("/demo").//
				and(RequestPredicates.accept(MediaType.TEXT_PLAIN));
		return RouterFunctions.route(reqestPredicate, this::hello);
	}

	public Mono<ServerResponse> hello(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("Yep\n"));
	}

}
