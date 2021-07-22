package entities;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class User {
    private int id;
    private String username;
    private String password;
    private String email;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


    public static class UserBuilder{
        private int id;
        private String username;
        private String password;
        private String email;

        public UserBuilder(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public UserBuilder withId(int id){
            this.id = id;
            return this;
        }

        public UserBuilder withEmail(String email){
            this.email = email;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.username = this.username;
            user.password = mask(this.password);
            user.email = this.email;
            return user;
        }

        private String mask (String string) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte [] digest = md.digest(string.getBytes());
                return DatatypeConverter.printHexBinary(digest);
            } catch (NoSuchAlgorithmException e) {
                return string;
            }

        }
    }

    private User() {}
}
