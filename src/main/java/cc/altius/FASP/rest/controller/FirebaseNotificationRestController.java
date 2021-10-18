package cc.altius.FASP.rest.controller;

import cc.altius.FASP.service.FirebaseNotificationService;
import cc.altius.FASP.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author altius
 */
@RestController
@RequestMapping("/api/sendNotification")
public class FirebaseNotificationRestController {
    
    @Autowired
    UserService userService;
    @Autowired
    FirebaseNotificationService firebaseNotificationService;

    /**
     * Asynchronous API used to get the commit status
     *
     *
     * @return returns the commit status
     */
    @Operation(description = "Asynchronous API used to get the commit status.", summary = "Asynchronous API to get commit status", tags = ("commitStatus"))
    @ApiResponse(content = @Content(mediaType = "text/json"), responseCode = "200", description = "Returns the Integration Program list")
    @GetMapping("/{commitRequestId}")
    public @ResponseBody
    CompletableFuture<ResponseEntity> sendNotification(@PathVariable("commitRequestId") int commitRequestId) throws InterruptedException {
        System.out.println("inside send notification"+commitRequestId);
        return this.firebaseNotificationService.getCommitRequestStatusByCommitRequestId(commitRequestId).thenApplyAsync(ResponseEntity -> {
            System.out.println("ResponseEntity+++"+ResponseEntity);
            return new ResponseEntity(ResponseEntity, HttpStatus.OK);
        });
    }

}