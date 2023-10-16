package com.enodation.stream.api;

import com.enodation.stream.model.WeatherEvent;
import com.enodation.stream.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class RequestController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(path = "/weather-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<WeatherEvent> getWeatherEventStream() {
        return weatherService.streamWeather();
    }
}