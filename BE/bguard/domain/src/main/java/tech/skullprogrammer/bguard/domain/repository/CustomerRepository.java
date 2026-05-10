package tech.skullprogrammer.bguard.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.skullprogrammer.bguard.domain.entity.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("select c from Customer c")
    public List<Customer> findAll();

}
