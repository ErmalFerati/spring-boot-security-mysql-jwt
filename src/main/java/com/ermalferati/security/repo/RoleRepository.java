package com.ermalferati.security.repo;

import java.util.Optional;

import com.ermalferati.security.model.Role;
import com.ermalferati.security.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);
}
