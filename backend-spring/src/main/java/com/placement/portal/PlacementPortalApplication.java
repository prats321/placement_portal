package com.placement.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the whole application.
 *
 * @SpringBootApplication turns on Spring Boot's "auto-configuration": it scans
 * this package (and sub-packages) for components like our Controller and
 * Repository, wires them together, and starts an embedded Tomcat web server.
 */
@SpringBootApplication
public class PlacementPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementPortalApplication.class, args);
    }
}
