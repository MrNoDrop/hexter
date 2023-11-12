package be.patryksitko.contest.ip2location.com.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@JsonRootName(value = "authenticationToken", namespace = "authenticationTokens")
@JsonIgnoreProperties({ "id", "credential" })
@JsonPropertyOrder({ "fingerprint", "authenticationToken" })
@Entity
@Table(name = "authentication_tokens")
public class AuthenticationToken implements Serializable, Cloneable {

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JsonProperty("credential")
    @ToString.Exclude
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "credential_id", value = ConstraintMode.CONSTRAINT), referencedColumnName = "id")
    private Credential credential;

    @NonNull
    @JsonProperty("fingerprint")
    @Column(name = "fingerprint", nullable = false)
    private String fingerprint;

    @JsonProperty("authenticationToken")
    @JsonAlias({ "authenticationToken", "authentication-token" })
    @Column(name = "authentication_token", nullable = false)
    private UUID authenticationToken;

    public String toJSON() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public AuthenticationToken clone() {
        try {
            return (AuthenticationToken) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
