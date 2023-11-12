package be.patryksitko.contest.ip2location.com.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ip2location.IPResult;

import be.patryksitko.contest.ip2location.com.component.Ip2LocationDB;
import be.patryksitko.contest.ip2location.com.component.Ip2LocationDB.IPResultJSONizer;
import be.patryksitko.contest.ip2location.com.other.builder.ResponseBuilder;
import be.patryksitko.contest.ip2location.com.other.builder.ResponseBuilder.ResponseType;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private Ip2LocationDB db;

    @GetMapping(value = "/get-ip", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> getClientIp(HttpServletRequest request) {
        System.out.println(db.IPQuery(request.getRemoteAddr()));
        final JSONObject responseBody = new JSONObject();
        responseBody.put("clientIp", request.getRemoteAddr());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(value = "/get-ip-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> getClientIpData(
            HttpServletRequest request) {
        final String clientIpAddress = request.getRemoteAddr();
        final Optional<IPResult> IPQueryResult = db.IPQuery(clientIpAddress);
        String responseBody = "{}";
        if (IPQueryResult.isPresent()) {
            responseBody = new IPResultJSONizer(IPQueryResult.get(), clientIpAddress).toJSON();
        }

        return ResponseEntity.ok(
                ResponseBuilder.builder().status(HttpStatus.OK).responseType(ResponseType.SUCCESS)
                        .body(responseBody)
                        .build());
    }

    @GetMapping(value = "/get-ip-data-of", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> getIpDataOf(@RequestParam("ip") String ipAddress) {
        final Optional<IPResult> IPQueryResult = db.IPQuery(ipAddress);
        String responseBody = "{}";
        if (IPQueryResult.isPresent()) {
            responseBody = new IPResultJSONizer(IPQueryResult.get(), ipAddress).toJSON();
        }

        return ResponseEntity.ok(
                ResponseBuilder.builder().status(HttpStatus.OK).responseType(ResponseType.SUCCESS)
                        .body(responseBody)
                        .build());
    }
}
