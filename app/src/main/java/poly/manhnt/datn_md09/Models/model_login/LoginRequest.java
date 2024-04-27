package poly.manhnt.datn_md09.Models.model_login;

public class LoginRequest {
    private String Username;
    private String Password;

    public LoginRequest(String username, String password) {
        Username = username;
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
