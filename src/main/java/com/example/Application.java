package com.example;

import java.time.LocalDate;
import java.util.List;

import com.example.integration.db.PersonRepository;
import com.example.integration.db.entity.Address;
import com.example.integration.db.entity.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import se.sundsvall.dept44.ServiceApplication;

@ServiceApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner seedData(final PersonRepository personRepository) {
        return args -> {
            personRepository.saveAll(List.of(
                new Person("Kalle", "Karlsson", new Address("Gatan 123", "12345", "Staden"), "kalle.karlsson@gmail.com", LocalDate.of(1980, 1, 15)),
                new Person("Olle", "Karlsson", new Address("Vägen 456", "12345", "Staden"), "olle.karlsson@hotmail.com", LocalDate.of(1982, 5, 31)),
                new Person("Gun", "Jonsson", new Address("Postlåda 88", "34567", "Byn"), "gunjonsson147823@hotmail.com", LocalDate.of(1957, 12, 9)),
                new Person("Jon", "Persson", new Address("Stigen 55", "45678", "Hålan"), "jonp4455@gmail.com", LocalDate.of(1988, 6, 7))
            ));
        };
    }
}
