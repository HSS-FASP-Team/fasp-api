/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service;

import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.DTO.DatabaseTranslationsDTO;
import cc.altius.FASP.model.DTO.StaticLabelDTO;
import java.util.List;

/**
 *
 * @author palash
 */
public interface LabelService {

    public List<DatabaseTranslationsDTO> getDatabaseLabelsList(CustomUserDetails curUser);
    
    public boolean saveDatabaseLabels(List<String> label,CustomUserDetails curUser);
    
    public List<StaticLabelDTO> getStaticLabelsList();
    
    public boolean saveStaticLabels(List<StaticLabelDTO> staticLabelList,CustomUserDetails curUser);
}
