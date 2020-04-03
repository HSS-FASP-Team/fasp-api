/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.rest.controller;

import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.Program;
import cc.altius.FASP.model.ProgramPlanningUnit;
import cc.altius.FASP.model.ResponseCode;
import cc.altius.FASP.service.ProgramDataService;
import cc.altius.FASP.service.ProgramService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author akil
 */
@RestController
@RequestMapping("/api")
public class ProgramRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProgramDataService programDataService;
    @Autowired
    private ProgramService programService;

    @PostMapping(path = "/program")
    public ResponseEntity postProgram(@RequestBody Program program, Authentication auth) {
        try {
            CustomUserDetails curUser = (CustomUserDetails) auth.getPrincipal();
            this.programService.addProgram(program, curUser);
            return new ResponseEntity(new ResponseCode("static.message.addSuccess"), HttpStatus.OK);
        } catch (AccessDeniedException ae) {
            logger.error("Error while trying to add Program", ae);
            return new ResponseEntity(new ResponseCode("static.message.addFailed"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Error while trying to add Program", e);
            return new ResponseEntity(new ResponseCode("static.message.addFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/program")
    public ResponseEntity putProgram(@RequestBody Program program, Authentication auth) {
        try {
            CustomUserDetails curUser = ((CustomUserDetails) auth.getPrincipal());
            this.programService.updateProgram(program, curUser);
            return new ResponseEntity(new ResponseCode("static.message.updateSuccess"), HttpStatus.OK);
        } catch (EmptyResultDataAccessException ae) {
            logger.error("Error while trying to update Program", ae);
            return new ResponseEntity(new ResponseCode("static.message.updateFailed"), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException ae) {
            logger.error("Error while trying to update Program", ae);
            return new ResponseEntity(new ResponseCode("static.message.updateFailed"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Error while trying to update Program", e);
            return new ResponseEntity(new ResponseCode("static.message.updateFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/program")
    public ResponseEntity getProgram(Authentication auth) {
        try {
            CustomUserDetails curUser = ((CustomUserDetails) auth.getPrincipal());
            return new ResponseEntity(this.programService.getProgramList(curUser), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while trying to list Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/program/{programId}/planningUnit")
    public ResponseEntity getPlanningUnitForProgram(@PathVariable("programId") int programId, Authentication auth) {
        try {
            CustomUserDetails curUser = ((CustomUserDetails) auth.getPrincipal());
            return new ResponseEntity(this.programService.getPlanningUnitListForProgramId(programId, curUser), HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error while trying to list PlanningUnit for Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            logger.error("Error while trying to list PlanningUnit for Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Error while trying to list PlanningUnit for Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/program/planningingUnit")
    public ResponseEntity savePlanningUnitForProgram(@RequestBody ProgramPlanningUnit ppu, Authentication auth) {
        try {
            CustomUserDetails curUser = ((CustomUserDetails) auth.getPrincipal());
            this.programService.saveProgramPlanningUnit(ppu, curUser);
            return new ResponseEntity("static.message.addSuccess", HttpStatus.OK);
        } catch (AccessDeniedException e) {
            logger.error("Error while trying to update PlanningUnit for Program", e);
            return new ResponseEntity(new ResponseCode("static.message.updateFailed"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Error while trying to update PlanningUnit for Program", e);
            return new ResponseEntity(new ResponseCode("static.message.updateFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/program/realmId/{realmId}")
    public ResponseEntity getProgramForRealm(@PathVariable(value = "realmId", required = true) int realmId, Authentication auth) {
        try {
            CustomUserDetails curUser = ((CustomUserDetails) auth.getPrincipal());
            return new ResponseEntity(this.programService.getProgramList(realmId, curUser), HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error while trying to list Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            logger.error("Error while trying to list Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Error while trying to update Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity getProgram(@PathVariable("programId") int programId, Authentication auth) {
        try {
            CustomUserDetails curUser = ((CustomUserDetails) auth.getPrincipal());
            return new ResponseEntity(this.programService.getProgramById(programId, curUser), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            logger.error("Error while trying to list Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.UNAUTHORIZED);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error while trying to list Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error while trying to list Program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(value = "/sync/program/{lastSyncDate}")
    public ResponseEntity getProgramListForSync(@PathVariable("lastSyncDate") String lastSyncDate, Authentication auth) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.parse(lastSyncDate);
            CustomUserDetails curUser = (CustomUserDetails) auth.getPrincipal();
            return new ResponseEntity(this.programService.getProgramListForSync(lastSyncDate, curUser), HttpStatus.OK);
        } catch (ParseException p) {
            logger.error("Error while listing program", p);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.PRECONDITION_FAILED);
        } catch (Exception e) {
            logger.error("Error while listing program", e);
            return new ResponseEntity(new ResponseCode("static.message.listFailed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
