package com.softserve.itacademy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "autorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "authority")
    private String authority;

//    @ManyToMany
//    @JoinTable(name = "user_authorities",
//            joinColumns = @JoinColumn(name = "authority_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private Collection<User> users;

    // constructor, getters and setters
    @Override
    public String getAuthority() {
        return authority;
    }
}
