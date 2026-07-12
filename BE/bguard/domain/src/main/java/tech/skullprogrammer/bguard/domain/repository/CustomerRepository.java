package tech.skullprogrammer.bguard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.skullprogrammer.bguard.domain.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    @Query("select c from Customer c")
//    public List<Customer> findAll();

    boolean existsByExternalCode(String externalCode);

    Customer findByExternalCode(String externalCode);
}
