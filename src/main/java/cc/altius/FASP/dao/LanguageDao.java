/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.dao;

import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.Language;
import java.util.List;
import java.util.Map;

/**
 *
 * @author altius
 */
public interface LanguageDao {

    public List<Language> getLanguageList(boolean active, CustomUserDetails curUser);

    public int addLanguage(Language language, CustomUserDetails curUser);

    public int editLanguage(Language language, CustomUserDetails curUser);

    public Language getLanguageById(int languageId, CustomUserDetails curUser);

    public List<Language> getLanguageListForSync(String lastSyncDate);

    public Map<String, String> getLanguageJsonForStaticLabels(String language);
}
