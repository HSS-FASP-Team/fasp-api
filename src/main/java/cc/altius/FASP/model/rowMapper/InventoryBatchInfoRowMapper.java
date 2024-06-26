/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.rowMapper;

import cc.altius.FASP.model.Batch;
import cc.altius.FASP.model.InventoryBatchInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author akil
 */
public class InventoryBatchInfoRowMapper implements RowMapper<InventoryBatchInfo> {

    @Override
    public InventoryBatchInfo mapRow(ResultSet rs, int i) throws SQLException {
        InventoryBatchInfo ib = new InventoryBatchInfo();
        ib.setInventoryTransBatchInfoId(rs.getInt("INVENTORY_TRANS_BATCH_INFO_ID"));
        if (rs.wasNull()) {
            return null;
        }
        ib.setBatch(new Batch(rs.getInt("BATCH_ID"), rs.getInt("BATCH_PLANNING_UNIT_ID"), rs.getString("BATCH_NO"), rs.getBoolean("AUTO_GENERATED"), rs.getDate("EXPIRY_DATE")));
        ib.getBatch().setCreatedDate(rs.getDate("BATCH_CREATED_DATE"));
        ib.setAdjustmentQty(rs.getDouble("BATCH_ADJUSTMENT_QTY"));
        if (rs.wasNull()) {
            ib.setAdjustmentQty(null);
        }
        ib.setActualQty(rs.getDouble("BATCH_ACTUAL_QTY"));
        if (rs.wasNull()) {
            ib.setActualQty(null);
        }
        return ib;
    }

}
