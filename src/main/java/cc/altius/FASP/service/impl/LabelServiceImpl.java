/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service.impl;

import cc.altius.FASP.dao.LabelDao;
import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.DTO.DatabaseTranslationsDTO;
import cc.altius.FASP.model.DTO.StaticLabelDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.altius.FASP.service.LabelService;

/**
 *
 * @author palash
 */
@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelDao labelDao;

    @Override
    public List<DatabaseTranslationsDTO> getDatabaseLabelsList(CustomUserDetails curUser) {
        return this.labelDao.getDatabaseLabelsList(curUser);
    }

    @Override
    public boolean saveDatabaseLabels(List<String> label, CustomUserDetails curUser) {
        return this.labelDao.saveDatabaseLabels(label, curUser);
    }

    @Override
    public List<StaticLabelDTO> getStaticLabelsList() {
        return this.labelDao.getStaticLabelsList();
    }

    @Override
    public boolean saveStaticLabels(List<StaticLabelDTO> staticLabelList, CustomUserDetails curUser) {
        return this.labelDao.saveStaticLabels(staticLabelList, curUser);
    }

}
