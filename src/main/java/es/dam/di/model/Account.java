package es.dam.di.model;

import java.time.LocalDate;
import java.util.Objects;

public class Account {
    private Long id;
    private String userName;
    private Email email;
    private String password;
    private LocalDate registrationDate;

    public Account() {
    }

    public Account(String userName, Email email, String password, LocalDate registrationDate) {
        this(null, userName, email, password, registrationDate);
    }

    public Account(Long id, String userName, Email email, String password, LocalDate registrationDate) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(userName, account.userName) &&
                Objects.equals(email, account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email=" + email +
                ", registrationDate=" + registrationDate +
                '}';
    }
}