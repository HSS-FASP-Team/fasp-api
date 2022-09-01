ALTER TABLE `fasp`.`rm_shipment_trans` 
ADD COLUMN `REALM_COUNTRY_PLANNING_UNIT_ID` INT(10) NULL AFTER `PLANNING_UNIT_ID`,
ADD COLUMN `SHIPMENT_RCPU_QTY` DOUBLE(16,4) NULL AFTER `SHIPMENT_QTY`;

USE `fasp`;
DROP procedure IF EXISTS `getShipmentData`;

DELIMITER $$
USE `fasp`$$
CREATE DEFINER=`faspUser`@`%` PROCEDURE `getShipmentData`(PROGRAM_ID INT(10), VERSION_ID INT (10), SHIPMENT_ACTIVE TINYINT(1), PLANNING_UNIT_ACTIVE TINYINT(1))
BEGIN
    SET @programId = PROGRAM_ID;
    SET @versionId = VERSION_ID;
    SET @shipmentActive = SHIPMENT_ACTIVE;
    SET @planningUnitActive = PLANNING_UNIT_ACTIVE;
    SET @sql1 = "";	
    IF @versionId = -1 THEN
        SELECT MAX(pv.VERSION_ID) INTO @versionId FROM rm_program_version pv WHERE pv.PROGRAM_ID=@programId;
    END IF;
    
    SELECT 
        st.*, stbi.SHIPMENT_TRANS_BATCH_INFO_ID, stbi.BATCH_ID, bi.PLANNING_UNIT_ID `BATCH_PLANNING_UNIT_ID`, bi.BATCH_NO, bi.AUTO_GENERATED, bi.EXPIRY_DATE, bi.CREATED_DATE `BATCH_CREATED_DATE`, stbi.BATCH_SHIPMENT_QTY `BATCH_SHIPMENT_QTY` 
    FROM (
        SELECT
            s.SHIPMENT_ID, s.PARENT_SHIPMENT_ID, st.EXPECTED_DELIVERY_DATE, st.PLANNED_DATE, st.SUBMITTED_DATE, st.APPROVED_DATE, st.SHIPPED_DATE, st.ARRIVED_DATE, st.RECEIVED_DATE, st.SHIPMENT_QTY, st.SHIPMENT_RCPU_QTY, pu.MULTIPLIER `CONVERSION_FACTOR`, st.RATE, st.PRODUCT_COST, st.FREIGHT_COST, st.SHIPMENT_MODE, s.SUGGESTED_QTY, st.ACCOUNT_FLAG, st.ERP_FLAG, st.ORDER_NO, st.PRIME_LINE_NO, st.VERSION_ID, st.NOTES, st.SHIPMENT_TRANS_ID, st.PARENT_LINKED_SHIPMENT_ID,
            p.PROGRAM_ID, p.LABEL_ID `PROGRAM_LABEL_ID`, p.LABEL_EN `PROGRAM_LABEL_EN`, p.LABEL_FR `PROGRAM_LABEL_FR`, p.LABEL_SP `PROGRAM_LABEL_SP`, p.LABEL_PR `PROGRAM_LABEL_PR`,
            pa.PROCUREMENT_AGENT_ID, pa.PROCUREMENT_AGENT_CODE, pa.`COLOR_HTML_CODE`, pa.LABEL_ID `PROCUREMENT_AGENT_LABEL_ID`, pa.LABEL_EN `PROCUREMENT_AGENT_LABEL_EN`, pa.LABEL_FR `PROCUREMENT_AGENT_LABEL_FR`, pa.LABEL_SP `PROCUREMENT_AGENT_LABEL_SP`, pa.LABEL_PR `PROCUREMENT_AGENT_LABEL_PR`,
            pu.PLANNING_UNIT_ID, pu.LABEL_ID `PLANNING_UNIT_LABEL_ID`, pu.LABEL_EN `PLANNING_UNIT_LABEL_EN`, pu.LABEL_FR `PLANNING_UNIT_LABEL_FR`, pu.LABEL_SP `PLANNING_UNIT_LABEL_SP`, pu.LABEL_PR `PLANNING_UNIT_LABEL_PR`,
            fu.FORECASTING_UNIT_ID, fu.LABEL_ID `FORECASTING_UNIT_LABEL_ID`, fu.LABEL_EN `FORECASTING_UNIT_LABEL_EN`, fu.LABEL_FR `FORECASTING_UNIT_LABEL_FR`, fu.LABEL_SP `FORECASTING_UNIT_LABEL_SP`, fu.LABEL_PR `FORECASTING_UNIT_LABEL_PR`,
            pc.PRODUCT_CATEGORY_ID, pc.LABEL_ID `PRODUCT_CATEGORY_LABEL_ID`, pc.LABEL_EN `PRODUCT_CATEGORY_LABEL_EN`, pc.LABEL_FR `PRODUCT_CATEGORY_LABEL_FR`, pc.LABEL_SP `PRODUCT_CATEGORY_LABEL_SP`, pc.LABEL_PR `PRODUCT_CATEGORY_LABEL_PR`,
            pru.PROCUREMENT_UNIT_ID, pru.LABEL_ID `PROCUREMENT_UNIT_LABEL_ID`, pru.LABEL_EN `PROCUREMENT_UNIT_LABEL_EN`, pru.LABEL_FR `PROCUREMENT_UNIT_LABEL_FR`, pru.LABEL_SP `PROCUREMENT_UNIT_LABEL_SP`, pru.LABEL_PR `PROCUREMENT_UNIT_LABEL_PR`,
            rcpu.REALM_COUNTRY_PLANNING_UNIT_ID `RCPU_ID`, rcpu.LABEL_ID `RCPU_LABEL_ID`, rcpu.LABEL_EN `RCPU_LABEL_EN`, rcpu.LABEL_FR `RCPU_LABEL_FR`, rcpu.LABEL_SP `RCPU_LABEL_SP`, rcpu.LABEL_PR `RCPU_LABEL_PR`, rcpu.MULTIPLIER `RCPU_MULTIPLIER`,
            su.SUPPLIER_ID, su.LABEL_ID `SUPPLIER_LABEL_ID`, su.LABEL_EN `SUPPLIER_LABEL_EN`, su.LABEL_FR `SUPPLIER_LABEL_FR`, su.LABEL_SP `SUPPLIER_LABEL_SP`, su.LABEL_PR `SUPPLIER_LABEL_PR`,
            shs.SHIPMENT_STATUS_ID, shs.LABEL_ID `SHIPMENT_STATUS_LABEL_ID`, shs.LABEL_EN `SHIPMENT_STATUS_LABEL_EN`, shs.LABEL_FR `SHIPMENT_STATUS_LABEL_FR`, shs.LABEL_SP `SHIPMENT_STATUS_LABEL_SP`, shs.LABEL_PR `SHIPMENT_STATUS_LABEL_PR`,
            ds.DATA_SOURCE_ID, ds.LABEL_ID `DATA_SOURCE_LABEL_ID`, ds.LABEL_EN `DATA_SOURCE_LABEL_EN`, ds.LABEL_FR `DATA_SOURCE_LABEL_FR`, ds.LABEL_SP `DATA_SOURCE_LABEL_SP`, ds.LABEL_PR `DATA_SOURCE_LABEL_PR`,
            sc.CURRENCY_ID `SHIPMENT_CURRENCY_ID`, sc.`CURRENCY_CODE` `SHIPMENT_CURRENCY_CODE`, s.CONVERSION_RATE_TO_USD `SHIPMENT_CONVERSION_RATE_TO_USD`, 
            sc.LABEL_ID `SHIPMENT_CURRENCY_LABEL_ID`, sc.LABEL_EN `SHIPMENT_CURRENCY_LABEL_EN`, sc.LABEL_FR `SHIPMENT_CURRENCY_LABEL_FR`, sc.LABEL_SP `SHIPMENT_CURRENCY_LABEL_SP`, sc.LABEL_PR `SHIPMENT_CURRENCY_LABEL_PR`,
            st.EMERGENCY_ORDER, st.LOCAL_PROCUREMENT, 
            cb.USER_ID `CB_USER_ID`, cb.USERNAME `CB_USERNAME`, s.CREATED_DATE, lmb.USER_ID `LMB_USER_ID`, lmb.USERNAME `LMB_USERNAME`, st.LAST_MODIFIED_DATE, st.ACTIVE,
            bc.CURRENCY_ID `BUDGET_CURRENCY_ID`, bc.CURRENCY_CODE `BUDGET_CURRENCY_CODE`, b.CONVERSION_RATE_TO_USD `BUDGET_CURRENCY_CONVERSION_RATE_TO_USD`, bc.LABEL_ID `BUDGET_CURRENCY_LABEL_ID`, bc.LABEL_EN `BUDGET_CURRENCY_LABEL_EN`, bc.LABEL_FR `BUDGET_CURRENCY_LABEL_FR`, bc.LABEL_SP `BUDGET_CURRENCY_LABEL_SP`, bc.LABEL_PR `BUDGET_CURRENCY_LABEL_PR`, 
            b.BUDGET_ID, b.BUDGET_CODE, b.LABEL_ID `BUDGET_LABEL_ID`, b.LABEL_EN `BUDGET_LABEL_EN`, b.LABEL_FR `BUDGET_LABEL_FR`, b.LABEL_SP `BUDGET_LABEL_SP`, b.LABEL_PR `BUDGET_LABEL_PR`,
            fs.FUNDING_SOURCE_ID, fs.FUNDING_SOURCE_CODE, fs.LABEL_ID `FUNDING_SOURCE_LABEL_ID`, fs.LABEL_EN `FUNDING_SOURCE_LABEL_EN`, fs.LABEL_FR `FUNDING_SOURCE_LABEL_FR`, fs.LABEL_SP `FUNDING_SOURCE_LABEL_SP`, fs.LABEL_PR `FUNDING_SOURCE_LABEL_PR`
        FROM (
            SELECT st.SHIPMENT_ID, MAX(st.VERSION_ID) MAX_VERSION_ID FROM rm_shipment s LEFT JOIN rm_shipment_trans st ON s.SHIPMENT_ID=st.SHIPMENT_ID WHERE (@versiONId=-1 OR st.VERSION_ID<=@versiONId) AND s.PROGRAM_ID=@programId GROUP BY st.SHIPMENT_ID
        ) ts 
        LEFT JOIN rm_shipment s ON ts.SHIPMENT_ID=s.SHIPMENT_ID
        LEFT JOIN rm_shipment_trans st ON ts.SHIPMENT_ID=st.SHIPMENT_ID AND ts.MAX_VERSION_ID=st.VERSION_ID
        LEFT JOIN vw_program p ON s.PROGRAM_ID=p.PROGRAM_ID
        LEFT JOIN vw_procurement_agent pa ON st.PROCUREMENT_AGENT_ID=pa.PROCUREMENT_AGENT_ID
        LEFT JOIN vw_planning_unit pu ON st.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID
        LEFT JOIN vw_realm_country_planning_unit rcpu ON st.REALM_COUNTRY_PLANNING_UNIT_ID=rcpu.REALM_COUNTRY_PLANNING_UNIT_ID
        LEFT JOIN vw_forecasting_unit fu ON pu.FORECASTING_UNIT_ID=fu.FORECASTING_UNIT_ID
        LEFT JOIN vw_product_category pc ON fu.PRODUCT_CATEGORY_ID=pc.PRODUCT_CATEGORY_ID
        LEFT JOIN vw_procurement_unit pru ON st.PROCUREMENT_UNIT_ID=pru.PROCUREMENT_UNIT_ID
        LEFT JOIN vw_supplier su ON st.SUPPLIER_ID=su.SUPPLIER_ID
        LEFT JOIN vw_shipment_status shs ON st.SHIPMENT_STATUS_ID=shs.SHIPMENT_STATUS_ID
        LEFT JOIN vw_data_source ds ON st.DATA_SOURCE_ID=ds.DATA_SOURCE_ID
        LEFT JOIN us_user cb ON s.CREATED_BY=cb.USER_ID
        LEFT JOIN us_user lmb ON st.LAST_MODIFIED_BY=lmb.USER_ID
        LEFT JOIN vw_currency sc ON s.CURRENCY_ID=sc.CURRENCY_ID
        LEFT JOIN vw_budget b ON st.BUDGET_ID=b.BUDGET_ID
        LEFT JOIN vw_currency bc ON b.CURRENCY_ID=bc.CURRENCY_ID
        LEFT JOIN vw_funding_source fs ON st.FUNDING_SOURCE_ID=fs.FUNDING_SOURCE_ID 
        WHERE (@shipmentActive = FALSE OR st.ACTIVE)
    ) st  
    LEFT JOIN rm_shipment_trans_batch_info stbi ON st.SHIPMENT_TRANS_ID = stbi.SHIPMENT_TRANS_ID
    LEFT JOIN rm_program_planning_unit ppu ON st.PLANNING_UNIT_ID=ppu.PLANNING_UNIT_ID AND ppu.PROGRAM_ID=@programId
    LEFT JOIN rm_batch_info bi ON stbi.BATCH_ID=bi.BATCH_ID
    WHERE (@planningUnitActive = FALSE OR ppu.ACTIVE)
    ORDER BY st.PLANNING_UNIT_ID, COALESCE(st.RECEIVED_DATE, st.EXPECTED_DELIVERY_DATE), bi.EXPIRY_DATE, bi.BATCH_ID; 
END$$

DELIMITER ;
;



USE `fasp`;
DROP procedure IF EXISTS `getNotLinkedQatShipments`;

DELIMITER $$
USE `fasp`$$
CREATE DEFINER=`faspUser`@`%` PROCEDURE `getNotLinkedQatShipments`(VAR_PROGRAM_ID INT(10), VAR_VERSION_ID INT (10), VAR_PROCUREMENT_AGENT_ID INT (10),VAR_PLANNING_UNIT_IDS TEXT)
BEGIN
    SET @programId = VAR_PROGRAM_ID;
    SET @versionId = VAR_VERSION_ID;
    SET @procurementAgentId = VAR_PROCUREMENT_AGENT_ID;
    SET @shipmentActive = 1;
    SET @planningUnitActive = 1;
    SET @planningUnitIds = VAR_PLANNING_UNIT_IDS;
    SET @sql1 = "";	
    IF @versionId = -1 THEN
        SELECT MAX(pv.VERSION_ID) INTO @versionId FROM rm_program_version pv WHERE pv.PROGRAM_ID=@programId;
    END IF;
      
    SELECT 
        st.*, stbi.SHIPMENT_TRANS_BATCH_INFO_ID, stbi.BATCH_ID, bi.PLANNING_UNIT_ID `BATCH_PLANNING_UNIT_ID`, bi.BATCH_NO, bi.AUTO_GENERATED, bi.EXPIRY_DATE, bi.CREATED_DATE `BATCH_CREATED_DATE`, stbi.BATCH_SHIPMENT_QTY `BATCH_SHIPMENT_QTY` 
    FROM (
        SELECT
            s.SHIPMENT_ID, s.PARENT_SHIPMENT_ID,null as PARENT_LINKED_SHIPMENT_ID, st.EXPECTED_DELIVERY_DATE, st.PLANNED_DATE, st.SUBMITTED_DATE, st.APPROVED_DATE, st.SHIPPED_DATE, st.ARRIVED_DATE, st.RECEIVED_DATE, st.SHIPMENT_QTY, st.SHIPMENT_RCPU_QTY, pu.MULTIPLIER `CONVERSION_FACTOR`, st.RATE, st.PRODUCT_COST, st.FREIGHT_COST, st.SHIPMENT_MODE, s.SUGGESTED_QTY, st.ACCOUNT_FLAG, st.ERP_FLAG, st.ORDER_NO, st.PRIME_LINE_NO, st.VERSION_ID, st.NOTES, st.SHIPMENT_TRANS_ID, 
            p.PROGRAM_ID, p.LABEL_ID `PROGRAM_LABEL_ID`, p.LABEL_EN `PROGRAM_LABEL_EN`, p.LABEL_FR `PROGRAM_LABEL_FR`, p.LABEL_SP `PROGRAM_LABEL_SP`, p.LABEL_PR `PROGRAM_LABEL_PR`,
            pa.PROCUREMENT_AGENT_ID, pa.PROCUREMENT_AGENT_CODE, pa.`COLOR_HTML_CODE`, pa.LABEL_ID `PROCUREMENT_AGENT_LABEL_ID`, pa.LABEL_EN `PROCUREMENT_AGENT_LABEL_EN`, pa.LABEL_FR `PROCUREMENT_AGENT_LABEL_FR`, pa.LABEL_SP `PROCUREMENT_AGENT_LABEL_SP`, pa.LABEL_PR `PROCUREMENT_AGENT_LABEL_PR`,
            pu.PLANNING_UNIT_ID, pu.LABEL_ID `PLANNING_UNIT_LABEL_ID`, pu.LABEL_EN `PLANNING_UNIT_LABEL_EN`, pu.LABEL_FR `PLANNING_UNIT_LABEL_FR`, pu.LABEL_SP `PLANNING_UNIT_LABEL_SP`, pu.LABEL_PR `PLANNING_UNIT_LABEL_PR`,
            rcpu.REALM_COUNTRY_PLANNING_UNIT_ID as RCPU_ID, rcpu.LABEL_ID `RCPU_LABEL_ID`, rcpu.LABEL_EN `RCPU_LABEL_EN`, rcpu.LABEL_FR `RCPU_LABEL_FR`, rcpu.LABEL_SP `RCPU_LABEL_SP`, rcpu.LABEL_PR `RCPU_LABEL_PR`, rcpu.MULTIPLIER `RCPU_MULTIPLIER`,
            fu.FORECASTING_UNIT_ID, fu.LABEL_ID `FORECASTING_UNIT_LABEL_ID`, fu.LABEL_EN `FORECASTING_UNIT_LABEL_EN`, fu.LABEL_FR `FORECASTING_UNIT_LABEL_FR`, fu.LABEL_SP `FORECASTING_UNIT_LABEL_SP`, fu.LABEL_PR `FORECASTING_UNIT_LABEL_PR`,
            pc.PRODUCT_CATEGORY_ID, pc.LABEL_ID `PRODUCT_CATEGORY_LABEL_ID`, pc.LABEL_EN `PRODUCT_CATEGORY_LABEL_EN`, pc.LABEL_FR `PRODUCT_CATEGORY_LABEL_FR`, pc.LABEL_SP `PRODUCT_CATEGORY_LABEL_SP`, pc.LABEL_PR `PRODUCT_CATEGORY_LABEL_PR`,
            pru.PROCUREMENT_UNIT_ID, pru.LABEL_ID `PROCUREMENT_UNIT_LABEL_ID`, pru.LABEL_EN `PROCUREMENT_UNIT_LABEL_EN`, pru.LABEL_FR `PROCUREMENT_UNIT_LABEL_FR`, pru.LABEL_SP `PROCUREMENT_UNIT_LABEL_SP`, pru.LABEL_PR `PROCUREMENT_UNIT_LABEL_PR`,
            su.SUPPLIER_ID, su.LABEL_ID `SUPPLIER_LABEL_ID`, su.LABEL_EN `SUPPLIER_LABEL_EN`, su.LABEL_FR `SUPPLIER_LABEL_FR`, su.LABEL_SP `SUPPLIER_LABEL_SP`, su.LABEL_PR `SUPPLIER_LABEL_PR`,
            shs.SHIPMENT_STATUS_ID, shs.LABEL_ID `SHIPMENT_STATUS_LABEL_ID`, shs.LABEL_EN `SHIPMENT_STATUS_LABEL_EN`, shs.LABEL_FR `SHIPMENT_STATUS_LABEL_FR`, shs.LABEL_SP `SHIPMENT_STATUS_LABEL_SP`, shs.LABEL_PR `SHIPMENT_STATUS_LABEL_PR`,
            ds.DATA_SOURCE_ID, ds.LABEL_ID `DATA_SOURCE_LABEL_ID`, ds.LABEL_EN `DATA_SOURCE_LABEL_EN`, ds.LABEL_FR `DATA_SOURCE_LABEL_FR`, ds.LABEL_SP `DATA_SOURCE_LABEL_SP`, ds.LABEL_PR `DATA_SOURCE_LABEL_PR`,
            sc.CURRENCY_ID `SHIPMENT_CURRENCY_ID`, sc.`CURRENCY_CODE` `SHIPMENT_CURRENCY_CODE`, s.CONVERSION_RATE_TO_USD `SHIPMENT_CONVERSION_RATE_TO_USD`, 
            sc.LABEL_ID `SHIPMENT_CURRENCY_LABEL_ID`, sc.LABEL_EN `SHIPMENT_CURRENCY_LABEL_EN`, sc.LABEL_FR `SHIPMENT_CURRENCY_LABEL_FR`, sc.LABEL_SP `SHIPMENT_CURRENCY_LABEL_SP`, sc.LABEL_PR `SHIPMENT_CURRENCY_LABEL_PR`,
            st.EMERGENCY_ORDER, st.LOCAL_PROCUREMENT, 
            cb.USER_ID `CB_USER_ID`, cb.USERNAME `CB_USERNAME`, s.CREATED_DATE, lmb.USER_ID `LMB_USER_ID`, lmb.USERNAME `LMB_USERNAME`, st.LAST_MODIFIED_DATE, st.ACTIVE,
            bc.CURRENCY_ID `BUDGET_CURRENCY_ID`, bc.CURRENCY_CODE `BUDGET_CURRENCY_CODE`, b.CONVERSION_RATE_TO_USD `BUDGET_CURRENCY_CONVERSION_RATE_TO_USD`, bc.LABEL_ID `BUDGET_CURRENCY_LABEL_ID`, bc.LABEL_EN `BUDGET_CURRENCY_LABEL_EN`, bc.LABEL_FR `BUDGET_CURRENCY_LABEL_FR`, bc.LABEL_SP `BUDGET_CURRENCY_LABEL_SP`, bc.LABEL_PR `BUDGET_CURRENCY_LABEL_PR`, 
            b.BUDGET_ID, b.BUDGET_CODE, b.LABEL_ID `BUDGET_LABEL_ID`, b.LABEL_EN `BUDGET_LABEL_EN`, b.LABEL_FR `BUDGET_LABEL_FR`, b.LABEL_SP `BUDGET_LABEL_SP`, b.LABEL_PR `BUDGET_LABEL_PR`,
            fs.FUNDING_SOURCE_ID, fs.FUNDING_SOURCE_CODE, fs.LABEL_ID `FUNDING_SOURCE_LABEL_ID`, fs.LABEL_EN `FUNDING_SOURCE_LABEL_EN`, fs.LABEL_FR `FUNDING_SOURCE_LABEL_FR`, fs.LABEL_SP `FUNDING_SOURCE_LABEL_SP`, fs.LABEL_PR `FUNDING_SOURCE_LABEL_PR`
        FROM (
            SELECT st.SHIPMENT_ID, MAX(st.VERSION_ID) MAX_VERSION_ID FROM rm_shipment s LEFT JOIN rm_shipment_trans st ON s.SHIPMENT_ID=st.SHIPMENT_ID WHERE (@versiONId=-1 OR st.VERSION_ID<=@versiONId) AND s.PROGRAM_ID=@programId GROUP BY st.SHIPMENT_ID
        ) ts 
        LEFT JOIN rm_shipment s ON ts.SHIPMENT_ID=s.SHIPMENT_ID
        LEFT JOIN rm_shipment_trans st ON ts.SHIPMENT_ID=st.SHIPMENT_ID AND ts.MAX_VERSION_ID=st.VERSION_ID
        LEFT JOIN vw_program p ON s.PROGRAM_ID=p.PROGRAM_ID
        LEFT JOIN vw_procurement_agent pa ON st.PROCUREMENT_AGENT_ID=pa.PROCUREMENT_AGENT_ID
        LEFT JOIN vw_planning_unit pu ON st.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID
        LEFT JOIN vw_realm_country_planning_unit rcpu ON st.REALM_COUNTRY_PLANNING_UNIT_ID=rcpu.REALM_COUNTRY_PLANNING_UNIT_ID
        LEFT JOIN vw_forecasting_unit fu ON pu.FORECASTING_UNIT_ID=fu.FORECASTING_UNIT_ID
        LEFT JOIN vw_product_category pc ON fu.PRODUCT_CATEGORY_ID=pc.PRODUCT_CATEGORY_ID
        LEFT JOIN vw_procurement_unit pru ON st.PROCUREMENT_UNIT_ID=pru.PROCUREMENT_UNIT_ID
        LEFT JOIN vw_supplier su ON st.SUPPLIER_ID=su.SUPPLIER_ID
        LEFT JOIN vw_shipment_status shs ON st.SHIPMENT_STATUS_ID=shs.SHIPMENT_STATUS_ID
        LEFT JOIN vw_data_source ds ON st.DATA_SOURCE_ID=ds.DATA_SOURCE_ID
        LEFT JOIN us_user cb ON s.CREATED_BY=cb.USER_ID
        LEFT JOIN us_user lmb ON st.LAST_MODIFIED_BY=lmb.USER_ID
        LEFT JOIN vw_currency sc ON s.CURRENCY_ID=sc.CURRENCY_ID
        LEFT JOIN vw_budget b ON st.BUDGET_ID=b.BUDGET_ID
        LEFT JOIN vw_currency bc ON b.CURRENCY_ID=bc.CURRENCY_ID
        LEFT JOIN vw_funding_source fs ON st.FUNDING_SOURCE_ID=fs.FUNDING_SOURCE_ID 
        WHERE 
			(@shipmentActive = FALSE OR st.ACTIVE) 
            AND (FIND_IN_SET(st.PLANNING_UNIT_ID, @planningUnitIds) OR @planningUnitIds='') 
            AND st.ERP_FLAG=0 
            AND st.ACCOUNT_FLAG 
            AND st.SHIPMENT_STATUS_ID IN (3,4,5,6,9,7)
            AND st.PROCUREMENT_AGENT_ID=@procurementAgentId
            AND IF(COALESCE(st.RECEIVED_DATE,st.EXPECTED_DELIVERY_DATE) < CURDATE() - INTERVAL 6 MONTH, st.SHIPMENT_STATUS_ID!=7 , st.SHIPMENT_STATUS_ID IN (3,4,5,6,9,7))
    ) st  
    LEFT JOIN rm_shipment_trans_batch_info stbi ON st.SHIPMENT_TRANS_ID = stbi.SHIPMENT_TRANS_ID
    LEFT JOIN rm_program_planning_unit ppu ON st.PLANNING_UNIT_ID=ppu.PLANNING_UNIT_ID AND ppu.PROGRAM_ID=@programId
    LEFT JOIN rm_batch_info bi ON stbi.BATCH_ID=bi.BATCH_ID
    WHERE (@planningUnitActive = FALSE OR ppu.ACTIVE) 
    ORDER BY st.PLANNING_UNIT_ID, COALESCE(st.RECEIVED_DATE, st.EXPECTED_DELIVERY_DATE), bi.EXPIRY_DATE, bi.BATCH_ID; 
END$$

DELIMITER ;



-- SELECT s1.REALM_COUNTRY_ID, s1.COUNTRY, s1.PLANNING_UNIT_ID, s1.PU, CONCAT(s1.COUNTRY_CODE, '-',s1.PROGRAM_ID,'-',s1.SKU_CODE) SKU_CODE, s1.UNIT_ID, s1.CNT, s1.RCPU_ID FROM (
-- SELECT st.SHIPMENT_ID, st.SHIPMENT_TRANS_ID, c.LABEL_EN `COUNTRY`, c.COUNTRY_CODE, p.REALM_COUNTRY_ID, p.PROGRAM_ID, pu.LABEL_EN `PU`, st.PLANNING_UNIT_ID, papu.SKU_CODE, pu.UNIT_ID, GROUP_CONCAT(rcpu.REALM_COUNTRY_PLANNING_UNIT_ID) RCPU_ID, COUNT(rcpu.REALM_COUNTRY_PLANNING_UNIT_ID) CNT
-- FROM rm_shipment_trans st 
-- LEFT JOIN rm_shipment s ON st.SHIPMENT_ID=s.SHIPMENT_ID 
-- LEFT JOIN vw_planning_unit pu ON st.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID
-- LEFT JOIN vw_program p ON s.PROGRAM_ID=p.PROGRAM_ID
-- LEFT JOIN vw_realm_country_planning_unit rcpu on p.REALM_COUNTRY_ID=rcpu.REALM_COUNTRY_ID AND rcpu.PLANNING_UNIT_ID=st.PLANNING_UNIT_ID AND rcpu.MULTIPLIER=1 AND rcpu.ACTIVE
-- LEFT JOIN rm_realm_country rc ON p.REALM_COUNTRY_ID=rc.REALM_COUNTRY_ID
-- LEFT JOIN vw_country c ON rc.COUNTRY_ID=c.COUNTRY_ID
-- LEFT JOIN rm_procurement_agent_planning_unit papu ON papu.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID AND papu.PROCUREMENT_AGENT_ID=1
-- WHERE p.PROGRAM_ID>=1000
-- group by st.SHIPMENT_TRANS_ID
-- HAVING COUNT(rcpu.REALM_COUNTRY_PLANNING_UNIT_ID)!=1) s1 GROUP BY s1.REALM_COUNTRY_ID, s1.PLANNING_UNIT_ID ORDER BY CNT, SKU_CODE, RCPU_ID;

-- Libre office function to determine what to do
-- =IF(AND(ISBLANK(E2),ISBLANK(H2)), "Activate", IF(AND(ISBLANK(E2),G2=0), "Create with SKU", IF(G2=0, "Create", IF(G2>=2,"Deactivate","Some other problem"))))

-- Create	insert into ap_label VALUES (null, '', null, null, null, 1, now(), 1, now(), 32);	SELECT LAST_INSERT_ID() into @labelId;	INSERT INTO rm_realm_country_planning_unit VALUES (null, , , @labelId, '', , 1, null, 1, 1, now(), 1, now());
-- Activate	UPDATE rm_realm_country_planning_unit rcpu SET ACTIVE=1, LAST_MODIFIED_DATE=now(), LAST_MODIFIED_BY=1 WHERE rcpu.REALM_COUNTRY_ID= AND rcpu.PLANNING_UNIT_ID=;		
-- Deactivate	UPDATE rm_realm_country_planning_unit rcpu SET rcpu.ACTIVE=0, rcpu.LAST_MODIFIED_BY=1, rcpu.LAST_MODIFIED_DATE=now() WHERE rcpu.REALM_COUNTRY_PLANNING_UNIT_ID=;	UPDATE rm_consumption_trans ct SET ct.REALM_COUNTRY_PLANNING_UNIT_ID= WHERE ct.REALM_COUNTRY_PLANNING_UNIT_ID=;	UPDATE rm_inventory_trans ct SET ct.REALM_COUNTRY_PLANNING_UNIT_ID= WHERE ct.REALM_COUNTRY_PLANNING_UNIT_ID=;

-- If you need to delete the created ARU's and start again
-- DELETE rcpu.* FROM rm_realm_country_planning_unit rcpu where rcpu.CREATED_DATE>='2022-08-24';
-- DELETE l.* FROM ap_label l WHERE l.CREATED_DATE>'2022-08-24' and l.SOURCE_ID=32;


-- If both of these queries return 0 records then you can run the update
-- SELECT st.SHIPMENT_TRANS_ID, st.PLANNING_UNIT_ID, rcpu.REALM_COUNTRY_PLANNING_UNIT_ID FROM rm_shipment_trans st LEFT JOIN rm_shipment s ON st.SHIPMENT_ID=s.SHIPMENT_ID LEFT JOIN rm_program p ON s.PROGRAM_ID=p.PROGRAM_ID LEFT JOIN rm_realm_country_planning_unit rcpu ON p.REALM_COUNTRY_ID=rcpu.REALM_COUNTRY_ID AND st.PLANNING_UNIT_ID=rcpu.PLANNING_UNIT_ID AND rcpu.ACTIVE and rcpu.MULTIPLIER=1 group by st.SHIPMENT_TRANS_ID HAVING COUNT(*)>1;
-- SELECT st.SHIPMENT_TRANS_ID, st.PLANNING_UNIT_ID, rcpu.REALM_COUNTRY_PLANNING_UNIT_ID FROM rm_shipment_trans st LEFT JOIN rm_shipment s ON st.SHIPMENT_ID=s.SHIPMENT_ID LEFT JOIN rm_program p ON s.PROGRAM_ID=p.PROGRAM_ID LEFT JOIN rm_realm_country_planning_unit rcpu ON p.REALM_COUNTRY_ID=rcpu.REALM_COUNTRY_ID AND st.PLANNING_UNIT_ID=rcpu.PLANNING_UNIT_ID AND rcpu.ACTIVE and rcpu.MULTIPLIER=1 WHERE rcpu.REALM_COUNTRY_PLANNING_UNIT_ID IS NULL;
UPDATE rm_shipment_trans st LEFT JOIN rm_shipment s ON st.SHIPMENT_ID=s.SHIPMENT_ID LEFT JOIN rm_program p ON s.PROGRAM_ID=p.PROGRAM_ID LEFT JOIN rm_realm_country_planning_unit rcpu ON p.REALM_COUNTRY_ID=rcpu.REALM_COUNTRY_ID AND st.PLANNING_UNIT_ID=rcpu.PLANNING_UNIT_ID AND rcpu.ACTIVE and rcpu.MULTIPLIER=1 SET st.REALM_COUNTRY_PLANNING_UNIT_ID=rcpu.REALM_COUNTRY_PLANNING_UNIT_ID, st.SHIPMENT_RCPU_QTY=st.SHIPMENT_QTY;

USE `fasp`;
DROP procedure IF EXISTS `getShipmentLinkingData`;

USE `fasp`;
DROP procedure IF EXISTS `fasp`.`getShipmentLinkingData`;
;

DELIMITER $$
USE `fasp`$$
CREATE DEFINER=`faspUser`@`%` PROCEDURE `getShipmentLinkingData`(PROGRAM_ID INT(10), VERSION_ID INT (10))
BEGIN
    SET @programId = PROGRAM_ID;
    SET @versionId = VERSION_ID;
    IF @versionId = -1 THEN
        SELECT MAX(pv.VERSION_ID) INTO @versionId FROM rm_program_version pv WHERE pv.PROGRAM_ID=@programId;
    END IF;
    
    SELECT 
		sl.SHIPMENT_LINKING_ID, sl.PROGRAM_ID, slt.VERSION_ID,
        pa.PROCUREMENT_AGENT_ID, pa.LABEL_ID `PA_LABEL_ID`, pa.LABEL_EN `PA_LABEL_EN`, pa.LABEL_FR `PA_LABEL_FR`, pa.LABEL_SP `PA_LABEL_SP`, pa.LABEL_PR `PA_LABEL_PR`, pa.PROCUREMENT_AGENT_CODE,
        sl.PARENT_SHIPMENT_ID, sl.CHILD_SHIPMENT_ID, st.PLANNING_UNIT_ID QAT_PLANNING_UNIT_ID,
        pu.PLANNING_UNIT_ID `ERP_PLANNING_UNIT_ID`, pu.LABEL_ID `PU_LABEL_ID`, pu.LABEL_EN `PU_LABEL_EN`, pu.LABEL_FR `PU_LABEL_FR`, pu.LABEL_SP `PU_LABEL_SP`, pu.LABEL_PR `PU_LABEL_PR`,
        sl.RO_NO, sl.RO_PRIME_LINE_NO, sl.ORDER_NO, sl.PRIME_LINE_NO, sl.KN_SHIPMENT_NO, slt.CONVERSION_FACTOR,
        slt.ACTIVE, sl.CREATED_DATE, cb.USER_ID `CB_USER_ID`, cb.USERNAME `CB_USERNAME`, slt.LAST_MODIFIED_DATE, lmb.USER_ID `LMB_USER_ID`, lmb.USERNAME `LMB_USERNAME`,coalesce(es.STATUS,eo.STATUS) AS `ERP_SHIPMENT_STATUS` 
    FROM (
	SELECT sl.SHIPMENT_LINKING_ID, MAX(slt.VERSION_ID) MAX_VERSION_ID FROM rm_shipment_linking sl LEFT JOIN rm_shipment_linking_trans slt ON sl.SHIPMENT_LINKING_ID=slt.SHIPMENT_LINKING_ID WHERE (@versionId=-1 OR slt.VERSION_ID<=@versionId) AND sl.PROGRAM_ID=@programId GROUP BY slt.SHIPMENT_LINKING_ID
    ) ts 
    LEFT JOIN rm_shipment_linking sl ON ts.SHIPMENT_LINKING_ID=sl.SHIPMENT_LINKING_ID
    LEFT JOIN rm_shipment_linking_trans slt ON ts.SHIPMENT_LINKING_ID=slt.SHIPMENT_LINKING_ID AND ts.MAX_VERSION_ID=slt.VERSION_ID
    LEFT JOIN vw_procurement_agent pa on sl.PROCUREMENT_AGENT_ID=pa.PROCUREMENT_AGENT_ID 
    LEFT JOIN rm_erp_order_consolidated eo ON sl.RO_NO=eo.RO_NO AND sl.RO_PRIME_LINE_NO=eo.RO_PRIME_LINE_NO AND eo.ACTIVE
    LEFT JOIN rm_erp_shipment_consolidated es ON es.ORDER_NO=sl.ORDER_NO AND es.PRIME_LINE_NO=sl.PRIME_LINE_NO AND es.KN_SHIPMENT_NO=sl.KN_SHIPMENT_NO AND es.ACTIVE
    LEFT JOIN rm_procurement_agent_planning_unit papu ON papu.PROCUREMENT_AGENT_ID=sl.PROCUREMENT_AGENT_ID AND LEFT(papu.SKU_CODE,12)=eo.PLANNING_UNIT_SKU_CODE
    LEFT JOIN vw_planning_unit pu ON papu.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID 
    LEFT JOIN rm_shipment s ON sl.PARENT_SHIPMENT_ID=s.SHIPMENT_ID 
    LEFT JOIN rm_shipment_trans st ON s.SHIPMENT_ID=st.SHIPMENT_ID AND s.MAX_VERSION_ID=st.VERSION_ID 
    LEFT JOIN us_user cb ON sl.CREATED_BY=cb.USER_ID 
    LEFT JOIN us_user lmb ON slt.LAST_MODIFIED_BY=lmb.USER_ID
    GROUP BY sl.SHIPMENT_LINKING_ID;
    
END$$

DELIMITER ;
;

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.shipment.shipmentQtyARU','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Order Quantity (ARU)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Quantité de commande (ARU)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Cantidad de pedido (ARU)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Quantidade do pedido (ARU)');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.shipment.shipmentQtyPU','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Order Quantity (PU)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Quantité de commande (PU)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Cantidad de pedido (PU)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Quantidade do pedido (PU)');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.manualTagging.aruQty','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'ARU Qty');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Qté ARU');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Cantidad de ARU');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Quantidade ARU');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.manualTagging.conversionARUToPU','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Conversion (ARU to PU)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Conversion (ARU en PU)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Conversión (ARU a PU)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Conversão (ARU para PU)');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.manualTagging.qtyPU','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Qty (PU)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Qté (PU)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Cant. (PU)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Quantidade (PU)');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.manualTagging.aru','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'ARU');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'ARU');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'ARU');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'ARU');-- pr

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.manualTagging.erpProduct','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'ERP Product');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Produit ERP');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Producto ERP');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Produto ERP');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.manualTagging.erpQty','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'ERP Qty');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Qté ERP');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Cantidad ERP');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Qtde ERP');-- pr

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.manualTagging.conversionERPToPU','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Conversion Factor (ERP to QAT PU)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Facteur de conversion (ERP à QAT PU)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Factor de conversión (ERP a QAT PU)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Fator de conversão (ERP para QAT PU)');-- pr