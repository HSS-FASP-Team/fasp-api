/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.BasicUser;
import cc.altius.FASP.model.Country;
import cc.altius.FASP.model.Program;
import cc.altius.FASP.model.Realm;
import cc.altius.FASP.model.RealmCountry;
import cc.altius.FASP.model.Region;
import cc.altius.FASP.model.SimpleCodeObject;
import cc.altius.FASP.model.SimpleObject;
import cc.altius.FASP.model.Version;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author akil
 */
public class ProgramListResultSetExtractor implements ResultSetExtractor<List<Program>> {

    @Override
    public List<Program> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Program> pList = new LinkedList<Program>();
//        int oldProgramId = 0, newProgramId;
        boolean isFirst = true;
        Program p = new Program();
        while (rs.next()) {
            int programId = rs.getInt("PROGRAM_ID");
            Program tmpProg = new Program(programId, null, null);
            if (pList.indexOf(tmpProg) == -1) {
                if (!isFirst) {
                    p = new Program();
                }
                pList.add(p);
                p.setProgramId(rs.getInt("PROGRAM_ID"));
                p.setProgramCode(rs.getString("PROGRAM_CODE"));
                p.setRealmCountry(
                        new RealmCountry(
                                rs.getInt("REALM_COUNTRY_ID"),
                                new Country(rs.getInt("COUNTRY_ID"), rs.getString("COUNTRY_CODE"), new LabelRowMapper("COUNTRY_").mapRow(rs, 1)),
                                new Realm(rs.getInt("REALM_ID"), new LabelRowMapper("REALM_").mapRow(rs, 1), rs.getString("REALM_CODE"))
                        )
                );
//                p.getRealmCountry().setDefaultCurrency(new Currency(rs.getInt("CURRENCY_ID"), rs.getString("CURRENCY_CODE"), new LabelRowMapper("CURRENCY_").mapRow(rs, 1), rs.getDouble("CONVERSION_RATE_TO_USD")));
//                p.getRealmCountry().setAirFreightPercentage(rs.getDouble("REALM_COUNTRY_AIR_FREIGHT_PERC"));
//                p.getRealmCountry().setSeaFreightPercentage(rs.getDouble("REALM_COUNTRY_SEA_FREIGHT_PERC"));
//                p.getRealmCountry().setShippedToArrivedBySeaLeadTime(rs.getDouble("REALM_COUNTRY_SHIPPED_TO_ARRIVED_BY_SEA_LEAD_TIME"));
//                p.getRealmCountry().setShippedToArrivedByAirLeadTime(rs.getDouble("REALM_COUNTRY_SHIPPED_TO_ARRIVED_BY_AIR_LEAD_TIME"));
//                p.getRealmCountry().setArrivedToDeliveredLeadTime(rs.getDouble("REALM_COUNTRY_ARRIVED_TO_DELIVERED_LEAD_TIME"));
//                p.getRealmCountry().setPalletUnit(new Unit(rs.getInt("UNIT_ID"), new LabelRowMapper("UNIT_").mapRow(rs, 1), rs.getString("UNIT_CODE")));
                p.setLabel(new LabelRowMapper().mapRow(rs, 1));
                p.setOrganisation(new SimpleCodeObject(rs.getInt("ORGANISATION_ID"), new LabelRowMapper("ORGANISATION_").mapRow(rs, 1), rs.getString("ORGANISATION_CODE")));
                p.setHealthArea(new SimpleCodeObject(rs.getInt("HEALTH_AREA_ID"), new LabelRowMapper("HEALTH_AREA_").mapRow(rs, 1), rs.getString("HEALTH_AREA_CODE")));
                p.setProgramManager(new BasicUser(rs.getInt("PROGRAM_MANAGER_USER_ID"), rs.getString("PROGRAM_MANAGER_USERNAME")));
                p.setProgramNotes(rs.getString("PROGRAM_NOTES"));
                p.setAirFreightPerc(rs.getDouble("AIR_FREIGHT_PERC"));
                p.setSeaFreightPerc(rs.getDouble("SEA_FREIGHT_PERC"));
                p.setPlannedToDraftLeadTime(rs.getDouble("PLANNED_TO_DRAFT_LEAD_TIME"));
                p.setDraftToSubmittedLeadTime(rs.getDouble("DRAFT_TO_SUBMITTED_LEAD_TIME"));
                p.setSubmittedToApprovedLeadTime(rs.getDouble("SUBMITTED_TO_APPROVED_LEAD_TIME"));
                p.setApprovedToShippedLeadTime(rs.getDouble("APPROVED_TO_SHIPPED_LEAD_TIME"));
                p.setShippedToArrivedBySeaLeadTime(rs.getDouble("SHIPPED_TO_ARRIVED_BY_SEA_LEAD_TIME"));
                p.setShippedToArrivedByAirLeadTime(rs.getDouble("SHIPPED_TO_ARRIVED_BY_AIR_LEAD_TIME"));
                p.setArrivedToDeliveredLeadTime(rs.getDouble("ARRIVED_TO_DELIVERED_LEAD_TIME"));
                p.setMonthsInPastForAmc(rs.getInt("MONTHS_IN_PAST_FOR_AMC"));
                p.setMonthsInFutureForAmc(rs.getInt("MONTHS_IN_FUTURE_FOR_AMC"));
                p.setCurrentVersion(new Version(
                        rs.getInt("CV_VERSION_ID"),
                        new SimpleObject(rs.getInt("CV_VERSION_TYPE_ID"), new LabelRowMapper("CV_VERSION_TYPE_").mapRow(rs, 1)),
                        new SimpleObject(rs.getInt("CV_VERSION_STATUS_ID"), new LabelRowMapper("CV_VERSION_STATUS_").mapRow(rs, 1)),
                        rs.getString("CV_VERSION_NOTES"),
                        new BasicUser(rs.getInt("CV_CB_USER_ID"), rs.getString("CV_CB_USERNAME")),
                        rs.getTimestamp("CV_CREATED_DATE"),
                        new BasicUser(rs.getInt("CV_LMB_USER_ID"), rs.getString("CV_LMB_USERNAME")),
                        rs.getTimestamp("CV_LAST_MODIFIED_DATE")
                ));
                p.setBaseModel(new BaseModelRowMapper().mapRow(rs, 1));
                p.setRegionList(new LinkedList<Region>());
                p.setVersionList(new LinkedList<Version>());
            } else {
                p = pList.get(pList.indexOf(tmpProg));
            }
            Region r = new Region(rs.getInt("REGION_ID"), new LabelRowMapper("REGION_").mapRow(rs, 0));
            if (p.getRegionList().indexOf(r) == -1) {
                p.getRegionList().add(r);
            }
            Version v = new Version(
                    rs.getInt("VT_VERSION_ID"),
                    new SimpleObject(rs.getInt("VT_VERSION_TYPE_ID"), new LabelRowMapper("VT_VERSION_TYPE_").mapRow(rs, 1)),
                    new SimpleObject(rs.getInt("VT_VERSION_STATUS_ID"), new LabelRowMapper("VT_VERSION_STATUS_").mapRow(rs, 1)),
                    rs.getString("VT_VERSION_NOTES"),
                    new BasicUser(rs.getInt("VT_CB_USER_ID"), rs.getString("VT_CB_USERNAME")),
                    rs.getTimestamp("VT_CREATED_DATE"),
                    new BasicUser(rs.getInt("VT_LMB_USER_ID"), rs.getString("VT_LMB_USERNAME")),
                    rs.getTimestamp("VT_LAST_MODIFIED_DATE")
            );
            if (p.getVersionList().indexOf(v) == -1) {
                p.getVersionList().add(v);
            }
            isFirst = false;
        }
        return pList;
    }
}
