/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.PlanningUnitCapacity;
import cc.altius.FASP.model.SimpleObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author akil
 */
public class PlanningUnitCapacityRowMapper implements RowMapper<PlanningUnitCapacity> {

    @Override
    public PlanningUnitCapacity mapRow(ResultSet rs, int rowNum) throws SQLException {
        PlanningUnitCapacity puc = new PlanningUnitCapacity(
                rs.getInt("PLANNING_UNIT_CAPACITY_ID"),
                new SimpleObject(rs.getInt("PLANNING_UNIT_ID"), new LabelRowMapper("PLANNING_UNIT_").mapRow(rs, rowNum)),
                new SimpleObject(rs.getInt("SUPPLIER_ID"), new LabelRowMapper("SUPPLIER_").mapRow(rs, rowNum)),
                rs.getString("START_DATE"),
                rs.getString("STOP_DATE"),
                rs.getDouble("CAPACITY"),
                rs.getBoolean("ACTIVE")
        );
        puc.setBaseModel(new BaseModelRowMapper().mapRow(rs, rowNum));
        return puc;
    }

}
