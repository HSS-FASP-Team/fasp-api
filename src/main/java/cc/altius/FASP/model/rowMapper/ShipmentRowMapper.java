/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.Shipment;
import cc.altius.FASP.model.SimpleCodeObject;
import cc.altius.FASP.model.SimpleForecastingUnitObject;
import cc.altius.FASP.model.SimpleObject;
import cc.altius.FASP.model.SimplePlanningUnitObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author akil
 */
public class ShipmentRowMapper implements RowMapper<Shipment> {

    @Override
    public Shipment mapRow(ResultSet rs, int i) throws SQLException {
        Shipment s = new Shipment();
        s.setShipmentId(rs.getInt("SHIPMENT_ID"));
        s.setPlanningUnit(
                new SimplePlanningUnitObject(
                        rs.getInt("PLANNING_UNIT_ID"),
                        new LabelRowMapper("PLANNING_UNIT_").mapRow(rs, i),
                        new SimpleForecastingUnitObject(
                                rs.getInt("FORECASTING_UNIT_ID"),
                                new LabelRowMapper("FORECASTING_UNIT_").mapRow(rs, i),
                                new SimpleObject(rs.getInt("PRODUCT_CATEGORY_ID"), new LabelRowMapper("PRODUCT_CATEGORY_").mapRow(rs, i))))
        );
        s.setExpectedDeliveryDate(rs.getDate("EXPECTED_DELIVERY_DATE"));
        s.setSuggestedQty(rs.getDouble("SUGGESTED_QTY"));
        s.setProcurementAgent(new SimpleCodeObject(rs.getInt("PROCUREMENT_AGENT_ID"), new LabelRowMapper("PROCUREMENT_AGENT_").mapRow(rs, i), rs.getString("PROCUREMENT_AGENT_CODE")));
        s.setProcurementUnit(new SimpleObject(rs.getInt("PROCUREMENT_UNIT_ID"), new LabelRowMapper("PROCUREMENT_UNIT_").mapRow(rs, i)));
        s.setSupplier(new SimpleObject(rs.getInt("SUPPLIER_ID"), new LabelRowMapper("SUPPLIER_").mapRow(rs, i)));
        s.setQuantity(rs.getDouble("QUANTITY"));
        s.setRate(rs.getDouble("RATE"));
        s.setProductCost(rs.getDouble("PRODUCT_COST"));
        s.setShipmentMode(rs.getString("SHIPPING_MODE"));
        s.setFreightCost(rs.getDouble("FREIGHT_COST"));
        s.setOrderedDate(rs.getDate("ORDERED_DATE"));
        s.setShippedDate(rs.getDate("SHIPPED_DATE"));
        s.setReceivedDate(rs.getDate("RECEIVED_DATE"));
        s.setShipmentStatus(new SimpleObject(rs.getInt("SHIPMENT_STATUS_ID"), new LabelRowMapper("SHIPMENT_STATUS_").mapRow(rs, i)));
        s.setNotes(rs.getString("NOTES"));
        s.setDataSource(new SimpleObject(rs.getInt("DATA_SOURCE_ID"), new LabelRowMapper("DATA_SOURCE_").mapRow(rs, i)));
        s.setAccountFlag(rs.getBoolean("ACCOUNT_FLAG"));
        s.setErpFlag(rs.getBoolean("ERP_FLAG"));
        s.setVersionId(rs.getInt("VERSION_ID"));
        s.setBaseModel(new BaseModelRowMapper().mapRow(rs, i));
        return s;
    }

}
