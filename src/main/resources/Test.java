import java.io.Serializable;
import java.lang.reflect.*;

public final class Test implements Serializable {
    private String username;
    private String password;
    private boolean isValidPassword;
    protected String name;
    protected static String privateData = "c";

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
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

    private final void setPassword(String password) {
        this.password = password;
    }

    public boolean logon(String pUsr, String pPsw) {
        if (pUsr.equals(username) && pPsw.equals(password)) {
            return true;
        }
        return false;
    }

    public void validateCredentials() {
        System.out.println("NOT YET IMPLEMENTED");
    }

    public void validate(String passwd) {
        if (passwd.equals("")) {
            throw new RuntimeException("Nope");
        }
    }

    public void foolMethod() {
        another.username = "";
    }

    public String hello() {
        return "Hi, I am " + name;
    }
}
