package Model;

public class Users {
    
    private String userName;
    private String password;
    private int userId;
    private int active;
       
    // main constructor
    public Users(String userName, String password, int userId, int active) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.active = active;
    }
    
    // empty constructor
    public Users() {
        
    }
    
    //getters & setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
