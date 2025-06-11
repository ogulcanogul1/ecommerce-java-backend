package com.ecommerce.project.repositories;

import com.ecommerce.project.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
