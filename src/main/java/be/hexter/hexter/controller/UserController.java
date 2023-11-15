package be.hexter.hexter.controller;

import java.util.List;

import javax.validation.Valid;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.User;
import be.hexter.hexter.other.GMailSender;
import be.hexter.hexter.other.RandomHash;
import be.hexter.hexter.other.builder.ResponseBuilder;
import be.hexter.hexter.other.builder.ResponseBuilder.ResponseType;
import be.hexter.hexter.other.helper.BCryptPasswordEncoder;
import be.hexter.hexter.other.helper.exception.PasswordFormatException;
import be.hexter.hexter.service.AuthenticationTokenService;
import be.hexter.hexter.service.UserService;
import be.hexter.hexter.service.exception.EmailRegisteredException;
import be.hexter.hexter.service.exception.EmailUnregisteredException;
import be.hexter.hexter.service.exception.PasswordMismatchException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @PutMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> registerUser(@RequestBody @Valid User user) {
        try {
            if (BCryptPasswordEncoder.isPasswordMeetingStandards(user.getCredential().getPassword())) {
                userService.registerUser(user);
            }
        } catch (EmailRegisteredException | PasswordFormatException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseBuilder.builder().status(HttpStatus.CONFLICT).errors(List.of(e.getMessage()))
                            .responseType(ResponseType.ERROR).body(user.toJSON())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder().status(HttpStatus.CREATED)
                .responseType(ResponseType.SUCCESS).body(user.toJSON()).build());

    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> loginUser(@RequestBody @Valid Credential credential) {
        AuthenticationToken authenticationToken;
        try {
            authenticationToken = userService.authenticateUser(credential);
        } catch (EmailUnregisteredException e) {
            log.error(e.getMessage());
            credential.setFingerprint("hidden.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseBuilder.builder().status(HttpStatus.NOT_FOUND).errors(List.of(e.getMessage()))
                            .responseType(ResponseType.ERROR).body(credential.toJSON()).build());
        } catch (PasswordMismatchException e) {
            log.error(e.getMessage());
            credential.setFingerprint("hidden.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseBuilder.builder().status(HttpStatus.CONFLICT).errors(List.of(e.getMessage()))
                            .responseType(ResponseType.ERROR).body(credential.toJSON()).build());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(ResponseBuilder.builder().status(HttpStatus.FOUND).responseType(ResponseType.SUCCESS)
                        .body(authenticationToken.toJSON()).build());
    }

    @PostMapping(value = "/validate-authentication-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> validateAuthenticationToken(
            @RequestBody @Valid AuthenticationToken authenticationToken) {
        final boolean authenticationTokenIsValid = authenticationTokenService
                .validateAuthenticationToken(authenticationToken);
        final HttpStatus statusOfHttp = authenticationTokenIsValid ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
        final JSONObject responseBody = new JSONObject();
        responseBody.put("isProvidedAuthenticationTokenValid", authenticationTokenIsValid);
        return ResponseEntity.status(statusOfHttp)
                .body(ResponseBuilder.builder().status(statusOfHttp)
                        .responseType(ResponseType.SUCCESS)
                        .body(responseBody.toString()).build());
    }

    @PostMapping(value = "/request-recover-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> requestRecoverPassword(@RequestBody @Valid String email) {
        User user;
        HttpStatus statusOfHttp;
        final String unwrappedEmail = email.substring(10, email.length() - 2);
        try {
            user = userService.findUserByEmail(unwrappedEmail);
            statusOfHttp = HttpStatus.FOUND;
        } catch (EmailUnregisteredException ex) {
            statusOfHttp = HttpStatus.NOT_FOUND;
            return ResponseEntity.status(statusOfHttp).body(ResponseBuilder.builder().status(statusOfHttp)
                    .responseType(ResponseType.ERROR).errors(List.of(ex.getMessage())).build());
        }
        final String resetPasswordToken = RandomHash.generateRandomString(8).toUpperCase();
        try {
            GMailSender.authenticate("patryk.sitko.algemeen@gmail.com", "bbfc vvue oxdf qfwk").send(
                    List.of(unwrappedEmail), "Hexter password reset",
                    "Your reset token is: " + resetPasswordToken + ".");
            userService.storeCredentialRecoveryToken(user, resetPasswordToken);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(statusOfHttp)
                .body(ResponseBuilder.builder().status(statusOfHttp).responseType(ResponseType.SUCCESS).build());
    }

    // @GetMapping(value = "/recover-password", consumes =
    // MediaType.APPLICATION_JSON_VALUE, produces =
    // MediaType.APPLICATION_JSON_VALUE)
    // public @ResponseBody ResponseEntity<Object>
    // recoverPassword(@RequestParam("email") String email,
    // @RequestParam("recovery-token") UUID recoveryToken) {
    // return null;
    // }
}
