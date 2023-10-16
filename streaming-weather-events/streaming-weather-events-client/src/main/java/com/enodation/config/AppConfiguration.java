package com.enodation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class AppConfiguration {

    private String applicationName;

    private String streamUrl;

}
