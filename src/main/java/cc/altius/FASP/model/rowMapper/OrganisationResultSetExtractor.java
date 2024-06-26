/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.Country;
import cc.altius.FASP.model.Organisation;
import cc.altius.FASP.model.RealmCountry;
import cc.altius.FASP.model.SimpleCodeObject;
import cc.altius.FASP.model.SimpleObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author altius
 */
public class OrganisationResultSetExtractor implements ResultSetExtractor<Organisation> {

    @Override
    public Organisation extractData(ResultSet rs) throws SQLException, DataAccessException {
        Organisation o = new Organisation();
        boolean isFirst = true;
        while (rs.next()) {
            if (isFirst) {
                o.setOrganisationId(rs.getInt("ORGANISATION_ID"));
                o.setOrganisationCode(rs.getString("ORGANISATION_CODE"));
                o.setOrganisationType(new SimpleObject(rs.getInt("ORGANISATION_TYPE_ID"), new LabelRowMapper("TYPE_").mapRow(rs, 1)));
                o.setRealm(new SimpleCodeObject(rs.getInt("REALM_ID"), new LabelRowMapper("REALM_").mapRow(rs, 1), rs.getString("REALM_CODE")));
                o.setLabel(new LabelRowMapper().mapRow(rs, 1));
                o.setBaseModel(new BaseModelRowMapper().mapRow(rs, 1));
                o.setRealmCountryList(new LinkedList<>());
            }
            int realmCountryId = rs.getInt("REALM_COUNTRY_ID");
            if (!rs.wasNull()) {
                RealmCountry rc = new RealmCountry();
                rc.setRealmCountryId(realmCountryId);
                rc.setCountry(new Country(rs.getInt("COUNTRY_ID"), new LabelRowMapper("COUNTRY_").mapRow(rs, 1)));
                if (o.getRealmCountryList().indexOf(rc) == -1) {
                    o.getRealmCountryList().add(rc);
                }
            }
            isFirst = false;
        }
        if (!isFirst) {
            o.setRealmCountryArray(new String[o.getRealmCountryList().size()]);
            int x = 0;
            for (RealmCountry rc : o.getRealmCountryList()) {
                o.getRealmCountryArray()[x] = Integer.toString(rc.getRealmCountryId());
                x++;
            }
            return o;
        } else {
            return null;
        }
    }
}
