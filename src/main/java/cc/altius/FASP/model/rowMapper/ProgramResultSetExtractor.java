/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.BasicUser;
import cc.altius.FASP.model.Country;
import cc.altius.FASP.model.Currency;
import cc.altius.FASP.model.HealthArea;
import cc.altius.FASP.model.Organisation;
import cc.altius.FASP.model.Program;
import cc.altius.FASP.model.Realm;
import cc.altius.FASP.model.RealmCountry;
import cc.altius.FASP.model.Region;
import cc.altius.FASP.model.SimpleObject;
import cc.altius.FASP.model.Unit;
import cc.altius.FASP.model.Version;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author akil
 */
public class ProgramResultSetExtractor implements ResultSetExtractor<Program> {

    @Override
    public Program extractData(ResultSet rs) throws SQLException, DataAccessException {
        Program p = new Program();
        boolean isFirst = true;
        while (rs.next()) {
            if (isFirst) {
                p.setProgramId(rs.getInt("PROGRAM_ID"));
                p.setRealmCountry(
                        new RealmCountry(
                                rs.getInt("REALM_COUNTRY_ID"),
                                new Country(rs.getInt("COUNTRY_ID"), new LabelRowMapper("COUNTRY_").mapRow(rs, 1)),
                                new Realm(rs.getInt("REALM_ID"), new LabelRowMapper("REALM_").mapRow(rs, 1), rs.getString("REALM_CODE"))
                        )
                );
                p.getRealmCountry().setDefaultCurrency(new Currency(rs.getInt("CURRENCY_ID"), rs.getString("CURRENCY_CODE"), rs.getString("CURRENCY_SYMBOL"), new LabelRowMapper("CURRENCY_").mapRow(rs, 1), rs.getDouble("CONVERSION_RATE_TO_USD")));
                p.getRealmCountry().setAirFreightPercentage(rs.getDouble("REALM_COUNTRY_AIR_FREIGHT_PERC"));
                p.getRealmCountry().setSeaFreightPercentage(rs.getDouble("REALM_COUNTRY_SEA_FREIGHT_PERC"));
                p.getRealmCountry().setShippedToArrivedSeaLeadTime(rs.getInt("REALM_COUNTRY_SHIPPED_TO_ARRIVED_SEA_LEAD_TIME"));
                p.getRealmCountry().setShippedToArrivedAirLeadTime(rs.getInt("REALM_COUNTRY_SHIPPED_TO_ARRIVED_AIR_LEAD_TIME"));
                p.getRealmCountry().setArrivedToDeliveredLeadTime(rs.getInt("REALM_COUNTRY_ARRIVED_TO_DELIVERED_LEAD_TIME"));
                p.getRealmCountry().setPalletUnit(new Unit(rs.getInt("UNIT_ID"), new LabelRowMapper("UNIT_").mapRow(rs, 1), rs.getString("UNIT_CODE")));
                p.setLabel(new LabelRowMapper().mapRow(rs, 1));
                p.setOrganisation(new SimpleObject(rs.getInt("ORGANISATION_ID"), new LabelRowMapper("ORGANISATION_").mapRow(rs, 1)));
                p.setHealthArea(new SimpleObject(rs.getInt("HEALTH_AREA_ID"), new LabelRowMapper("REALM_").mapRow(rs, 1)));
                p.setProgramManager(new BasicUser(rs.getInt("PROGRAM_MANAGER_USER_ID"), rs.getString("PROGRAM_MANAGER_USERNAME")));
                p.setProgramNotes(rs.getString("PROGRAM_NOTES"));
                p.setAirFreightPerc(rs.getDouble("AIR_FREIGHT_PERC"));
                p.setSeaFreightPerc(rs.getDouble("SEA_FREIGHT_PERC"));
                p.setPlannedToDraftLeadTime(rs.getInt("PLANNED_TO_DRAFT_LEAD_TIME"));
                p.setDraftToSubmittedLeadTime(rs.getInt("DRAFT_TO_SUBMITTED_LEAD_TIME"));
                p.setSubmittedToApprovedLeadTime(rs.getInt("SUBMITTED_TO_APPROVED_LEAD_TIME"));
                p.setApprovedToShippedLeadTime(rs.getInt("APPROVED_TO_SHIPPED_LEAD_TIME"));
                p.setDeliveredToReceivedLeadTime(rs.getInt("DELIVERED_TO_RECEIVED_LEAD_TIME"));
                p.setMonthsInPastForAmc(rs.getInt("MONTHS_IN_PAST_FOR_AMC"));
                p.setMonthsInFutureForAmc(rs.getInt("MONTHS_IN_FUTURE_FOR_AMC"));
                p.setCurrentVersion(new Version(rs.getInt("CURRENT_VERSION_ID"), new BasicUser(rs.getInt("CV_CMB_USER_ID"), rs.getString("CV_CMB_USERNAME")), rs.getTimestamp("CV_CREATED_DATE")));
                p.setBaseModel(new BaseModelRowMapper().mapRow(rs, 1));
                p.setRegionList(new LinkedList<>());
                p.setVersionList(new LinkedList<>());
            }
            Region r = new Region(rs.getInt("REGION_ID"), new LabelRowMapper("REGION_").mapRow(rs, 0));
            if (p.getRegionList().indexOf(r) == -1) {
                p.getRegionList().add(r);
            }
            Version v = new Version(rs.getInt("VERSION_ID"), new BasicUser(rs.getInt("VERSION_USER_ID"), rs.getString("VERSION_USERNAME")), rs.getTimestamp("VERSION_CREATED_DATE"));
            if (p.getVersionList().indexOf(v) == -1) {
                p.getVersionList().add(v);
            }
            isFirst = false;
        }
        if (!isFirst) {
//            p.setRegionArray(new String[p.getRegionList().size()]);
//            int x = 0;
//            for (Region r : p.getRegionList()) {
//                p.getRegionArray()[x] = Integer.toString(r.getRegionId());
//                x++;
//            }
            return p;
        } else {
            return null;
        }
    }

}
