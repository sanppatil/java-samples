package com.enodation.stream.service;

import com.enodation.stream.model.Weather;
import com.enodation.stream.model.WeatherEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);

    public Flux<WeatherEvent> streamWeather() {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(5));
        Flux<WeatherEvent> events = Flux.fromStream(
                Stream.generate(
                        () -> {
                            WeatherEvent weatherEvent = new WeatherEvent(new Weather(getTemperatures(), getHumidity(), getWindSpeed()), LocalDateTime.now());
                            //LOGGER.info("Weather - " + weatherEvent);
                            return weatherEvent;
                        }));
        return Flux.zip(events, interval, (key, value) -> key);
    }

    private String getWindSpeed() {
        String[] windSpeeds = "100 km/h,101 km/h,102 km/h,103 km/h,104 km/h".split(",");
        return windSpeeds[new Random().nextInt(windSpeeds.length)];
    }

    private String getHumidity() {
        String[] humidity = "40 %,41 %,42 %,42 %,44 %,45 %,46 %".split(",");
        return humidity[new Random().nextInt(humidity.length)];
    }

    private String getTemperatures() {
        String[] temperatures = "19 C,19.5 C,20 C,20.5 C,21 C,21.5 C,22 C,22.5 C,23 C,23.5 C,24 C".split(",");
        return temperatures[new Random().nextInt(temperatures.length)];
    }
}