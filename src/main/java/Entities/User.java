package Entities;

import javax.persistence.*;

@Entity
@Table(name="role")
public class User implements Entry {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "userid")
    private String name;
    private String password;
    private Role role;

    @Transient
    private boolean valid;

    public User() {

    }

    public User(String username, String password, Role role, boolean valid) {
        this.name = username;
        this.password = password;
        this.role = role;
        this.valid = valid;
    }

    public User(String username, String password) {
        this(username, password, null, false);
    }

    public String toString() {
            return Character.toUpperCase(this.name.charAt(0)) + this.name.substring(1);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User))
            return false;
        return this.name.equals(((User) obj).name) && this.password.equals(((User) obj).password);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /*public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
