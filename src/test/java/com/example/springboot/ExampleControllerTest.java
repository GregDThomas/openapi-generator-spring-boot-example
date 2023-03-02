package com.example.springboot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapi.example.model.SearchObjectParamParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExampleController exampleController;

    @BeforeEach
    void setUp() {
        exampleController.clearRequests();
    }

    @Test
    void willStoreTheRequestWithNoParams() {
        final String response = restTemplate.getForObject(
            "http://localhost:%d/api/search".formatted(port),
            String.class
        );
        assertThat(response).contains("anything");
        assertThat(exampleController.getAllRequests())
            .containsExactly(new ExampleController.FullRequest(new SearchObjectParamParameter(), null));

    }

    @Test
    void willStoreTheRequestWithRegularParam() {
        final String response = restTemplate.getForObject(
            "http://localhost:%d/api/search?regular-param=foo".formatted(port),
            String.class
        );
        assertThat(response).contains("anything");
        assertThat(exampleController.getAllRequests())
            .containsExactly(new ExampleController.FullRequest(new SearchObjectParamParameter(), "foo"));
    }

    @Test
    void willStoreTheRequestWithExplodedParams() {
        final String response = restTemplate.getForObject(
            "http://localhost:%d/api/search?some-kebab-string=foo&some_snake_string=bar&someCamelString=foobar".formatted(port),
            String.class
        );
        assertThat(response).contains("anything");
        assertThat(exampleController.getAllRequests())
            .containsExactly(new ExampleController.FullRequest(
                new SearchObjectParamParameter()
                    .someKebabString("foo")
                    .someSnakeString("bar")
                    .someSnakeString("foobar"),
                null
            ));
    }
}