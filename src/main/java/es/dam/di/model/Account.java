package es.dam.di.model;

import java.time.LocalDate;

public class Account {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private LocalDate registrationDate;

    public Account() {
    }

    public Account(String userName, String email, String password, LocalDate registrationDate) {
        this(null, userName, email, password, registrationDate);
    }

    public Account(Long id, String userName, String email, String password, LocalDate registrationDate) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
}
