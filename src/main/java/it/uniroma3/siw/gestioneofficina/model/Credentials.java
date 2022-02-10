package it.uniroma3.siw.gestioneofficina.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Credentials {
    public static final String DEFAULT_ROLE = "CLIENTE";
    public static final String ADMIN_ROLE = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, length = 100)
    private String password;

    @Transient
    private String confirmPassword;


    @Column(nullable = false, length = 10)
    private String role;

    @Column(updatable = false, nullable = false)
    private LocalDateTime creationTimestamp;

    @Column(nullable = false)
    private LocalDateTime lastUpdateTimestamp;

    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private User user;

    public Credentials() {}

    public Credentials(String userName, String password) {
        this();
        this.userName = userName;
        this.password = password;
    }

    public Credentials(String userName, String password, String role, User user) {
        this(userName, password);
        this.role = role;
        this.user = user;
    }

    public Credentials(String userName, String password, String role, LocalDateTime creationTimestamp, LocalDateTime lastUpdateTimestamp, User user) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.creationTimestamp = creationTimestamp;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.user = user;
    }

    @PrePersist
    protected void onPersist() {
        this.creationTimestamp = LocalDateTime.now();
        this.lastUpdateTimestamp = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdateTimestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public static String getDefaultRole() {
        return DEFAULT_ROLE;
    }

    public static String getAdminRole() {
        return ADMIN_ROLE;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
