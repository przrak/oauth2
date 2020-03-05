package com.stl.crm.repository;

import org.springframework.data.repository.CrudRepository;
import com.stl.crm.domain.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findById(Long customerId);
}
