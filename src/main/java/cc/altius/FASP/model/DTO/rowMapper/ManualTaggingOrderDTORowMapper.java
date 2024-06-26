/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.DTO.rowMapper;

import cc.altius.FASP.model.DTO.ManualTaggingOrderDTO;
import cc.altius.FASP.model.Label;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author altius
 */
public class ManualTaggingOrderDTORowMapper implements RowMapper<ManualTaggingOrderDTO> {

    @Override
    public ManualTaggingOrderDTO mapRow(ResultSet rs, int arg1) throws SQLException {
        ManualTaggingOrderDTO e = new ManualTaggingOrderDTO();
        e.setErpOrderId(rs.getInt("ERP_ORDER_ID"));
        e.setShipmentId(rs.getInt("SHIPMENT_ID"));
        e.setQuantity(rs.getLong("QTY"));
        e.setOrderNo(rs.getString("ORDER_NO"));
        e.setPrimeLineNo(rs.getInt("PRIME_LINE_NO"));
        e.setRoNo(rs.getString("RO_NO"));
        e.setRoPrimeLineNo(rs.getString("RO_PRIME_LINE_NO"));
        e.setOrderType(rs.getString("ORDER_TYPE"));
        e.setPlanningUnitSkuCode(rs.getString("PLANNING_UNIT_SKU_CODE"));
        e.setProcurementUnitSkuCode(rs.getString("PROCUREMENT_UNIT_SKU_CODE"));
        e.setCurrentEstimatedDeliveryDate(rs.getString("CURRENT_ESTIMATED_DELIVERY_DATE"));
        e.setSupplierName(rs.getString("SUPPLIER_NAME"));
        e.setPrice(rs.getDouble("PRICE"));
        e.setShippingCost(rs.getDouble("SHIPPING_COST"));
        e.setStatus(rs.getString("STATUS"));
        e.setRecipentCountry(rs.getString("RECPIENT_COUNTRY"));
        e.setPlanningUnitLabel(new Label(rs.getInt("LABEL_ID"), rs.getString("LABEL_EN"), rs.getString("LABEL_SP"), rs.getString("LABEL_FR"), rs.getString("LABEL_PR")));
        e.setActive(rs.getBoolean("ACTIVE"));
        e.setNotes(rs.getString("NOTES"));
//        e.setConversionFactor(rs.getBigDecimal("CONVERSION_FACTOR"));
        e.setConversionFactor(rs.getDouble("CONVERSION_FACTOR"));
        return e;
    }

}
