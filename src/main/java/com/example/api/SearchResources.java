package com.example.api;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.example.integration.db.PersonRepository;
import com.example.integration.db.entity.Person;
import com.example.integration.db.spec.PersonByAddressInfoSpec;
import com.example.integration.db.spec.PersonByBasicInfoSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@RestController
@RequestMapping("/search")
class SearchResources {

    private final PersonRepository personRepository;

    SearchResources(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    ResponseEntity<List<Person>> search(
            @Or(value = {
                @Spec(params = "q", path = "firstName", spec = LikeIgnoreCase.class),
                @Spec(params = "q", path = "lastName", spec = LikeIgnoreCase.class),
                @Spec(params = "q", path = "emailAddress", spec = LikeIgnoreCase.class),
                @Spec(params = "q", path = "address.street", spec = LikeIgnoreCase.class),
                @Spec(params = "q", path = "address.zipCode", spec = LikeIgnoreCase.class),
                @Spec(params = "q", path = "address.city", spec = LikeIgnoreCase.class),
            })
            final Specification<Person> basicSpec, final HttpServletRequest request) {
        // Extract the query manually, to be able to perform a "like" query on the birth date by
        // manually combining the resolved specification with a new one, treating the birth date as
        // a string
        var queryParameter = request.getParameter("q");

        var specWithBirthDate = basicSpec.or((Specification<Person>) (root, query, cb) -> {
            var dateAsString = cb.function("TO_CHAR", String.class, root.get("birthDate"), cb.literal("YYYYMMDD"));

            return cb.createQuery(Person.class)
                .where(cb.like(dateAsString, "%" + queryParameter + "%"))
                .getRestriction();
        });

        var result = personRepository.findAll(specWithBirthDate);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-basic-info")
    ResponseEntity<List<Person>> searchByBasicInfo(final PersonByBasicInfoSpec spec) {
        var result = personRepository.findAll(spec);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }


    @GetMapping("/by-address-info")
    ResponseEntity<List<Person>> searchByAddressInfo(final PersonByAddressInfoSpec spec) {
        var result = personRepository.findAll(spec);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }
}
