package com.example.springboot;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapi.example.api.ApiApi;
import org.openapi.example.model.SearchObjectParamParameter;
import org.openapi.example.model.SomeReturnValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class ExampleController implements ApiApi {

    private final List<FullRequest> allRequests = new CopyOnWriteArrayList<>();

    public List<FullRequest> getAllRequests() {
        return Collections.unmodifiableList(allRequests);
    }

    public void clearRequests() {
        allRequests.clear();
    }

    @Override
    public ResponseEntity<SomeReturnValue> search(final SearchObjectParamParameter objectParam, final String regularParam) {
        final FullRequest fullRequest = new FullRequest(objectParam, regularParam);
        log.info("Received request: {}", fullRequest);
        allRequests.add(fullRequest);
        return ok()
            .body(new SomeReturnValue().someValue("anything"));
    }

    @Data
    @RequiredArgsConstructor
    public static class FullRequest {
        private final SearchObjectParamParameter explodedParameter;
        private final String regularParam;
    }
}
