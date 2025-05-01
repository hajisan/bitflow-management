package user;

public class User {

    private int userId;
    private String firstName, lastName, email, passwordHash;
    private Role role;

    public User(int userId, String firstName, String lastName, String email, String passwordHash, Role role) {
        setUserId(userId);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPasswordHash(passwordHash);
        setRole(role);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String toString() {
        return String.format("""
                ID: %d
                First Name: %s
                Last Name : %s
                E-mail    : %s
                Role      : %e
                Password  : %s
                """, userId, firstName, lastName, email, role, passwordHash);
    }



}
