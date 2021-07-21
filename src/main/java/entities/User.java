package entities;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

        public User build(){
            User user = new User();
            user.id = this.id;
            user.username = this.username;
            user.password = this.password;
            user.email = this.email;
            return user;
        }
    }

    private User() {}
}
