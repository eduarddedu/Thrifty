package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @NotNull
    @Email(message = "Invalid email: '${validatedValue}'")
    private String username;

    @NotNull
    private String password;

    @Column(name = "account_id")
    private Long accountId;

    public User(String username, String password, Long accountId) {
        this.username = username;
        this.password = password;
        this.accountId = accountId;
    }

    @Override
    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return accountId == null ? null :
                Collections.singleton(new SimpleGrantedAuthority(accountId.toString()));
    }

}
