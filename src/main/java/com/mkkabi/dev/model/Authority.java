package com.mkkabi.dev.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

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

    @Override
    public String getAuthority() {
        return authority;
    }
}
