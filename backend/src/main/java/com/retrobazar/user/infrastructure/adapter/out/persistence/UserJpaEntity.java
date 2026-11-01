package com.retrobazar.user.infrastructure.adapter.out.persistence;

import com.retrobazar.user.domain.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Apuntes del programador (A.P.):
 * Esta entidad representa la tabla users y permite que JPA transforme sus objetos
 * en filas de la base de datos.
 */
@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "new_product_email_notifications_enabled", nullable = false)
    private boolean newProductEmailNotificationsEnabled;

    protected UserJpaEntity() {
    }

    public UserJpaEntity(
            UUID id,
            String name,
            String email,
            String passwordHash,
            UserRole role,
            boolean newProductEmailNotificationsEnabled
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.newProductEmailNotificationsEnabled = newProductEmailNotificationsEnabled;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isNewProductEmailNotificationsEnabled() {
        return newProductEmailNotificationsEnabled;
    }
}
