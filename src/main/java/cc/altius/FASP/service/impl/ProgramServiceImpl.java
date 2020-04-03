/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service.impl;

import cc.altius.FASP.dao.ProgramDao;
import cc.altius.FASP.dao.RealmDao;
import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.DTO.ProgramDTO;
import cc.altius.FASP.model.Program;
import cc.altius.FASP.model.ProgramPlanningUnit;
import cc.altius.FASP.model.Realm;
import cc.altius.FASP.service.AclService;
import cc.altius.FASP.service.ProgramService;
import cc.altius.FASP.service.RealmCountryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 *
 * @author altius
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramDao programDao;
    @Autowired
    private RealmCountryService realmCountryService;
    @Autowired
    private RealmDao realmDao;
    @Autowired
    private AclService aclService;

    @Override
    public List<ProgramDTO> getProgramListForDropdown(CustomUserDetails curUser) {
        return this.programDao.getProgramListForDropdown(curUser);
    }

    @Override
    public int addProgram(Program p, CustomUserDetails curUser) {
        p.setRealmCountry(this.realmCountryService.getRealmCountryById(p.getRealmCountry().getRealmCountryId(), curUser));
        if (this.aclService.checkAccessForUser(
                curUser,
                p.getRealmCountry().getRealm().getRealmId(),
                p.getRealmCountry().getRealmCountryId(),
                p.getHealthArea().getHealthAreaId(),
                p.getOrganisation().getOrganisationId(),
                0)) {
            return this.programDao.addProgram(p, curUser);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public int updateProgram(Program p, CustomUserDetails curUser) {
        Program curProg = this.getProgramById(p.getProgramId(), curUser);
        if (curProg == null) {
            throw new EmptyResultDataAccessException(1);
        }
        if (this.aclService.checkAccessForUser(
                curUser,
                curProg.getRealmCountry().getRealm().getRealmId(),
                curProg.getRealmCountry().getRealmCountryId(),
                curProg.getHealthArea().getHealthAreaId(),
                curProg.getOrganisation().getOrganisationId(),
                curProg.getProgramId())) {
            return this.programDao.updateProgram(p, curUser);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public List<Program> getProgramList(CustomUserDetails curUser) {
        return this.programDao.getProgramList(curUser);
    }

    @Override
    public List<Program> getProgramList(int realmId, CustomUserDetails curUser) {
        Realm r = this.realmDao.getRealmById(realmId, curUser);
        if (r == null) {
            throw new EmptyResultDataAccessException(1);
        }
        if (this.aclService.checkRealmAccessForUser(curUser, realmId)) {
            return this.programDao.getProgramList(realmId, curUser);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public Program getProgramById(int programId, CustomUserDetails curUser) {
        Program p = this.programDao.getProgramById(programId, curUser);
        if (p == null) {
            throw new EmptyResultDataAccessException(1);
        }
        if (this.aclService.checkRealmAccessForUser(curUser, p.getRealmCountry().getRealm().getRealmId())) {
            return p;
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public ProgramPlanningUnit getPlanningUnitListForProgramId(int programId, CustomUserDetails curUser) {
        if (this.aclService.checkProgramAccessForUser(curUser, programId)) {
            return this.programDao.getPlanningUnitListForProgramId(programId, curUser);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public int saveProgramPlanningUnit(ProgramPlanningUnit ppu, CustomUserDetails curUser) {
        if (this.aclService.checkProgramAccessForUser(curUser, ppu.getProgramId())) {
            return this.programDao.saveProgramPlanningUnit(ppu, curUser);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public List<Program> getProgramListForSync(String lastSyncDate, CustomUserDetails curUser) {
        return this.programDao.getProgramListForSync(lastSyncDate, curUser);
    }

}
