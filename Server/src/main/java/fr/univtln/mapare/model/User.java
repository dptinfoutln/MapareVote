package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The type User.
 */
@Data
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = User.class)
@Table(name = "\"USERS\"")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "SELECT U FROM User U WHERE U.email = :email"),
        @NamedQuery(name = "User.findAll", query = "SELECT U FROM User U")
})
public class User implements Serializable, Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String firstname;

    @JsonIgnore
    @Column(name = "\"emailToken\"")
    private String emailToken = UUID.randomUUID().toString();

    @Column(nullable = false)
    private boolean confirmed;

    @Column(nullable = false)
    private boolean admin;

    @Column(nullable = false)
    private boolean banned;

    /**
     * The Password hash.
     */
    @JsonIgnore
    @Column(nullable = false)
    byte[] passwordHash;

    /**
     * The Salt.
     */
    @JsonIgnore
    @Column(nullable = false)
    byte[] salt = new byte[16];

    @OneToMany(mappedBy = "votemaker", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"label", "startDate", "endDate", "algo", "anonymous", "votemaker", "choices", "members",
            "resultList", "private", "public", "intermediaryResult"})
    private List<Vote> startedVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "members", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"label", "startDate", "endDate", "algo", "anonymous", "votemaker", "choices", "members",
            "resultList", "private", "public", "intermediaryResult"})
    @JoinTable(name = "\"PRIVATE_VOTES\"",
            joinColumns = @JoinColumn(name = "\"user\""),
            inverseJoinColumns = @JoinColumn(name = "\"vote\""))
    private List<Vote> privateVoteList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<VotedVote> votedVotes = new ArrayList<>();

    /**
     * Instantiates a new User.
     *
     * @param email     the email
     * @param lastname  the lastname
     * @param firstname the firstname
     * @param password  the password
     */
    @Builder
    @SneakyThrows
    public User(String email, String lastname, String firstname, String password) {
        this.id = 0;
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.confirmed = false;
        this.admin = false;
        this.banned = false;

        setPassword(password);
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    @SneakyThrows
    public void setPassword(String password) {
        new SecureRandom().nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        passwordHash = factory.generateSecret(spec).getEncoded();
    }

    /**
     * Add started vote.
     *
     * @param vote the vote
     */
    public void addStartedVote(Vote vote) {
        if (!startedVotes.contains(vote))
            startedVotes.add(vote);
    }

    /**
     * Add private vote.
     *
     * @param vote the vote
     */
    public void addPrivateVote(Vote vote) {
        if (!privateVoteList.contains(vote))
            privateVoteList.add(vote);
    }

    /**
     * Add voted vote.
     *
     * @param votedVote the voted vote
     */
    public void addVotedVote(VotedVote votedVote) {
        if (!votedVotes.contains(votedVote))
            votedVotes.add(votedVote);
    }

    /**
     * Check password boolean.
     *
     * @param password the password
     * @return the boolean
     */
    @SneakyThrows
    public boolean checkPassword(String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] submittedPasswordHash = factory.generateSecret(spec).getEncoded();
        return Arrays.equals(passwordHash, submittedPasswordHash);
    }

    @Override
    @JsonIgnore
    public String getName() {
        return lastname + ", " + firstname + " <" + email + ">";
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }

    /**
     * Gets votes on which the user has voted.
     *
     * @return the votes on which the user has voted
     */
    @JsonIgnore
    public List<Vote> getVotesOnWhichTheUserHasVoted() {
        List<Vote> tempList = new ArrayList<>();
        for (VotedVote vv : votedVotes)
            if (!tempList.contains(vv.getVote()))
                tempList.add(vv.getVote());
        return tempList;
    }
}
