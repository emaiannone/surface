public class Test {
    private String name;
    public String username;
    private String password;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String pUsername, String pPassword) {
        if (pUsername.equals(username) && pPassword.equals(password)) {
            return true;
        }
        return false;
    }

    public void foolPassword() {
        String password = "";
        System.out.println(password);
    }

    public void foolUsername() {
        another.username = "";
    }

    public String hello() {
        return "Hi, I am " + name;
    }
}
