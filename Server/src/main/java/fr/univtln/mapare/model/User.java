package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"USERS\"")
@NamedQueries({
        @NamedQuery(name = "User.findById", query = "SELECT U FROM User U WHERE U.id = :id"),
        @NamedQuery(name = "User.findByName", query = "SELECT U FROM User U WHERE U.lastname = :lastname"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT U FROM User U WHERE U.email = :email"),
        @NamedQuery(name = "User.findAll", query = "SELECT U FROM User U")
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String firstname;

    @Column(name = "\"emailToken\"")
    private String emailToken;

    @Column(nullable = false)
    private Boolean confirmed;

    @Column(nullable = false)
    private Boolean admin;

    @Column(nullable = false)
    private Boolean banned;

    @Column(nullable = false)
    byte[] passwordHash;

    @Column(nullable = false)
    byte[] salt = new byte[16];

    @OneToMany
    @JoinTable(name = "\"STARTED_VOTES\"",
            joinColumns = @JoinColumn(name = "votemaker"),
            inverseJoinColumns = @JoinColumn(name = "vote"))
    private List<Vote> startedVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    @JoinTable(name = "\"PRIVATE_VOTES\"",
            joinColumns = @JoinColumn(name = "\"user\""),
            inverseJoinColumns = @JoinColumn(name = "\"vote\""))
    private List<Vote> privateVoteList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<VotedVote> votedVotes = new ArrayList<>();

    @Builder
    @SneakyThrows
    public User(String email, String lastname, String firstname, String password) {
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.emailToken = UUID.randomUUID().toString();
        this.confirmed = false;
        this.admin = false;
        this.banned = false;

        new SecureRandom().nextBytes(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        passwordHash = factory.generateSecret(spec).getEncoded();
    }

    public void addStartedVote(Vote vote) {
        if (!startedVotes.contains(vote))
            startedVotes.add(vote);
    }

    public void addPrivateVote(Vote vote) {
        if (!privateVoteList.contains(vote))
            privateVoteList.add(vote);
    }

    @SneakyThrows
    public boolean checkPassword(String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] submittedPasswordHash = factory.generateSecret(spec).getEncoded();
        return Arrays.equals(passwordHash, submittedPasswordHash);
    }
}
