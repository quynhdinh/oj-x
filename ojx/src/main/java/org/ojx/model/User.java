package org.ojx.model;

public class User {
    private int userId;
    private String userType;
    private String userName;
    private String password;
    private String email;
    private String name;
    private String country;
    private int rating;

    // Private constructor to be used by Builder
    private User(Builder builder) {
        this.userId = builder.userId;
        this.userType = builder.userType;
        this.userName = builder.userName;
        this.password = builder.password;
        this.email = builder.email;
        this.name = builder.name;
        this.country = builder.country;
        this.rating = builder.rating;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getRating() {
        return rating;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userType='" + userType + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", rating=" + rating +
                '}';
    }
    public static Builder builder() {
        return new Builder();
    }
    
    // Builder class
    public static class Builder {
        private int userId;
        private String userType;
        private String userName;
        private String password;
        private String email;
        private String name;
        private String country;
        private int rating = 0; // Default rating
        
        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder userType(String userType) {
            this.userType = userType;
            return this;
        }
        
        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder country(String country) {
            this.country = country;
            return this;
        }
        
        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }
        
        public User build() {
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Username is required");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (userType == null || userType.trim().isEmpty()) {
                throw new IllegalArgumentException("User type is required");
            }
            
            return new User(this);
        }
    }
}