/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service;

import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.SimpleObject;
import java.util.List;

/**
 *
 * @author akil
 */
public interface ForecastingStaticDataService {

    public List<SimpleObject> getUsageTypeList(boolean active, CustomUserDetails curUser);

    public List<SimpleObject> getNodeTypeList(boolean active, CustomUserDetails curUser);

    public List<SimpleObject> getForecastMethodTypeList(boolean active, CustomUserDetails curUser);

//    public int addAndUpadteUsageType(List<UsageType> usageTypeList, CustomUserDetails curUser);
}
