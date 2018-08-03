package com.pestov.testexercise.models;

import com.pestov.testexercise.models.enums.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
public class CustomUser {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(name="is_active")
    private boolean isActive;

    public CustomUser() {

    }
    public CustomUser(String email, String password) {
        this.email = email;
        this.password = password;
        this.isActive = false;
    }

    public Long getId() {
        return id;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public Collection<Role> getRoles() {
		Collection<Role> result = new ArrayList<>();
		((ArrayList<Role>) result).add(1, Role.ROLE_USER);
		return result;
	}
}
