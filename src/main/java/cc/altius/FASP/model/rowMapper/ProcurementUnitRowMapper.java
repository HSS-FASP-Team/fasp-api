/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.ForecastingUnit;
import cc.altius.FASP.model.PlanningUnit;
import cc.altius.FASP.model.ProcurementUnit;
import cc.altius.FASP.model.SimpleCodeObject;
import cc.altius.FASP.model.SimpleObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author akil
 */
public class ProcurementUnitRowMapper implements RowMapper<ProcurementUnit> {

    @Override
    public ProcurementUnit mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProcurementUnit pu = new ProcurementUnit(
                rs.getInt("PROCUREMENT_UNIT_ID"),
                new PlanningUnit(
                        rs.getInt("PLANNING_UNIT_ID"),
                        new ForecastingUnit(
                                rs.getInt("FORECASTING_UNIT_ID"),
                                new SimpleCodeObject(rs.getInt("REALM_ID"), new LabelRowMapper("REALM_").mapRow(rs, rowNum), rs.getString("REALM_CODE")),
                                new LabelRowMapper("GENERIC_").mapRow(rs, rowNum),
                                new LabelRowMapper("FORECASTING_UNIT_").mapRow(rs, rowNum),
                                new SimpleObject(rs.getInt("PRODUCT_CATEGORY_ID"), new LabelRowMapper("PRODUCT_CATEGORY_").mapRow(rs, rowNum)),
                                new SimpleObject(rs.getInt("TRACER_CATEGORY_ID"), new LabelRowMapper("TRACER_CATEGORY_").mapRow(rs, rowNum))
                        ),
                        new LabelRowMapper("PLANNING_UNIT_").mapRow(rs, rowNum),
                        new SimpleCodeObject(rs.getInt("PLANNING_UNIT_UNIT_ID"), new LabelRowMapper("PLANNING_UNIT_UNIT_").mapRow(rs, rowNum), rs.getString("PLANNING_UNIT_UNIT_CODE")),
                        rs.getDouble("PLANNING_UNIT_MULTIPLIER"),
                        rs.getBoolean("ACTIVE")
                ),
                new LabelRowMapper().mapRow(rs, rowNum),
                new SimpleObject(rs.getInt("UNIT_ID"), new LabelRowMapper("UNIT_").mapRow(rs, rowNum)),
                rs.getDouble("MULTIPLIER"));
        pu.setHeightQty(rs.getDouble("HEIGHT_QTY"));
        pu.setWidthQty(rs.getDouble("WIDTH_QTY"));
        pu.setLengthQty(rs.getDouble("LENGTH_QTY"));
        pu.setLengthUnit(new SimpleObject(rs.getInt("LENGTH_UNIT_ID"), new LabelRowMapper("LENGTH_UNIT_").mapRow(rs, rowNum)));
        pu.setWeightQty(rs.getDouble("WEIGHT_QTY"));
        pu.setWeightUnit(new SimpleObject(rs.getInt("WEIGHT_UNIT_ID"), new LabelRowMapper("WEIGHT_UNIT_").mapRow(rs, rowNum)));
        pu.setVolumeQty(rs.getDouble("VOLUME_QTY"));
        pu.setVolumeUnit(new SimpleObject(rs.getInt("VOLUME_UNIT_ID"), new LabelRowMapper("VOLUME_UNIT_").mapRow(rs, rowNum)));
        pu.setUnitsPerCase(rs.getDouble("UNITS_PER_CASE"));
        pu.setUnitsPerPalletEuro1(rs.getDouble("UNITS_PER_PALLET_EURO1"));
        pu.setUnitsPerPalletEuro2(rs.getDouble("UNITS_PER_PALLET_EURO2"));
        pu.setUnitsPerContainer(rs.getDouble("UNITS_PER_CONTAINER"));
        pu.setLabeling(rs.getString("LABELING"));
        pu.setSupplier(new SimpleObject(rs.getInt("SUPPLIER_ID"), new LabelRowMapper("SUPPLIER_").mapRow(rs, rowNum)));
        pu.setBaseModel(new BaseModelRowMapper().mapRow(rs, rowNum));
        return pu;
    }

}
