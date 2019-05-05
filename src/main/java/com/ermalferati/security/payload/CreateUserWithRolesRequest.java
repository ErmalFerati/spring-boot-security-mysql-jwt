package com.ermalferati.security.payload;

import com.ermalferati.security.model.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class CreateUserWithRolesRequest extends CreateUserRequest {

    @NotNull
    @Size(min = 1, message = "User must have at least one role")
    private Set<Role> roles;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
