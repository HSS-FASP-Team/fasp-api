/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.ForecastingUnit;
import cc.altius.FASP.model.PlanningUnit;
import cc.altius.FASP.model.SimpleCodeObject;
import cc.altius.FASP.model.SimpleObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author akil
 */
public class PlanningUnitRowMapper implements RowMapper<PlanningUnit> {
    
    @Override
    public PlanningUnit mapRow(ResultSet rs, int rowNum) throws SQLException {
        PlanningUnit pu = new PlanningUnit(
                rs.getInt("PLANNING_UNIT_ID"),
                new ForecastingUnit(
                        rs.getInt("FORECASTING_UNIT_ID"),
                        new SimpleCodeObject(rs.getInt("REALM_ID"), new LabelRowMapper("REALM_").mapRow(rs, rowNum), rs.getString("REALM_CODE")),
                        new LabelRowMapper("GENERIC_").mapRow(rs, rowNum),
                        new LabelRowMapper("FORECASTING_UNIT_").mapRow(rs, rowNum),
                        new SimpleObject(rs.getInt("PRODUCT_CATEGORY_ID"), new LabelRowMapper("PRODUCT_CATEGORY_").mapRow(rs, rowNum)),
                        new SimpleObject(rs.getInt("TRACER_CATEGORY_ID"), new LabelRowMapper("TRACER_CATEGORY_").mapRow(rs, rowNum))
                ),
                new LabelRowMapper().mapRow(rs, rowNum),
                new SimpleCodeObject(rs.getInt("UNIT_ID"), new LabelRowMapper("UNIT_").mapRow(rs, rowNum), rs.getString("UNIT_CODE")),
                rs.getDouble("MULTIPLIER"),
                rs.getBoolean("ACTIVE")
        );
        pu.getForecastingUnit().setUnit(new SimpleCodeObject(rs.getInt("FU_UNIT_ID"), new LabelRowMapper("FU_UNIT_").mapRow(rs, rowNum), rs.getString("FU_UNIT_CODE")));
        pu.setBaseModel(new BaseModelRowMapper().mapRow(rs, rowNum));
        return pu;
    }
    
}
