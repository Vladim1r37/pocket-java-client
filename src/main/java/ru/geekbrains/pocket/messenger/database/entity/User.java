package ru.geekbrains.pocket.messenger.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.pocket.messenger.client.model.pub.UserPub;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    @Column
    private String email;

    //@NotNull
    @JsonIgnore
    @Column
    private String password;

    @Column
    private String uid; //serverUserId

    @Column
    private String token;

    @Column
    private Timestamp created_at = new Timestamp(new Date().getTime());

    //@NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> sentMess;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> receivedMess;

    public User(@NotNull UserPub userPub) {
        this.email = userPub.getEmail();
        this.uid = userPub.getId();
        this.profile = new UserProfile(userPub.getProfile());
    }

    public User(@NotNull UserPub userPub, @NotNull String token) {
        this.email = userPub.getEmail();
        this.uid = userPub.getId();
        this.profile = new UserProfile(userPub.getProfile());
        this.token = token;
    }

    public User(String email, UserProfile profile) {
        this.email = email;
        this.profile = profile;
        sentMess = new ArrayList<>();
        receivedMess = new ArrayList<>();
    }

    public void addReceivedMessage(Message message) {
        message.setReceiver(this);
        receivedMess.add(message);
    }

    public void addSentMessage(Message message) {
        message.setSender(this);
        sentMess.add(message);
    }

    public void clearMessages() {
        sentMess.clear();
        receivedMess.clear();
    }

    public String getAccount_name() {
        return profile.getUsername();
    }

    public void setAccount_name(String name) {
        this.profile.setUsername(name) ;
    }


    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", profile=" + profile + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.email);
        hash = 41 * hash + Objects.hashCode(this.profile);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return Objects.equals(this.profile, other.profile);
    }

}
