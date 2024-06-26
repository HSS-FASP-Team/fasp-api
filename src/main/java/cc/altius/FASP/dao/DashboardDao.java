/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.dao;

import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.DashboardUser;
import cc.altius.FASP.model.ProgramCount;
import java.util.List;

/**
 *
 * @author altius
 */
public interface DashboardDao {

    public int getRealmCount(CustomUserDetails curUser);

    public int getLanguageCount(CustomUserDetails curUser);

    public int getRealmCountryCount(CustomUserDetails curUser);

    public int getHealthAreaCount(CustomUserDetails curUser);

    public int getOrganisationCount(CustomUserDetails curUser);

    public int getRegionCount(CustomUserDetails curUser);

    public ProgramCount getProgramCount(CustomUserDetails curUser);

    public int getSupplyPlanPendingCount(CustomUserDetails curUser);

    List<DashboardUser> getUserListForApplicationLevelAdmin(CustomUserDetails curUser);

    List<DashboardUser> getUserListForRealmLevelAdmin(CustomUserDetails curUser);

}
