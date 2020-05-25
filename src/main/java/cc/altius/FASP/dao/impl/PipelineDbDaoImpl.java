/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.dao.impl;

import cc.altius.FASP.dao.LabelDao;
import cc.altius.FASP.dao.PipelineDbDao;
import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.Label;
import cc.altius.FASP.model.Program;
import cc.altius.FASP.model.Region;
import cc.altius.FASP.model.pipeline.Pipeline;
import cc.altius.FASP.model.pipeline.PplConsumption;
import cc.altius.FASP.model.pipeline.PplProduct;
import cc.altius.FASP.model.pipeline.PplPrograminfo;
import cc.altius.FASP.model.pipeline.rowMapper.PipelineProductRowMapper;
import cc.altius.FASP.model.pipeline.rowMapper.QatTempProgramResultSetExtractor;
import cc.altius.FASP.model.pipeline.PplShipment;
import cc.altius.FASP.model.pipeline.QatTempConsumption;
import cc.altius.FASP.model.pipeline.QatTempProgramPlanningUnit;
import cc.altius.FASP.model.pipeline.rowMapper.PipelineConsumptionRowMapper;
import cc.altius.FASP.model.pipeline.rowMapper.PplPrograminfoRowMapper;
import cc.altius.FASP.model.pipeline.rowMapper.PplShipmentRowMapper;
import cc.altius.FASP.model.pipeline.rowMapper.QatTemRegionRowMapper;
import cc.altius.FASP.model.pipeline.rowMapper.QatTempConsumptionRowMapper;
import cc.altius.FASP.model.pipeline.rowMapper.QatTempPlanningUnitRowMapper;
import cc.altius.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akil
 */
@Repository
public class PipelineDbDaoImpl implements PipelineDbDao {

    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    private LabelDao labelDao;

    public String sqlListString = "SELECT  "
            + "     p.PROGRAM_ID, p.AIR_FREIGHT_PERC, p.SEA_FREIGHT_PERC, p.PLANNED_TO_DRAFT_LEAD_TIME, p.DRAFT_TO_SUBMITTED_LEAD_TIME,"
            + "     p.SUBMITTED_TO_APPROVED_LEAD_TIME, p.APPROVED_TO_SHIPPED_LEAD_TIME, p.DELIVERED_TO_RECEIVED_LEAD_TIME, p.MONTHS_IN_PAST_FOR_AMC, p.MONTHS_IN_FUTURE_FOR_AMC, "
            + "     p.PROGRAM_NOTES, pm.USERNAME `PROGRAM_MANAGER_USERNAME`, pm.USER_ID `PROGRAM_MANAGER_USER_ID`, "
            + "     pl.LABEL_ID, pl.LABEL_EN, pl.LABEL_FR, pl.LABEL_PR, pl.LABEL_SP, "
            + "     rc.REALM_COUNTRY_ID, r.REALM_ID, r.REALM_CODE, rc.AIR_FREIGHT_PERC `REALM_COUNTRY_AIR_FREIGHT_PERC`, rc.SEA_FREIGHT_PERC `REALM_COUNTRY_SEA_FREIGHT_PERC`, rc.SHIPPED_TO_ARRIVED_AIR_LEAD_TIME `REALM_COUNTRY_SHIPPED_TO_ARRIVED_AIR_LEAD_TIME`, rc.SHIPPED_TO_ARRIVED_SEA_LEAD_TIME `REALM_COUNTRY_SHIPPED_TO_ARRIVED_SEA_LEAD_TIME`, rc.ARRIVED_TO_DELIVERED_LEAD_TIME `REALM_COUNTRY_ARRIVED_TO_DELIVERED_LEAD_TIME`, "
            + "     rl.LABEL_ID `REALM_LABEL_ID`, rl.LABEL_EN `REALM_LABEL_EN`, rl.LABEL_FR `REALM_LABEL_FR`, rl.LABEL_PR `REALM_LABEL_PR`, rl.LABEL_SP `REALM_LABEL_SP`, "
            + "     c.COUNTRY_ID, c.COUNTRY_CODE,  "
            + "     cl.LABEL_ID `COUNTRY_LABEL_ID`, cl.LABEL_EN `COUNTRY_LABEL_EN`, cl.LABEL_FR `COUNTRY_LABEL_FR`, cl.LABEL_PR `COUNTRY_LABEL_PR`, cl.LABEL_SP `COUNTRY_LABEL_SP`, "
            + "     cu.CURRENCY_ID, cu.CURRENCY_CODE, cu.CONVERSION_RATE_TO_USD,  "
            + "     cul.LABEL_ID `CURRENCY_LABEL_ID`, cul.LABEL_EN `CURRENCY_LABEL_EN`, cul.LABEL_FR `CURRENCY_LABEL_FR`, cul.LABEL_PR `CURRENCY_LABEL_PR`, cul.LABEL_SP `CURRENCY_LABEL_SP`, "
            + "     o.ORGANISATION_ID, o.ORGANISATION_CODE, "
            + "     ol.LABEL_ID `ORGANISATION_LABEL_ID`, ol.LABEL_EN `ORGANISATION_LABEL_EN`, ol.LABEL_FR `ORGANISATION_LABEL_FR`, ol.LABEL_PR `ORGANISATION_LABEL_PR`, ol.LABEL_SP `ORGANISATION_LABEL_SP`, "
            + "     ha.HEALTH_AREA_ID, "
            + "     hal.LABEL_ID `HEALTH_AREA_LABEL_ID`, hal.LABEL_EN `HEALTH_AREA_LABEL_EN`, hal.LABEL_FR `HEALTH_AREA_LABEL_FR`, hal.LABEL_PR `HEALTH_AREA_LABEL_PR`, hal.LABEL_SP `HEALTH_AREA_LABEL_SP`, "
            + "     re.REGION_ID, "
            + "     rel.LABEL_ID `REGION_LABEL_ID`, rel.LABEL_EN `REGION_LABEL_EN`, rel.LABEL_FR `REGION_LABEL_FR`, rel.LABEL_PR `REGION_LABEL_PR`, rel.LABEL_SP `REGION_LABEL_SP`, "
            + "     u.UNIT_ID, u.UNIT_CODE, ul.LABEL_ID `UNIT_LABEL_ID`, ul.LABEL_EN `UNIT_LABEL_EN`, ul.LABEL_FR `UNIT_LABEL_FR`, ul.LABEL_PR `UNIT_LABEL_PR`, ul.LABEL_SP `UNIT_LABEL_SP`, "
            //            + "     pv.VERSION_ID, pv.CREATED_DATE `VERSION_CREATED_DATE`, "
            //            + "pvcmb.USER_ID `VERSION_USER_ID`, pvcmb.USERNAME `VERSION_USERNAME`, "
            //            + "     p.ACTIVE, "
            + "cb.USER_ID `CB_USER_ID`, cb.USERNAME `CB_USERNAME`, p.CREATED_DATE, lmb.USER_ID `LMB_USER_ID`, lmb.USERNAME `LMB_USERNAME`, p.LAST_MODIFIED_DATE "
            + " FROM qat_temp_program p  "
            + " LEFT JOIN qat_temp_ap_label pl ON p.LABEL_ID=pl.LABEL_ID "
            + " LEFT JOIN rm_realm_country rc ON p.REALM_COUNTRY_ID=rc.REALM_COUNTRY_ID "
            + " LEFT JOIN rm_realm r ON rc.REALM_ID=r.REALM_ID "
            + " LEFT JOIN ap_label rl ON r.LABEL_ID=rl.LABEL_ID "
            + " LEFT JOIN ap_country c ON rc.COUNTRY_ID=c.COUNTRY_ID "
            + " LEFT JOIN ap_label cl ON c.LABEL_ID=cl.LABEL_ID "
            + " LEFT JOIN ap_currency cu ON rc.DEFAULT_CURRENCY_ID=cu.CURRENCY_ID "
            + " LEFT JOIN ap_label cul ON cu.LABEL_ID=cul.LABEL_ID "
            + " LEFT JOIN rm_organisation o ON p.ORGANISATION_ID=o.ORGANISATION_ID "
            + " LEFT JOIN ap_label ol ON o.LABEL_ID=ol.LABEL_ID "
            + " LEFT JOIN rm_health_area ha ON p.HEALTH_AREA_ID=ha.HEALTH_AREA_ID "
            + " LEFT JOIN ap_label hal ON ha.LABEL_ID=hal.LABEL_ID "
            + " LEFT JOIN us_user pm ON p.PROGRAM_MANAGER_USER_ID=pm.USER_ID "
            + " LEFT JOIN qat_temp_program_region pr ON p.PROGRAM_ID=pr.PROGRAM_ID "
            + " LEFT JOIN rm_region re ON pr.REGION_ID=re.REGION_ID "
            + " LEFT JOIN ap_label rel ON re.LABEL_ID=rel.LABEL_ID "
            + " LEFT JOIN ap_unit u ON rc.PALLET_UNIT_ID=u.UNIT_ID "
            + " LEFT JOIN ap_label ul ON u.LABEL_ID=ul.LABEL_ID "
            + " LEFT JOIN us_user cb ON p.CREATED_BY=cb.USER_ID "
            + " LEFT JOIN us_user lmb ON p.LAST_MODIFIED_BY=lmb.USER_ID "
            //            + " LEFT JOIN rm_program_version cpv ON p.PROGRAM_ID=cpv.PROGRAM_ID AND p.CURRENT_VERSION_ID=cpv.VERSION_ID "
            //            + " LEFT JOIN us_user cpvcb ON cpv.CREATED_BY=cpvcb.USER_ID "
            //            + " LEFT JOIN rm_program_version pv ON p.PROGRAM_ID=pv.PROGRAM_ID "
            //            + " LEFT JOIN us_user pvcmb ON pv.CREATED_BY=pvcmb.USER_ID "
            + " WHERE TRUE ";

    @Override
    @Transactional
    public int savePipelineDbData(Pipeline pipeline, CustomUserDetails curUser) {
        Date curDate = DateUtils.getCurrentDateObject(DateUtils.EST);
        // Save records for adb_pipeline
        SimpleJdbcInsert si = new SimpleJdbcInsert(dataSource).withTableName("adb_pipeline").usingGeneratedKeyColumns("PIPELINE_ID");
        Map<String, Object> params = new HashMap<>();
        params.put("CREATED_BY", curUser.getUserId());
        params.put("CREATED_DATE", curDate);
        int pipelineId = si.executeAndReturnKeyHolder(params).getKey().intValue();

        // Save records for adb_commodityprice
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_commodityprice");
        final List<SqlParameterSource> batchParams = new ArrayList<>();
        if (pipeline.getCommodityprice() != null) {
            pipeline.getCommodityprice().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProductID", c.getProductid());
                tp.put("SupplierID", c.getSupplierid());
                tp.put("dtmEffective", c.getDtmeffective());
                tp.put("UnitPrice", c.getUnitprice());
                tp.put("dtmChanged", c.getDtmchanged());
                tp.put("User", c.getUser());
                tp.put("Note", c.getNote());
                tp.put("fUserDefined", c.getFuserdefined());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_consumption
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_consumption");
        batchParams.clear();
        if (pipeline.getConsumption() != null) {
            pipeline.getConsumption().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProductID", c.getProductid());
                tp.put("ConsStartYear", c.getConsstartyear());
                tp.put("ConsStartMonth", c.getConsstartmonth());
                tp.put("ConsActualFlag", c.getConsactualflag());
                tp.put("ConsNumMonths", c.getConsnummonths());
                tp.put("ConsAmount", c.getConsamount());
                tp.put("ConsDataSourceID", c.getConsdatasourceid());
                tp.put("ConsIflator", c.getConsiflator());
                tp.put("ConsNote", c.getConsnote());
                tp.put("ConsDateChanged", c.getConsdatechanged());
                tp.put("ConsID", c.getConsid());
                tp.put("ConsDisplayNote", c.getConsdisplaynote());
                tp.put("Old consumption", c.getOld_consumption());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_datasource
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_datasource");
        batchParams.clear();
        if (pipeline.getDatasource() != null) {
            pipeline.getDatasource().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("DataSourceID", c.getDatasourceid());
                tp.put("DataSourceName", c.getDatasourcename());
                tp.put("DataSourceTypeID", c.getDatasourcetypeid());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }
        // Save records for adb_fundingsource
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_fundingsource");
        batchParams.clear();
        if (pipeline.getFundingsource() != null) {
            pipeline.getFundingsource().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("FundingSourceID", c.getFundingsourceid());
                tp.put("FundingSourceName", c.getFundingsourcename());
                tp.put("FundingNote", c.getFundingnote());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_inventory
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_inventory");
        batchParams.clear();
        if (pipeline.getInventory() != null) {
            pipeline.getInventory().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProductID", c.getProductid());
                tp.put("Period", c.getPeriod());
                tp.put("InvAmount", c.getInvamount());
                tp.put("InvTransferFlag", c.getInvtransferflag());
                tp.put("InvNote", c.getInvnote());
                tp.put("InvDateChanged", c.getInvdatechanged());
                tp.put("ctrIndex", c.getCtrindex());
                tp.put("InvDisplayNote", c.getInvdisplaynote());
                tp.put("InvDataSourceID", c.getInvdatasourceid());
                tp.put("fImported", c.getFimported());
                tp.put("Old_Inventory", c.getOld_inventory());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_method
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_method");
        batchParams.clear();
        if (pipeline.getMethod() != null) {
            pipeline.getMethod().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("MethodID", c.getMethodid());
                tp.put("MethodName", c.getMethodname());
                tp.put("CYPFactor", c.getCypfactor());
                tp.put("MethodNote", c.getMethodnote());
                tp.put("ParentID", c.getParentid());
                tp.put("CategoryID", c.getCategoryid());
                tp.put("fRollup", c.getFrollup());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_monthlystockarchive
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_monthlystockarchive");
        batchParams.clear();
        if (pipeline.getMonthlystockarchive() != null) {
            pipeline.getMonthlystockarchive().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProductID", c.getProductid());
                tp.put("EOYBalance", c.getEoybalance());
                tp.put("StockYear", c.getStockyear());
                tp.put("StockMonth", c.getStockmonth());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }
        // Save records for adb_paste_errors
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_paste_errors");
        batchParams.clear();
        if (pipeline.getPaste_errors() != null) {
            pipeline.getPaste_errors().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("F1", c.getF1());
                tp.put("F2", c.getF2());
                tp.put("F3", c.getF3());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_product
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_product");
        batchParams.clear();
        if (pipeline.getProduct() != null) {
            pipeline.getProduct().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProductID", c.getProductid());
                tp.put("ProductName", c.getProductname());
                tp.put("ProductMinMonths", c.getProductminmonths());
                tp.put("ProductMaxMonths", c.getProductmaxmonths());
                tp.put("SupplierID", c.getSupplierid());
                tp.put("MethodID", c.getMethodid());
                tp.put("ProductActiveFlag", c.getProductactiveflag());
                tp.put("ProductActiveDate", c.getProductactivedate());
                tp.put("DefaultCaseSize", c.getDefaultcasesize());
                tp.put("ProductNote", c.getProductnote());
                tp.put("ProdCMax", c.getProdcmax());
                tp.put("ProdCMin", c.getProdcmin());
                tp.put("ProdDesStock", c.getProddesstock());
                tp.put("txtInnovatorDrugName", c.getTxtinnovatordrugname());
                tp.put("dblLowestUnitQty", c.getDbllowestunitqty());
                tp.put("txtLowestUnitMeasure", c.getTxtlowestunitmeasure());
                tp.put("txtSubstitutionList", c.getTxtsubstitutionlist());
                tp.put("fPermittedInCountry", c.getFpermittedincountry());
                tp.put("memAvailabilityNotes", c.getMemavailabilitynotes());
                tp.put("fAvailabilityStatus", c.getFavailabilitystatus());
                tp.put("fUserDefined", c.getFuserdefined());
                tp.put("strImportSource", c.getStrimportsource());
                tp.put("BUConversion", c.getBuconversion());
                tp.put("txtPreferenceNotes", c.getTxtpreferencenotes());
                tp.put("lngAMCStart", c.getLngamcstart());
                tp.put("lngAMCMonths", c.getLngamcmonths());
                tp.put("fAMCChanged", c.getFamcchanged());
                tp.put("txtMigrationStatus", c.getTxtmigrationstatus());
                tp.put("txtMigrationStatusDate", c.getTxtmigrationstatusdate());
                tp.put("strType", c.getStrtype());
                tp.put("OldProductID", c.getOldproductid());
                tp.put("OldProductName", c.getOldproductname());
                tp.put("lngBatch", c.getLngbatch());
                tp.put("OldMethodID", c.getOldmethodid());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_productfreightcost
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_productfreightcost");
        batchParams.clear();
        if (pipeline.getProductfreightcost() != null) {
            pipeline.getProductfreightcost().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProductID", c.getProductid());
                tp.put("SupplierID", c.getSupplierid());
                tp.put("FreightCost", c.getFreightcost());
                tp.put("dtmChanged", c.getDtmchanged());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_productsuppliercasesize
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_productsuppliercasesize");
        batchParams.clear();
        if (pipeline.getProductsuppliercasesize() != null) {
            pipeline.getProductsuppliercasesize().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProductID", c.getProductid());
                tp.put("SupplierID", c.getSupplierid());
                tp.put("dtmEffective", c.getDtmeffective());
                tp.put("intCaseSize", c.getIntcasesize());
                tp.put("dtmChanged", c.getDtmchanged());
                tp.put("User", c.getUser());
                tp.put("Note", c.getNote());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_programinfo
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_programinfo");
        batchParams.clear();
        if (pipeline.getPrograminfo() != null) {
            pipeline.getPrograminfo().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ProgramName", c.getProgramname());
                tp.put("DataDirectory", c.getDatadirectory());
                tp.put("Language", c.getLanguage());
                tp.put("DefaultLeadTimePlan", c.getDefaultleadtimeplan());
                tp.put("DefaultLeadTimeOrder", c.getDefaultleadtimeorder());
                tp.put("DefaultLeadTimeShip", c.getDefaultleadtimeship());
                tp.put("DefaultShipCost", c.getDefaultshipcost());
                tp.put("ProgramContact", c.getProgramcontact());
                tp.put("Telephone", c.getTelephone());
                tp.put("Fax", c.getFax());
                tp.put("Email", c.getEmail());
                tp.put("CountryCode", c.getCountrycode());
                tp.put("CountryName", c.getCountryname());
                tp.put("IsCurrent", c.getIscurrent());
                tp.put("Note", c.getNote());
                tp.put("ProgramCode", c.getProgramcode());
                tp.put("IsActive", c.getIsactive());
                tp.put("StartSize", c.getStartsize());
                tp.put("IsDefault", c.getIsdefault());
                tp.put("ArchiveDate", c.getArchivedate());
                tp.put("ArchiveYear", c.getArchiveyear());
                tp.put("ArchiveInclude", c.getArchiveinclude());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_shipment
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_shipment");
        batchParams.clear();
        if (pipeline.getShipment() != null) {
            pipeline.getShipment().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("ShipmentID", c.getShipmentid());
                tp.put("ProductID", c.getProductid());
                tp.put("SupplierID", c.getSupplierid());
                tp.put("ShipDataSourceID", c.getShipdatasourceid());
                tp.put("ShipAmount", c.getShipamount());
                tp.put("ShipPlannedDate", c.getShipplanneddate());
                tp.put("ShipOrderedDate", c.getShipordereddate());
                tp.put("ShipShippedDate", c.getShipshippeddate());
                tp.put("ShipReceivedDate", c.getShipreceiveddate());
                tp.put("ShipStatusCode", c.getShipstatuscode());
                tp.put("ShipNote", c.getShipnote());
                tp.put("ShipDateChanged", c.getShipdatechanged());
                tp.put("ShipFreightCost", c.getShipfreightcost());
                tp.put("ShipValue", c.getShipvalue());
                tp.put("ShipCaseLot", c.getShipcaselot());
                tp.put("ShipDisplayNote", c.getShipdisplaynote());
                tp.put("ShipPO", c.getShippo());
                tp.put("Old Shipment", c.getOld_shipment());
                tp.put("ShipFundingSourceID", c.getShipfundingsourceid());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_source
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_source");
        batchParams.clear();
        if (pipeline.getSource() != null) {
            pipeline.getSource().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("SupplierID", c.getSupplierid());
                tp.put("SupplierName", c.getSuppliername());
                tp.put("SupplierLeadTimePlan", c.getSupplierleadtimeplan());
                tp.put("SupplierLeadTimeOrder", c.getSupplierleadtimeorder());
                tp.put("SupplierLeadTimeShip", c.getSupplierleadtimeship());
                tp.put("SupplierNote", c.getSuppliernote());
                tp.put("Freight", c.getFreight());
                tp.put("DefaultSupplier", c.getDefaultsupplier());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_tblbe_version
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_tblbe_version");
        batchParams.clear();
        if (pipeline.getTblbe_version() != null) {
            pipeline.getTblbe_version().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("sBE_Version", c.getSbe_version());
                tp.put("dtmUpdated", c.getDtmupdated());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_tblimportproducts
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_tblimportproducts");
        batchParams.clear();
        if (pipeline.getTblimportproducts() != null) {
            pipeline.getTblimportproducts().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("strProductID", c.getStrproductid());
                tp.put("strName", c.getStrname());
                tp.put("strDose", c.getStrdose());
                tp.put("lngCYP", c.getLngcyp());
                tp.put("dtmExport", c.getDtmexport());
                tp.put("fProcessed", c.getFprocessed());
                tp.put("lngID", c.getLngid());
                tp.put("strSource", c.getStrsource());
                tp.put("strMapping", c.getStrmapping());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }

        // Save records for adb_tblimportrecords
        si = null;
        si = new SimpleJdbcInsert(dataSource).withTableName("adb_tblimportrecords");
        batchParams.clear();
        if (pipeline.getTblimportrecords() != null) {
            pipeline.getTblimportrecords().forEach((c) -> {
                Map<String, Object> tp = new HashMap<>();
                tp.put("strProductID", c.getStrproductid());
                tp.put("dtmPeriod", c.getDtmperiod());
                tp.put("lngconsumption", c.getLngconsumption());
                tp.put("lngAdjustment", c.getLngadjustment());
                tp.put("dblDataInterval", c.getDbldatainterval());
                tp.put("lngParentID", c.getLngparentid());
                tp.put("PIPELINE_ID", pipelineId);
                batchParams.add(new MapSqlParameterSource(tp));
            });
            SqlParameterSource[] batchSqlSource = new SqlParameterSource[batchParams.size()];
            si.executeBatch(batchParams.toArray(batchSqlSource));
        }
        return pipelineId;
    }

    @Override
    public List<Map<String, Object>> getPipelineProgramList(CustomUserDetails curUser) {
        String sql = "select ap.*,u.USERNAME from adb_pipeline ap \n"
                + "left join us_user u on u.USER_ID=ap.CREATED_BY\n"
                + "where ap.CREATED_BY=:userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", curUser.getUserId());
        return this.namedParameterJdbcTemplate.queryForList(sql, params);
    }

    @Override
    public PplPrograminfo getPipelineProgramInfoById(int pipelineId, CustomUserDetails curUser) {
//        String sql = "SELECT If(rc.COUNTRY_ID IS NULL,ap.CountryName,rc.COUNTRY_ID) as CountryName,ap.Note,ap.ProgramName \n"
//                + "FROM fasp.adb_programinfo ap\n"
//                + "left join ap_label al on upper(al.LABEL_EN)=upper(ap.CountryName) \n"
//                + "OR upper(al.LABEL_FR)=upper(ap.CountryName) OR upper(al.LABEL_SP)=upper(ap.CountryName) \n"
//                + "OR upper(al.LABEL_PR)=upper(ap.CountryName)\n"
//                + "left join ap_country ac on ac.LABEL_ID=al.LABEL_ID\n"
//                + "left join rm_realm_country rc on rc.COUNTRY_ID=ac.COUNTRY_ID \n"
//                + "AND rc.REALM_ID=:realmId AND rc.COUNTRY_ID IS NOT NULL\n"
//                + "where ap.PIPELINE_ID=:pipelineId ;";
        String sql = "SELECT If(rc.REALM_COUNTRY_ID IS NULL,ap.CountryName,rc.REALM_COUNTRY_ID) as CountryName,ap.Note,ap.ProgramName \n"
                + "FROM fasp.adb_programinfo ap\n"
                + "left join ap_label al on upper(al.LABEL_EN)=upper(ap.CountryName) \n"
                + "OR upper(al.LABEL_FR)=upper(ap.CountryName) OR upper(al.LABEL_SP)=upper(ap.CountryName) \n"
                + "OR upper(al.LABEL_PR)=upper(ap.CountryName)\n"
                + "left join ap_country ac on ac.LABEL_ID=al.LABEL_ID\n"
                + "left join rm_realm_country rc on rc.COUNTRY_ID=ac.COUNTRY_ID \n"
                + "AND rc.REALM_ID=:realmId AND rc.COUNTRY_ID IS NOT NULL\n"
                + "where ap.PIPELINE_ID=:pipelineId ;";
        Map<String, Object> params = new HashMap<>();
        params.put("pipelineId", pipelineId);
        params.put("realmId", curUser.getRealm().getRealmId());
        return this.namedParameterJdbcTemplate.queryForObject(sql, params, new PplPrograminfoRowMapper());
    }

    @Override
    @Transactional
    public int addQatTempProgram(Program p, CustomUserDetails curUser, int pipelineId) {
        if (p.getProgramId() != 0) {
            Date curDate = DateUtils.getCurrentDateObject(DateUtils.EST);
            Map<String, Object> params = new HashMap<>();
            params.put("programId", p.getProgramId());
            params.put("labelEn", p.getLabel().getLabel_en());
            params.put("programManagerUserId", p.getProgramManager().getUserId());
            params.put("programNotes", p.getProgramNotes());
            params.put("airFreightPerc", p.getAirFreightPerc());
            params.put("seaFreightPerc", p.getSeaFreightPerc());
            params.put("plannedToDraftLeadTime", p.getPlannedToDraftLeadTime());
            params.put("draftToSubmittedLeadTime", p.getDraftToSubmittedLeadTime());
            params.put("submittedToApprovedLeadTime", p.getSubmittedToApprovedLeadTime());
            params.put("approvedToShippedLeadTime", p.getApprovedToShippedLeadTime());
            params.put("deliveredToReceivedLeadTime", p.getDeliveredToReceivedLeadTime());
            params.put("monthsInPastForAmc", p.getMonthsInPastForAmc());
            params.put("monthsInFutureForAmc", p.getMonthsInFutureForAmc());
//            params.put("active", true);
            params.put("curUser", curUser.getUserId());
            params.put("curDate", curDate);
            String sqlString = "UPDATE qat_temp_program p "
                    + "LEFT JOIN qat_temp_ap_label pl ON p.LABEL_ID=pl.LABEL_ID "
                    + "SET "
                    + "p.PROGRAM_MANAGER_USER_ID=:programManagerUserId, "
                    + "p.PROGRAM_NOTES=:programNotes, "
                    + "p.AIR_FREIGHT_PERC=:airFreightPerc, "
                    + "p.SEA_FREIGHT_PERC=:seaFreightPerc, "
                    + "p.PLANNED_TO_DRAFT_LEAD_TIME=:plannedToDraftLeadTime, "
                    + "p.DRAFT_TO_SUBMITTED_LEAD_TIME=:draftToSubmittedLeadTime, "
                    + "p.SUBMITTED_TO_APPROVED_LEAD_TIME=:submittedToApprovedLeadTime, "
                    + "p.APPROVED_TO_SHIPPED_LEAD_TIME=:approvedToShippedLeadTime, "
                    + "p.DELIVERED_TO_RECEIVED_LEAD_TIME=:deliveredToReceivedLeadTime, "
                    + "p.MONTHS_IN_PAST_FOR_AMC=:monthsInPastForAmc, "
                    + "p.MONTHS_IN_FUTURE_FOR_AMC=:monthsInFutureForAmc, "
                    //                    + "p.ACTIVE=:active, "
                    + "p.LAST_MODIFIED_BY=:curUser, "
                    + "p.LAST_MODIFIED_DATE=:curDate, "
                    + "pl.LABEL_EN=:labelEn, "
                    + "pl.LAST_MODIFIED_BY=:curUser, "
                    + "pl.LAST_MODIFIED_DATE=:curDate "
                    + "WHERE p.PROGRAM_ID=:programId ";
            int rows = this.namedParameterJdbcTemplate.update(sqlString, params);
            params.clear();
            params.put("programId", p.getProgramId());
            this.namedParameterJdbcTemplate.update("DELETE FROM qat_temp_program_region WHERE PROGRAM_ID=:programId", params);
            SimpleJdbcInsert si = new SimpleJdbcInsert(this.dataSource).withTableName("qat_temp_program_region");
            SqlParameterSource[] paramList = new SqlParameterSource[p.getRegionArray().length];
            int i = 0;
            for (String regionId : p.getRegionArray()) {
                params = new HashMap<>();
                params.put("PROGRAM_ID", p.getProgramId());
                params.put("REGION_ID", regionId);
                params.put("CREATED_BY", curUser.getUserId());
                params.put("CREATED_DATE", curDate);
                params.put("LAST_MODIFIED_BY", curUser.getUserId());
                params.put("LAST_MODIFIED_DATE", curDate);
                params.put("ACTIVE", true);
                paramList[i] = new MapSqlParameterSource(params);
                i++;
            }
            si.executeBatch(paramList);
            return rows;

        } else {
            Map<String, Object> params = new HashMap<>();
            Date curDate = DateUtils.getCurrentDateObject(DateUtils.EST);
//        int labelId = this.labelDao.addLabel(p.getLabel(), curUser.getUserId());
            int labelId = this.addQatTempLabel(p.getLabel(), curUser.getUserId());
            SimpleJdbcInsert si = new SimpleJdbcInsert(dataSource).withTableName("qat_temp_program").usingGeneratedKeyColumns("PROGRAM_ID");
            params.put("REALM_COUNTRY_ID", p.getRealmCountry().getRealmCountryId());
            params.put("ORGANISATION_ID", p.getOrganisation().getId());
            params.put("HEALTH_AREA_ID", p.getHealthArea().getId());
            params.put("LABEL_ID", labelId);
            params.put("PROGRAM_MANAGER_USER_ID", p.getProgramManager().getUserId());
            params.put("PROGRAM_NOTES", p.getProgramNotes());
            params.put("AIR_FREIGHT_PERC", p.getAirFreightPerc());
            params.put("SEA_FREIGHT_PERC", p.getSeaFreightPerc());
            params.put("PLANNED_TO_DRAFT_LEAD_TIME", p.getPlannedToDraftLeadTime());
            params.put("DRAFT_TO_SUBMITTED_LEAD_TIME", p.getDraftToSubmittedLeadTime());
            params.put("SUBMITTED_TO_APPROVED_LEAD_TIME", p.getSubmittedToApprovedLeadTime());
            params.put("APPROVED_TO_SHIPPED_LEAD_TIME", p.getApprovedToShippedLeadTime());
            params.put("DELIVERED_TO_RECEIVED_LEAD_TIME", p.getDeliveredToReceivedLeadTime());
            params.put("MONTHS_IN_PAST_FOR_AMC", p.getMonthsInPastForAmc());
            params.put("MONTHS_IN_FUTURE_FOR_AMC", p.getMonthsInFutureForAmc());
            params.put("CURRENT_VERSION_ID", null);
            params.put("ACTIVE", true);
            params.put("CREATED_BY", curUser.getUserId());
            params.put("CREATED_DATE", curDate);
            params.put("LAST_MODIFIED_BY", curUser.getUserId());
            params.put("LAST_MODIFIED_DATE", curDate);
            params.put("PIPELINE_ID", pipelineId);
            int programId = si.executeAndReturnKey(params).intValue();
            si = new SimpleJdbcInsert(this.dataSource).withTableName("qat_temp_program_region");
            SqlParameterSource[] paramList = new SqlParameterSource[p.getRegionArray().length];
            int i = 0;
            for (String rId : p.getRegionArray()) {
                params = new HashMap<>();
                params.put("REGION_ID", rId);
                params.put("PROGRAM_ID", programId);
                params.put("CREATED_BY", curUser.getUserId());
                params.put("CREATED_DATE", curDate);
                params.put("LAST_MODIFIED_BY", curUser.getUserId());
                params.put("LAST_MODIFIED_DATE", curDate);
                params.put("ACTIVE", true);
                paramList[i] = new MapSqlParameterSource(params);
                i++;
            }
            si.executeBatch(paramList);
            params.clear();
//        params.put("curUser", curUser.getUserId());
//        params.put("curDate", curDate);
//        params.put("programId", programId);
//        int versionId = this.namedParameterJdbcTemplate.queryForObject("CALL getVersionId(:programId, :curUser, :curDate)", params, Integer.class);
//        params.put("versionId", versionId);
//        this.namedParameterJdbcTemplate.update("UPDATE rm_program SET CURRENT_VERSION_ID=:versionId WHERE PROGRAM_ID=:programId", params);
            return programId;
        }
    }

    @Override
    public Program getQatTempProgram(CustomUserDetails curUser, int pipelineId) {
        StringBuilder sqlStringBuilder = new StringBuilder(this.sqlListString).append(" AND p.PIPELINE_ID=:pipelineId");
        Map<String, Object> params = new HashMap<>();
        params.put("pipelineId", pipelineId);
        return this.namedParameterJdbcTemplate.query(sqlStringBuilder.toString(), params, new QatTempProgramResultSetExtractor());
    }

    @Override
    public int addQatTempLabel(Label label, int curUser) {
        String curDate = DateUtils.getCurrentDateString(DateUtils.EST, DateUtils.YMDHMS);
        SimpleJdbcInsert si = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("qat_temp_ap_label").usingGeneratedKeyColumns("LABEL_ID");
        Map<String, Object> params = new HashMap<>();
        params.put("LABEL_EN", label.getLabel_en());
        params.put("LABEL_FR", label.getLabel_fr());
        params.put("LABEL_SP", label.getLabel_sp());
        params.put("LABEL_PR", label.getLabel_pr());
        params.put("CREATED_BY", curUser);
        params.put("CREATED_DATE", curDate);
        params.put("LAST_MODIFIED_BY", curUser);
        params.put("LAST_MODIFIED_DATE", curDate);
        return si.executeAndReturnKey(params).intValue();
    }

    @Override
    public List<PplProduct> getPipelineProductListById(CustomUserDetails curUser, int pipelineId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT p.ProductId,p.ProductMinMonths,if(pu.PLANNING_UNIT_ID IS NULL,p.ProductName,pu.PLANNING_UNIT_ID) as planningUnitId\n"
                + "FROM fasp.adb_product p \n"
                + "left join ap_label al on al.LABEL_EN=p.ProductName OR al.LABEL_FR=p.ProductName \n"
                + "or al.LABEL_PR=p.ProductName or al.LABEL_SP=p.ProductName \n"
                + "left join rm_planning_unit pu on pu.LABEL_ID=al.LABEL_ID\n"
                + "where p.PIPELINE_ID=:pipelineId";
        params.put("pipelineId", pipelineId);
        return this.namedParameterJdbcTemplate.query(sql, params, new PipelineProductRowMapper());
    }

    @Override
    public List<PplShipment> getPipelineShipmentdataById(int pipelineId, CustomUserDetails curUser) {
        String sql = "SELECT pu.`PLANNING_UNIT_ID`,ash.`ShipAmount`,ash.`ShipOrderedDate`,ash.`ShipShippedDate`,ash.`ShipReceivedDate`,ash.`ShipNote`,ash.`ShipFreightCost`,ash.`ShipPO` \n"
                + " FROM adb_shipment ash LEFT JOIN \n"
                + "adb_product pr ON pr.`ProductID`=ash.`ProductID` \n"
                + "LEFT JOIN ap_label al ON UPPER(al.LABEL_EN)=UPPER(pr.`ProductName`)  OR UPPER(al.LABEL_FR)=UPPER(pr.`ProductName`) OR UPPER(al.LABEL_SP)=UPPER(pr.`ProductName`) OR UPPER(al.LABEL_PR)=UPPER(pr.`ProductName`)\n"
                + "LEFT JOIN rm_planning_unit pu ON pu.`LABEL_ID`=al.`LABEL_ID`\n"
                + " WHERE ash.`PIPELINE_ID`=:pipelineId";
        Map<String, Object> params = new HashMap<>();
        params.put("pipelineId", pipelineId);
        return this.namedParameterJdbcTemplate.query(sql, params, new PplShipmentRowMapper());

    }

    @Override
    @Transactional
    public int saveQatTempProgramPlanningUnit(QatTempProgramPlanningUnit[] programPlanningUnits, CustomUserDetails curUser, int pipelineId) {
        String sql = " delete from qat_temp_program_planning_unit  where PIPELINE_ID=?";
        this.jdbcTemplate.update(sql, pipelineId);

        SimpleJdbcInsert si = new SimpleJdbcInsert(dataSource).withTableName("qat_temp_program_planning_unit");
        List<SqlParameterSource> insertList = new ArrayList<>();
        int rowsEffected = 0;
        Date curDate = DateUtils.getCurrentDateObject(DateUtils.EST);
        Map<String, Object> params;
        for (QatTempProgramPlanningUnit ppu : programPlanningUnits) {
            // Insert
            params = new HashMap<>();
            params.put("PLANNING_UNIT_ID", ppu.getPlanningUnit().getId());
            params.put("PROGRAM_ID", ppu.getProgram().getId());
            params.put("REORDER_FREQUENCY_IN_MONTHS", ppu.getReorderFrequencyInMonths());
            params.put("MIN_MONTHS_OF_STOCK", ppu.getMinMonthsOfStock());
            params.put("CREATED_DATE", curDate);
            params.put("CREATED_BY", curUser.getUserId());
            params.put("LAST_MODIFIED_DATE", curDate);
            params.put("LAST_MODIFIED_BY", curUser.getUserId());
            params.put("PIPELINE_ID", pipelineId);
            params.put("PIPELINE_PRODUCT_ID", ppu.getProgramPlanningUnitId());
            insertList.add(new MapSqlParameterSource(params));

        }
        if (insertList.size() > 0) {
            SqlParameterSource[] insertParams = new SqlParameterSource[insertList.size()];
            rowsEffected += si.executeBatch(insertList.toArray(insertParams)).length;
        }
        return rowsEffected;
    }

    @Override
    public List<QatTempProgramPlanningUnit> getQatTempPlanningUnitListByPipelienId(int pipelineId, CustomUserDetails curUser) {
        String sql = "SELECT p.PIPELINE_PRODUCT_ID,p.PLANNING_UNIT_ID,p.REORDER_FREQUENCY_IN_MONTHS,p.MIN_MONTHS_OF_STOCK FROM fasp.qat_temp_program_planning_unit p where p.PIPELINE_ID=:pipelineId;";
        Map<String, Object> params = new HashMap<>();
        params.put("pipelineId", pipelineId);
        return this.namedParameterJdbcTemplate.query(sql, params, new QatTempPlanningUnitRowMapper());
    }

    @Override
    public List<PplConsumption> getPipelineConsumptionById(CustomUserDetails curUser, int pipelineId) {
        Map<String, Object> params = new HashMap<>();
//        String sql = "SELECT \n"
//                + "If(rds.DATA_SOURCE_ID IS NULL,ad.DataSourceName,rds.DATA_SOURCE_ID) as DataSourceName,\n"
//                + "date_format(CONCAT(c.ConsStartYear,\"-\",c.ConsStartMonth,\"-01\"),\"%Y-%m-%d\") as consumptionDate,\n"
//                + "c.ConsActualFlag,c.ConsAmount,c.ConsNote\n"
//                + "FROM fasp.adb_consumption c\n"
//                + "left join adb_datasource ad on ad.DataSourceID=c.ConsDataSourceID\n"
//                + "left join ap_label al on upper(al.LABEL_EN)=upper(ad.DataSourceName) \n"
//                + "OR upper(al.LABEL_FR)=upper(ad.DataSourceName) \n"
//                + "OR upper(al.LABEL_SP)=upper(ad.DataSourceName) \n"
//                + "OR upper(al.LABEL_PR)=upper(ad.DataSourceName)\n"
//                + "left join rm_data_source rds on rds.LABEL_ID=al.LABEL_ID AND al.LABEL_ID IS NOT NULL \n"
//                + "where c.PIPELINE_ID=:pipelineId\n"
//                + ";";

        String sql = "SELECT \n"
                + "If(rds.DATA_SOURCE_ID IS NULL,ad.DataSourceName,rds.DATA_SOURCE_ID) as DataSourceName,\n"
                + "date_format(CONCAT(c.ConsStartYear,\"-\",c.ConsStartMonth,\"-01\"),\"%Y-%m-%d\") as consumptionDate,\n"
                + "c.ConsActualFlag,c.ConsAmount,c.ConsNote,qtp.PLANNING_UNIT_ID\n"
                + "FROM fasp.adb_consumption c\n"
                + "left join adb_datasource ad on ad.DataSourceID=c.ConsDataSourceID\n"
                + "left join ap_label al on upper(al.LABEL_EN)=upper(ad.DataSourceName) \n"
                + "OR upper(al.LABEL_FR)=upper(ad.DataSourceName) \n"
                + "OR upper(al.LABEL_SP)=upper(ad.DataSourceName) \n"
                + "OR upper(al.LABEL_PR)=upper(ad.DataSourceName)\n"
                + "left join qat_temp_program_planning_unit qtp on qtp.PIPELINE_PRODUCT_ID=c.ProductID\n"
                + "left join rm_data_source rds on rds.LABEL_ID=al.LABEL_ID AND al.LABEL_ID IS NOT NULL \n"
                + "where c.PIPELINE_ID=:pipelineId\n"
                + ";";
        params.put("pipelineId", pipelineId);
        return this.namedParameterJdbcTemplate.query(sql, params, new PipelineConsumptionRowMapper());

    }

    @Override
    public List<Region> getQatTempRegionsById(CustomUserDetails curUser, int pipelineId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT r.REGION_ID,al.LABEL_EN,al.LABEL_FR,al.LABEL_SP,al.LABEL_PR\n"
                + "FROM fasp.qat_temp_program_region pr\n"
                + "left join rm_region r on r.REGION_ID=pr.REGION_ID\n"
                + "left join ap_label al on al.LABEL_ID=r.LABEL_ID\n"
                + "left join qat_temp_program qp on qp.PROGRAM_ID=pr.PROGRAM_ID\n"
                + "where qp.PIPELINE_ID=:pipelineId";
        params.put("pipelineId", pipelineId);
        return this.namedParameterJdbcTemplate.query(sql, params, new QatTemRegionRowMapper());
    }

    @Override
    @Transactional
    public int saveQatTempConsumption(QatTempConsumption[] consumption, CustomUserDetails curUser, int pipelineId) {
        String sql = " delete from qat_temp_consumption  where PIPELINE_ID=?";
        this.jdbcTemplate.update(sql, pipelineId);

        SimpleJdbcInsert si = new SimpleJdbcInsert(dataSource).withTableName("qat_temp_consumption");
        List<SqlParameterSource> insertList = new ArrayList<>();
        int rowsEffected = 0;
        Date curDate = DateUtils.getCurrentDateObject(DateUtils.EST);
        Map<String, Object> params;
        for (QatTempConsumption ppu : consumption) {
            // Insert
            params = new HashMap<>();
            params.put("REGION_ID", ppu.getRegion().getId());
            params.put("PLANNING_UNIT_ID", ppu.getPlanningUnit().getId());
            params.put("DATA_SOURCE_ID", ppu.getDataSource().getId());
            params.put("CONSUMPTION_DATE", ppu.getConsumptionDate());
            params.put("DAYS_OF_STOCK_OUT", ppu.getDayOfStockOut());
            params.put("NOTES", ppu.getNotes());
            params.put("CONSUMPTION_QUANTITY", ppu.getConsumptionQty());
            params.put("PIPELINE_ID", pipelineId);
            params.put("ACTUAL_FLAG", ppu.isActualFlag());

            params.put("CREATED_DATE", curDate);
            params.put("CREATED_BY", curUser.getUserId());
            params.put("LAST_MODIFIED_DATE", curDate);
            params.put("LAST_MODIFIED_BY", curUser.getUserId());
            insertList.add(new MapSqlParameterSource(params));

        }
        if (insertList.size() > 0) {
            SqlParameterSource[] insertParams = new SqlParameterSource[insertList.size()];
            rowsEffected += si.executeBatch(insertList.toArray(insertParams)).length;
        }
        return rowsEffected;
    }

    @Override
    public List<QatTempConsumption> getQatTempConsumptionListByPipelienId(int pipelineId, CustomUserDetails curUser) {
        String sql = "select * from qat_temp_consumption c where c.PIPELINE_ID=:pipelineId";
        Map<String, Object> params = new HashMap<>();
        params.put("pipelineId", pipelineId);
        return this.namedParameterJdbcTemplate.query(sql, params, new QatTempConsumptionRowMapper());
    }

}
