/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service;

import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.Realm;
import java.util.List;

/**
 *
 * @author altius
 */
public interface RealmService {

    public List<Realm> getRealmList(boolean active, CustomUserDetails curUser);

    public int addRealm(Realm realm, CustomUserDetails curUser);

    public int updateRealm(Realm realm, CustomUserDetails curUser);

    public Realm getRealmById(int realmId, CustomUserDetails curUser);
    
    public List<Realm> getRealmListForSync(String lastSyncDate, CustomUserDetails curUser);
}
