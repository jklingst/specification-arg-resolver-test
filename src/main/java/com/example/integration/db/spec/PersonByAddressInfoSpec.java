package com.example.integration.db.spec;

import com.example.integration.db.entity.Person;
import org.springframework.data.jpa.domain.Specification;

import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@Or(value = {
    @Spec(params = "q", path = "address.street", spec = LikeIgnoreCase.class),
    @Spec(params = "q", path = "address.zipCode", spec = LikeIgnoreCase.class),
    @Spec(params = "q", path = "address.city", spec = LikeIgnoreCase.class),
})
public interface PersonByAddressInfoSpec extends Specification<Person> {

}
