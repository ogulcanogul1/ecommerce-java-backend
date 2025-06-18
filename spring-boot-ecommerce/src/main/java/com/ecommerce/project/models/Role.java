package com.ecommerce.project.models;

import com.ecommerce.project.models.enums.AppRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Data
@Table(name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "role_name")
        })
public class Role {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private AppRole roleName;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<User> users = new ArrayList<>();

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
