package com.example.springbootchatserver.model;

public class User {
    // Variabel til at gemme brugernavnet
    private String username;
    // Variabel til at gemme adgangskoden
    private String password;

    // Tom constructor, som opretter en User uden at sætte værdier
    public User() {

    }

    // Constructor, som opretter en User med brugernavn og adgangskode
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter til at hente brugernavnet
    public String getUsername() {
        return username;
    }

    // Setter til at ændre brugernavnet
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter til at hente adgangskoden
    public String getPassword() {
        return password;
    }

    // Setter til at ændre adgangskoden
    public void setPassword(String password) {
        this.password = password;
    }

    // Override af toString()-metoden til at returnere en streng-repræsentation af User-objektet
    @Override
    public String toString() {
        // Returnerer en streng, der viser brugernavn og adgangskode
        return "User{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
