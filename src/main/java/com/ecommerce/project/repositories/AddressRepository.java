package com.ecommerce.project.repositories;

import com.ecommerce.project.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {

    @Query("SELECT c FROM Address c WHERE c.user.email = ?1")
    Optional<List<Address>> findByUserEmail(String email);
}
