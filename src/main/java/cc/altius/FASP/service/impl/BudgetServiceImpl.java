/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service.impl;

import cc.altius.FASP.dao.BudgetDao;
import cc.altius.FASP.dao.CurrencyDao;
import cc.altius.FASP.dao.FundingSourceDao;
import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.Budget;
import cc.altius.FASP.model.Currency;
import cc.altius.FASP.model.FundingSource;
import cc.altius.FASP.model.Realm;
import cc.altius.FASP.model.SimpleCodeObject;
import cc.altius.FASP.model.SimpleProgram;
import cc.altius.FASP.service.AclService;
import cc.altius.FASP.service.BudgetService;
import cc.altius.FASP.service.ProgramService;
import cc.altius.FASP.service.RealmService;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 *
 * @author akil
 */
@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetDao budgetDao;
    @Autowired
    private FundingSourceDao fundingSourceDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
    private RealmService realmService;
    @Autowired
    private ProgramService programService;
    @Autowired
    private AclService aclService;

    @Override
    public int addBudget(Budget b, CustomUserDetails curUser) {
        FundingSource fs = this.fundingSourceDao.getFundingSourceById(b.getFundingSource().getFundingSourceId(), curUser);
        b.setFundingSource(fs);
        Currency c = this.currencyDao.getCurrencyById(b.getCurrency().getCurrencyId(), curUser);
        b.setCurrency(c);
        if (this.aclService.checkRealmAccessForUser(curUser, fs.getRealm().getId())) {
            return this.budgetDao.addBudget(b, curUser);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public int updateBudget(Budget b, CustomUserDetails curUser) {
        Budget bt = this.budgetDao.getBudgetById(b.getBudgetId(), curUser);
        if (this.aclService.checkRealmAccessForUser(curUser, bt.getFundingSource().getRealm().getId())) {
            return this.budgetDao.updateBudget(b, curUser);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public List<Budget> getBudgetListForProgramIds(String[] programIds, CustomUserDetails curUser) {
        List<Budget> bList = this.budgetDao.getBudgetListForProgramIds(programIds, curUser);
//        this.updateProgramsWithAccess(bList, curUser);
        return bList;
    }

    @Override
    public List<Budget> getBudgetList(CustomUserDetails curUser) {
        List<Budget> bList = this.budgetDao.getBudgetList(curUser);
//        this.updateProgramsWithAccess(bList, curUser);
        return bList;
    }

    @Override
    public List<Budget> getBudgetListForRealm(int realmId, CustomUserDetails curUser) {
        Realm r = this.realmService.getRealmById(realmId, curUser);
        if (this.aclService.checkRealmAccessForUser(curUser, realmId)) {
            List<Budget> bList = this.budgetDao.getBudgetListForRealm(realmId, curUser);
//            this.updateProgramsWithAccess(bList, curUser);
            return bList;
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public Budget getBudgetById(int BudgetId, CustomUserDetails curUser) {
        Budget b = this.budgetDao.getBudgetById(BudgetId, curUser);
        this.updateProgramsWithAccess(b, curUser);
        return b;
    }

    @Override
    public List<SimpleCodeObject> getBudgetDropdownFilterMultipleFundingSources(String fundingSourceIds, CustomUserDetails curUser) {
        return this.budgetDao.getBudgetDropdownFilterMultipleFundingSources(fundingSourceIds, curUser);
    }

    @Override
    public List<SimpleCodeObject> getBudgetDropdownForProgram(int programId, CustomUserDetails curUser) {
        return this.budgetDao.getBudgetDropdownForProgram(programId, curUser);
    }

    @Override
    public List<Budget> getBudgetListForSync(String lastSyncDate, CustomUserDetails curUser) {
        List<Budget> bList = this.budgetDao.getBudgetListForSync(lastSyncDate, curUser);
        updateProgramsWithAccess(bList, curUser);
        return bList;
    }

    @Override
    public List<Budget> getBudgetListForSyncProgram(String programIdsString, CustomUserDetails curUser) {
        if (programIdsString.length() > 0) {
            List<Budget> bList = this.budgetDao.getBudgetListForSyncProgram(programIdsString, curUser);
            updateProgramsWithAccess(bList, curUser);
            return bList;
        } else {
            return new LinkedList<>();
        }
    }

    private void updateProgramsWithAccess(List<Budget> budgetList, CustomUserDetails curUser) {
        for (Budget b : budgetList) {
            this.updateProgramsWithAccess(b, curUser);
        }
    }

    private void updateProgramsWithAccess(Budget b, CustomUserDetails curUser) {
        b.setProgramsWithAccess(new LinkedList<>());
        for (SimpleCodeObject p : b.getPrograms()) {
            try {
                SimpleProgram sp = this.programService.getSimpleProgramById(p.getId(), curUser);
                b.getProgramsWithAccess().add(p);
            } catch (EmptyResultDataAccessException e) {

            }
        }
    }
}
