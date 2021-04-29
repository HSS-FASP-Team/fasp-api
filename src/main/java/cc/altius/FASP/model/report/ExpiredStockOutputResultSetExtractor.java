/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.report;

import cc.altius.FASP.model.Batch;
import cc.altius.FASP.model.SimpleCodeObject;
import cc.altius.FASP.model.SimpleObject;
import cc.altius.FASP.model.rowMapper.LabelRowMapper;
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
public class ExpiredStockOutputResultSetExtractor implements ResultSetExtractor<List<ExpiredStockOutput>> {

    @Override
    public List<ExpiredStockOutput> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<ExpiredStockOutput> esList = new LinkedList<>();
        while (rs.next()) {
            ExpiredStockOutput es = new ExpiredStockOutput();
            es.setProgram(new SimpleCodeObject(rs.getInt("PROGRAM_ID"), new LabelRowMapper("PROGRAM_").mapRow(rs, 1), rs.getString("PROGRAM_CODE")));
            es.setPlanningUnit(new SimpleObject(rs.getInt("PLANNING_UNIT_ID"), new LabelRowMapper("PLANNING_UNIT_").mapRow(rs, 1)));
            es.setBatchInfo(new Batch(rs.getInt("BATCH_ID"), rs.getInt("PLANNING_UNIT_ID"), rs.getString("BATCH_NO"), rs.getBoolean("AUTO_GENERATED"), rs.getDate("EXPIRY_DATE"), rs.getTimestamp("CREATED_DATE")));
            es.setExpiredQty(rs.getLong("EXPIRED_STOCK"));
            int idx = esList.indexOf(es);
            if (idx == -1) {
                esList.add(es);
            } else {
                es = esList.get(idx);
            }
            SimpleBatchQuantityWithTransHistory sb = new SimpleBatchQuantityWithTransHistory(
                    rs.getDate("BATCH_TRANS_DATE"),
                    es.getBatchInfo().getBatchId(),
                    es.getBatchInfo().getBatchNo(),
                    es.getBatchInfo().getExpiryDate(),
                    es.getBatchInfo().isAutoGenerated(),
                    rs.getLong("BATCH_CLOSING_BALANCE"),
                    rs.getLong("BATCH_CLOSING_BALANCE_WPS"),
                    rs.getLong("BATCH_EXPIRED_STOCK"),
                    rs.getLong("BATCH_EXPIRED_STOCK_WPS"),
                    es.getBatchInfo().getCreatedDate(),
                    rs.getLong("BATCH_SHIPMENT_QTY"),
                    rs.getLong("BATCH_SHIPMENT_QTY_WPS"));
            sb.setOpeningBalance(rs.getLong("BATCH_OPENING_BALANCE"));
            sb.setOpeningBalanceWps(rs.getLong("BATCH_OPENING_BALANCE_WPS"));
            sb.setConsumptionQty(rs.getLong("BATCH_CONSUMPTION_QTY"));
            if (rs.wasNull()) {
                sb.setConsumptionQty(null);
            }
            sb.setStockQty(rs.getLong("BATCH_STOCK_MULTIPLIED_QTY"));
            if (rs.wasNull()) {
                sb.setStockQty(null);
            }
            sb.setAdjustmentQty(rs.getLong("BATCH_ADJUSTMENT_MULTIPLIED_QTY"));
            if (rs.wasNull()) {
                sb.setAdjustmentQty(null);
            }
            sb.setUnallocatedQty(rs.getLong("BATCH_CALCULATED_CONSUMPTION_QTY"));
            if (rs.wasNull()) {
                sb.setUnallocatedQty(null);
            }
            sb.setUnallocatedQtyWps(rs.getLong("BATCH_CALCULATED_CONSUMPTION_QTY_WPS"));
            if (rs.wasNull()) {
                sb.setUnallocatedQtyWps(null);
            }
            es.getBatchHistory().add(sb);
        }

        return esList;
    }

}
