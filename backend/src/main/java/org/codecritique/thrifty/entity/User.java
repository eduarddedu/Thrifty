package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User extends BaseEntity {
    /**
     * The email address of the user serves as the login username
     */
    @NotNull
    @Email(message = "Invalid email: '${validatedValue}'")
    private String username;

    /**
     * The password hash based on the chosen encoding algorithm
     */
    @NotNull
    @JsonIgnore
    private String password;

    @Column(name = "account_id")
    private Long accountId;

    public User() {
    }

    public User(String username, String password, Long accountId) {
        this.username = username;
        this.password = password;
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }


}
