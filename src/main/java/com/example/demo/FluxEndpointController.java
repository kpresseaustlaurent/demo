package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class FluxEndpointController {
    public static final int DELAY = 50;
    @GetMapping(value = "/flux-endpoint", produces = "application/json")
    public ResponseEntity<Flux<String>> getFluxData() {
        return ResponseEntity.ok().body(Flux.fromIterable(getMockStrings())
                .delayElements(Duration.ofMillis(DELAY))
                .map(data -> "{\"value\": \"" + data + "\"}"));
    }
    @GetMapping(value = "/paging-endpoint", produces = "application/json")
    public ResponseEntity<Page<String>> getStrings (
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "0") int page) throws InterruptedException {

        List<String> data = getMockStrings();
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return ResponseEntity.ok().body(createPage(data, pageRequest));
    }

    private Page<String> createPage(List<String> data, PageRequest pageRequest) throws InterruptedException {
        int totalSize = data.size();
        int startIndex = (int) pageRequest.getOffset();
        int endIndex = Math.min(startIndex + pageRequest.getPageSize(), totalSize);
        List<String> pageStrings = data.subList(startIndex, endIndex);
        Thread.sleep(DELAY * pageRequest.getPageSize());
        return new PageImpl<>(pageStrings, pageRequest, totalSize);
    }

    private List<String> getMockStrings() {
        List<String> allStrings = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            allStrings.add("String " + i);
        }
        return allStrings;
    }
}
