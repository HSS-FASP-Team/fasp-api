/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.dao.impl;

import cc.altius.FASP.dao.LabelDao;
import cc.altius.FASP.dao.PlanningUnitDao;
import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.PlanningUnit;
import cc.altius.FASP.model.rowMapper.PlanningUnitRowMapper;
import cc.altius.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author altius
 */
@Repository
public class PlanningUnitDaoImpl implements PlanningUnitDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private DataSource dataSource;

    @Autowired
    private LabelDao labelDao;

    private String sqlListString = "SELECT pu.PLANNING_UNIT_ID, pu.MULTIPLIER, fu.FORECASTING_UNIT_ID, "
            + "	pul.LABEL_ID, pul.LABEL_EN, pul.LABEL_FR, pul.LABEL_PR, pul.LABEL_SP, "
            + "    ful.LABEL_ID `FORECASTING_UNIT_LABEL_ID`, ful.LABEL_EN `FORECASTING_UNIT_LABEL_EN`, ful.LABEL_FR `FORECASTING_UNIT_LABEL_FR`, ful.LABEL_PR `FORECASTING_UNIT_LABEL_PR`, ful.LABEL_SP `FORECASTING_UNIT_LABEL_SP`, "
            + "    fugl.LABEL_ID `GENERIC_LABEL_ID`, fugl.LABEL_EN `GENERIC_LABEL_EN`, fugl.LABEL_FR `GENERIC_LABEL_FR`, fugl.LABEL_PR `GENERIC_LABEL_PR`, fugl.LABEL_SP `GENERIC_LABEL_SP`, "
            + "    r.REALM_ID, r.REALM_CODE, rl.LABEL_ID `REALM_LABEL_ID`, rl.LABEL_EN `REALM_LABEL_EN`, rl.LABEL_FR `REALM_LABEL_FR`, rl.LABEL_PR `REALM_LABEL_PR`, rl.LABEL_SP `REALM_LABEL_SP`, "
            + "    pc.PRODUCT_CATEGORY_ID, pcl.LABEL_ID `PRODUCT_CATEGORY_LABEL_ID`, pcl.LABEL_EN `PRODUCT_CATEGORY_LABEL_EN`, pcl.LABEL_FR `PRODUCT_CATEGORY_LABEL_FR`, pcl.LABEL_PR `PRODUCT_CATEGORY_LABEL_PR`, pcl.LABEL_SP `PRODUCT_CATEGORY_LABEL_SP`, "
            + "    tc.TRACER_CATEGORY_ID, tcl.LABEL_ID `TRACER_CATEGORY_LABEL_ID`, tcl.LABEL_EN `TRACER_CATEGORY_LABEL_EN`, tcl.LABEL_FR `TRACER_CATEGORY_LABEL_FR`, tcl.LABEL_PR `TRACER_CATEGORY_LABEL_PR`, tcl.LABEL_SP `TRACER_CATEGORY_LABEL_SP`, "
            + "    u.UNIT_ID, u.UNIT_CODE, ul.LABEL_ID `UNIT_LABEL_ID`, ul.LABEL_EN `UNIT_LABEL_EN`, ul.LABEL_FR `UNIT_LABEL_FR`, ul.LABEL_PR `UNIT_LABEL_PR`, ul.LABEL_SP `UNIT_LABEL_SP`, "
            + "    cb.USER_ID `CB_USER_ID`, cb.USERNAME `CB_USERNAME`, lmb.USER_ID `LMB_USER_ID`, lmb.USERNAME `LMB_USERNAME`, pu.ACTIVE, pu.CREATED_DATE, pu.LAST_MODIFIED_DATE  "
            + "FROM rm_planning_unit pu "
            + "LEFT JOIN ap_label pul ON pu.LABEL_ID=pul.LABEL_ID "
            + "LEFT JOIN rm_forecasting_unit fu on pu.FORECASTING_UNIT_ID=fu.FORECASTING_UNIT_ID "
            + "LEFT JOIN ap_label ful ON fu.LABEL_ID=ful.LABEL_ID  "
            + "LEFT JOIN ap_label fugl ON fu.GENERIC_LABEL_ID=fugl.LABEL_ID  "
            + "LEFT JOIN rm_realm r ON fu.REALM_ID=r.REALM_ID  "
            + "LEFT JOIN ap_label rl ON r.LABEL_ID=rl.LABEL_ID  "
            + "LEFT JOIN rm_product_category pc ON fu.PRODUCT_CATEGORY_ID=pc.PRODUCT_CATEGORY_ID  "
            + "LEFT JOIN ap_label pcl ON pc.LABEL_ID=pcl.LABEL_ID  "
            + "LEFT JOIN rm_tracer_category tc ON fu.TRACER_CATEGORY_ID=tc.TRACER_CATEGORY_ID  "
            + "LEFT JOIN ap_label tcl ON tc.LABEL_ID=tcl.LABEL_ID  "
            + "LEFT JOIN ap_unit u ON pu.UNIT_ID=u.UNIT_ID "
            + "LEFT JOIN ap_label ul ON u.LABEL_ID=ul.LABEL_ID  "
            + "LEFT JOIN us_user cb ON pu.CREATED_BY=cb.USER_ID  "
            + "LEFT JOIN us_user lmb ON pu.LAST_MODIFIED_BY=lmb.USER_ID "
            + "WHERE TRUE ";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<PlanningUnit> getPlanningUnitList(boolean active, CustomUserDetails curUser) {
        String sqlString = this.sqlListString;
        Map<String, Object> params = new HashMap<>();
        if (active) {
            sqlString += " AND pu.ACTIVE=:active ";
            params.put("active", active);
        }
        if (curUser.getRealm().getRealmId() != -1) {
            sqlString += "AND fu.REALM_ID=:realmId ";
            params.put("realmId", curUser.getRealm().getRealmId());
        }
        return this.namedParameterJdbcTemplate.query(sqlString, params, new PlanningUnitRowMapper());
    }

    @Override
    public List<PlanningUnit> getPlanningUnitList(int realmId, boolean active, CustomUserDetails curUser) {
        String sqlString = this.sqlListString;
        Map<String, Object> params = new HashMap<>();
        sqlString += " AND fu.REALM_ID=:userRealmId ";
        params.put("userRealmId", realmId);
        if (active) {
            sqlString += " AND pu.ACTIVE=:active ";
            params.put("active", active);
        }
        if (curUser.getRealm().getRealmId() != -1) {
            sqlString += "AND fu.REALM_ID=:realmId ";
            params.put("realmId", curUser.getRealm().getRealmId());
        }
        return this.namedParameterJdbcTemplate.query(sqlString, params, new PlanningUnitRowMapper());
    }

    @Override
    public List<PlanningUnit> getPlanningUnitListByForecastingUnit(int forecastingUnitId, boolean active, CustomUserDetails curUser) {
        String sqlString = this.sqlListString;
        Map<String, Object> params = new HashMap<>();
        sqlString += " AND pu.FORECASTING_UNIT_ID=:forecastingUnitId ";
        params.put("forecastingUnitId", forecastingUnitId);
        if (active) {
            sqlString += " AND pu.ACTIVE=:active ";
            params.put("active", active);
        }
        if (curUser.getRealm().getRealmId() != -1) {
            sqlString += "AND fu.REALM_ID=:realmId ";
            params.put("realmId", curUser.getRealm().getRealmId());
        }
        return this.namedParameterJdbcTemplate.query(sqlString, params, new PlanningUnitRowMapper());
    }

    @Override
    public int addPlanningUnit(PlanningUnit planningUnit, CustomUserDetails curUser) {
        SimpleJdbcInsert si = new SimpleJdbcInsert(this.dataSource).withTableName("rm_planning_unit").usingGeneratedKeyColumns("PLANNING_UNIT_ID");
        Date curDate = DateUtils.getCurrentDateObject(DateUtils.EST);
        Map<String, Object> params = new HashMap<>();
        int labelId = this.labelDao.addLabel(planningUnit.getLabel(), curUser.getUserId());
        params.put("LABEL_ID", labelId);
        params.put("FORECASTING_UNIT_ID", planningUnit.getForeacastingUnit().getForecastingUnitId());
        params.put("UNIT_ID", planningUnit.getUnit().getUnitId());
        params.put("MULTIPLIER", planningUnit.getMultiplier());
        params.put("ACTIVE", true);
        params.put("CREATED_BY", curUser.getUserId());
        params.put("CREATED_DATE", curDate);
        params.put("LAST_MODIFIED_BY", curUser.getUserId());
        params.put("LAST_MODIFIED_DATE", curDate);
        return si.executeAndReturnKey(params).intValue();
    }

    @Override
    public int updatePlanningUnit(PlanningUnit planningUnit, CustomUserDetails curUser) {
        Date curDate = DateUtils.getCurrentDateObject(DateUtils.EST);
        String sqlString = "UPDATE rm_planning_unit pu LEFT JOIN ap_label pul ON pu.LABEL_ID=pul.LABEL_ID "
                + "SET  "
                + "    pu.MULTIPLIER=:multiplier, "
                + "    pu.UNIT_ID=:unitId, "
                + "    pu.ACTIVE=:active, "
                + "    pu.LAST_MODIFIED_BY=IF(pu.MULTIPLIER!=:multiplier OR pu.UNIT_ID!=:unitId OR pu.ACTIVE!=:active,:curUser, pu.LAST_MODIFIED_BY), "
                + "    pu.LAST_MODIFIED_DATE=IF(pu.MULTIPLIER!=:multiplier OR pu.UNIT_ID!=:unitId OR pu.ACTIVE!=:active,:curDate, fu.LAST_MODIFIED_DATE), "
                + "    pul.LABEL_EN=:labelEn, "
                + "    pul.LAST_MODIFIED_BY=IF(pul.LABEL_EN=:labelEn,:curUser, pul.LAST_MODIFIED_BY), "
                + "    pul.LAST_MODIFIED_DATE=IF(pul.LABEL_EN=:labelEn,:curDate, pul.LAST_MODIFIED_DATE) "
                + "WHERE pu.PLANNING_UNIT_ID=:planningUnitId";
        Map<String, Object> params = new HashMap<>();
        params.put("planningUnitId", planningUnit.getPlanningUnitId());
        params.put("unitId", planningUnit.getUnit().getUnitId());
        params.put("active", planningUnit.isActive());
        params.put("labelEn", planningUnit.getLabel().getLabel_en());
        params.put("curUser", curUser.getUserId());
        params.put("curDate", curDate);
        return this.namedParameterJdbcTemplate.update(sqlString, params);
    }

    @Override
    public PlanningUnit getPlanningUnitById(int planningUnitId, CustomUserDetails curUser) {
        String sqlString = this.sqlListString;
        Map<String, Object> params = new HashMap<>();
        sqlString += " AND pu.PLANNING_UNIT_ID=:planningUnitId ";
        params.put("planningUnitId", planningUnitId);
        if (curUser.getRealm().getRealmId() != -1) {
            sqlString += "AND fu.REALM_ID=:realmId ";
            params.put("realmId", curUser.getRealm().getRealmId());
        }
        return this.namedParameterJdbcTemplate.queryForObject(sqlString, params, new PlanningUnitRowMapper());
    }

    @Override
    public List<PlanningUnit> getPlanningUnitListForSync(String lastSyncDate, CustomUserDetails curUser) {
        String sqlString = this.sqlListString;
        Map<String, Object> params = new HashMap<>();
        sqlString += " AND pu.LAST_MODIFIED_DATE>:lastSyncDate ";
        params.put("lastSyncDate", lastSyncDate);
        if (curUser.getRealm().getRealmId() != -1) {
            sqlString += "AND fu.REALM_ID=:realmId ";
            params.put("realmId", curUser.getRealm().getRealmId());
        }
        return this.namedParameterJdbcTemplate.query(sqlString, params, new PlanningUnitRowMapper());
    }

}
