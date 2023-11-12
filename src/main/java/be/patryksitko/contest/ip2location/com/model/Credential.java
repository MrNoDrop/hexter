package be.patryksitko.contest.ip2location.com.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.patryksitko.contest.ip2location.com.other.helper.BCryptPasswordEncoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@JsonCreator), access = AccessLevel.PUBLIC)
@JsonRootName(value = "credeial", namespace = "credentials")
@JsonIgnoreProperties({ "id", "user", "authenticationTokens" })
@JsonPropertyOrder({ "email" })
@Entity
@Table(name = "credentials")
public class Credential implements Serializable, Cloneable {

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @JsonProperty("user")
    @ToString.Exclude
    @OneToOne(mappedBy = "credential")
    private User user;

    @NonNull
    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    public String email;

    @NonNull
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter(onMethod = @__(@JsonProperty("password")))
    @JsonAlias({ "password", "passwd" })
    @JsonIgnore
    @Column(name = "password", nullable = false)
    public String password;

    @JsonProperty("authenticationTokens")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "credential")
    private List<AuthenticationToken> authenticationTokens;

    @Transient
    private transient String fingerprint;

    public boolean isPasswordMatching(String password) {
        return BCryptPasswordEncoder.getInstance.matches(password, this.password);
    }

    public String toJSON() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Credential clone() {
        try {
            return (Credential) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
