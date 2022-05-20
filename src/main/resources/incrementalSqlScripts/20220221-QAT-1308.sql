
CREATE TABLE `log` (
  `LOG_ID` int unsigned NOT NULL AUTO_INCREMENT,
  `UPDATED_DATE` datetime NOT NULL,
  `DESC` varchar(200) NOT NULL,
  PRIMARY KEY (`LOG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `rm_erp_order_consolidated` (
  `ERP_ORDER_ID` int unsigned NOT NULL,
  `RO_NO` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `RO_PRIME_LINE_NO` int unsigned NOT NULL,
  `ORDER_NO` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PRIME_LINE_NO` int DEFAULT NULL,
  `ORDER_TYPE` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `PARENT_RO` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PARENT_CREATED_DATE` datetime DEFAULT NULL,
  `PLANNING_UNIT_SKU_CODE` varchar(13) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROCUREMENT_UNIT_SKU_CODE` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `QTY` bigint unsigned NOT NULL,
  `ORDERD_DATE` date DEFAULT NULL,
  `CURRENT_ESTIMATED_DELIVERY_DATE` date DEFAULT NULL,
  `REQ_DELIVERY_DATE` date DEFAULT NULL,
  `AGREED_DELIVERY_DATE` date DEFAULT NULL,
  `SUPPLIER_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PRICE` decimal(14,4) NOT NULL,
  `SHIPPING_COST` decimal(14,4) DEFAULT NULL,
  `SHIP_BY` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `RECPIENT_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `RECPIENT_COUNTRY` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `STATUS` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CHANGE_CODE` int unsigned NOT NULL,
  `PROCUREMENT_AGENT_ID` int unsigned NOT NULL,
  `LAST_MODIFIED_DATE` datetime NOT NULL,
  `VERSION_ID` int DEFAULT NULL,
  `PROGRAM_ID` int unsigned DEFAULT NULL,
  `SHIPMENT_ID` int unsigned DEFAULT NULL,
  `FILE_NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ACTIVE` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`ERP_ORDER_ID`),
  UNIQUE KEY `unq_erp_order_consolidated_orderNo_primeLineNo` (`ORDER_NO`,`PRIME_LINE_NO`),
  KEY `idx_erp_order_consolidated_orderNo` (`ORDER_NO`),
  KEY `idx_erp_order_consolidated_primeLineNo` (`PRIME_LINE_NO`),
  KEY `idx_erp_order_consolidated_planningUnitSkuCode` (`PLANNING_UNIT_SKU_CODE`),
  KEY `idx_erp_order_consolidated_procurementUnitSkuCode` (`PROCUREMENT_UNIT_SKU_CODE`),
  KEY `idx_erp_order_consolidated_recipientCountry` (`RECPIENT_COUNTRY`),
  KEY `idx_erp_order_consolidated_status` (`STATUS`),
  KEY `fk_erp_order_consolidated_procurementAgentId_idx` (`PROCUREMENT_AGENT_ID`),
  KEY `fk_rm_erp_order_consolidated_programId_idx` (`PROGRAM_ID`),
  KEY `fk_rm_erp_order_consolidated_shipmentId_idx` (`SHIPMENT_ID`),
  KEY `idx_erp_order_consolidated_fileName` (`FILE_NAME`),
  KEY `idx_erp_order_consolidated_active` (`ACTIVE`),
  CONSTRAINT `fk_erp_order_consolidated_procurementAgentId` FOREIGN KEY (`PROCUREMENT_AGENT_ID`) REFERENCES `rm_procurement_agent` (`PROCUREMENT_AGENT_ID`),
  CONSTRAINT `fk_rm_erp_order_consolidated_programId` FOREIGN KEY (`PROGRAM_ID`) REFERENCES `rm_program` (`PROGRAM_ID`),
  CONSTRAINT `fk_rm_erp_order_consolidated_shipmentId` FOREIGN KEY (`SHIPMENT_ID`) REFERENCES `rm_shipment` (`SHIPMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

CREATE TABLE `rm_erp_shipment_consolidated` (
  `ERP_SHIPMENT_ID` int unsigned NOT NULL AUTO_INCREMENT,
  `ERP_ORDER_ID` int unsigned DEFAULT NULL,
  `KN_SHIPMENT_NO` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ORDER_NO` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PRIME_LINE_NO` int NOT NULL,
  `BATCH_NO` varchar(25) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXPIRY_DATE` date DEFAULT NULL,
  `PROCUREMENT_UNIT_SKU_CODE` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SHIPPED_QTY` int DEFAULT NULL,
  `DELIVERED_QTY` bigint DEFAULT NULL,
  `ACTUAL_SHIPMENT_DATE` date DEFAULT NULL,
  `ACTUAL_DELIVERY_DATE` date DEFAULT NULL,
  `ARRIVAL_AT_DESTINATION_DATE` date DEFAULT NULL,
  `STATUS` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CHANGE_CODE` int unsigned NOT NULL,
  `LAST_MODIFIED_DATE` datetime NOT NULL,
  `FILE_NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ACTIVE` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`ERP_SHIPMENT_ID`),
  UNIQUE KEY `index9` (`KN_SHIPMENT_NO`,`ORDER_NO`,`PRIME_LINE_NO`,`BATCH_NO`),
  KEY `idx_erp_shipment_consolidated_active` (`ACTIVE`),
  KEY `idx_erp_shipment_consolidated_orderNo` (`ORDER_NO`),
  KEY `idx_erp_shipment_consolidated_primeLineNo` (`PRIME_LINE_NO`),
  KEY `idx_erp_shipment_consolidated_knShipmentNo` (`KN_SHIPMENT_NO`),
  KEY `idx_erp_shipment_consolidated_batchNo` (`BATCH_NO`),
  KEY `idx_erp_shipment_consolidated_erpOrderId_idx` (`ERP_ORDER_ID`),
  KEY `idx_erp_shipment_consolidated_fileName` (`FILE_NAME`),
  CONSTRAINT `fk_rm_erp_shipment_consolidated_erpOrderId` FOREIGN KEY (`ERP_ORDER_ID`) REFERENCES `rm_erp_order_consolidated` (`ERP_ORDER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;


USE `fasp`;
DROP procedure IF EXISTS `buildErpOrder`;

DELIMITER $$
USE `fasp`$$
CREATE DEFINER=`faspUser`@`%` PROCEDURE `buildErpOrder`(VAR_DT VARCHAR(10))
BEGIN
	DECLARE VAR_FINISHED INTEGER DEFAULT 0;
	DECLARE VAR_FILE_NAME varchar(45) DEFAULT "";

	
	DEClARE curErpOrder CURSOR FOR 
		SELECT DISTINCT eo.FILE_NAME FROM rm_erp_order eo WHERE eo.FILE_NAME!='QATDefault' AND eo.FILE_NAME>=CONCAT('order_data_',VAR_DT,'.xml') group by eo.FILE_NAME;

	-- declare NOT FOUND handler
	DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET VAR_FINISHED = 1;
	OPEN curErpOrder;
    INSERT INTO log VALUES (null, now(), "Starting buildErpOrder");
    getFileName: LOOP
		FETCH curErpOrder INTO VAR_FILE_NAME;
		IF VAR_FINISHED = 1 THEN 
			LEAVE getFileName;
		END IF;
        INSERT INTO log VALUES (null, now(), CONCAT("Starting loop for ",VAR_FILE_NAME));
		-- Do work
        -- First complete all Inserts
        INSERT IGNORE INTO rm_erp_order_consolidated 
			(`ERP_ORDER_ID`, `RO_NO`, `RO_PRIME_LINE_NO`, `ORDER_NO`, `PRIME_LINE_NO`, 
            `ORDER_TYPE`, `CREATED_DATE`, `PARENT_RO`, `PARENT_CREATED_DATE`, `PLANNING_UNIT_SKU_CODE`,
            `PROCUREMENT_UNIT_SKU_CODE`, `QTY`, `ORDERD_DATE`, `CURRENT_ESTIMATED_DELIVERY_DATE`, `REQ_DELIVERY_DATE`,
            `AGREED_DELIVERY_DATE`, `SUPPLIER_NAME`, `PRICE`, `SHIPPING_COST`, `SHIP_BY`, 
            `RECPIENT_NAME`, `RECPIENT_COUNTRY`, `STATUS`, `CHANGE_CODE`, `PROCUREMENT_AGENT_ID`, 
            `LAST_MODIFIED_DATE`, `FILE_NAME`, `ACTIVE`)
			SELECT 
				`ERP_ORDER_ID`, `RO_NO`, `RO_PRIME_LINE_NO`, `ORDER_NO`, `PRIME_LINE_NO`, 
				`ORDER_TYPE`, `CREATED_DATE`, `PARENT_RO`, `PARENT_CREATED_DATE`, `PLANNING_UNIT_SKU_CODE`,
				`PROCUREMENT_UNIT_SKU_CODE`, `QTY`, `ORDERD_DATE`, `CURRENT_ESTIMATED_DELIVERY_DATE`, `REQ_DELIVERY_DATE`,
				`AGREED_DELIVERY_DATE`, `SUPPLIER_NAME`, `PRICE`, `SHIPPING_COST`, `SHIP_BY`, 
				`RECPIENT_NAME`, `RECPIENT_COUNTRY`, `STATUS`, `CHANGE_CODE`, `PROCUREMENT_AGENT_ID`, 
				`LAST_MODIFIED_DATE`, `FILE_NAME`, 1 
			FROM rm_erp_order WHERE FILE_NAME=VAR_FILE_NAME;
		INSERT INTO log VALUES (null, now(), CONCAT(ROW_COUNT(), "-Inserts done"));
        -- Now Update all the Updates
        UPDATE 
			rm_erp_order_consolidated eoc 
        LEFT JOIN (SELECT eo2.* FROM (SELECT MAX(ERP_ORDER_ID) `ERP_ORDER_ID` FROM rm_erp_order WHERE FILE_NAME=VAR_FILE_NAME group by ORDER_NO, PRIME_LINE_NO) AS eo1 LEFT JOIN rm_erp_order eo2 ON eo1.ERP_ORDER_ID=eo2.ERP_ORDER_ID) AS eo ON eo.ORDER_NO=eoc.ORDER_NO and eo.PRIME_LINE_NO=eoc.PRIME_LINE_NO
        SET 
			eoc.`RO_NO`=eo.`RO_NO`, 
            eoc.`RO_PRIME_LINE_NO`=eo.`RO_PRIME_LINE_NO`, 
            eoc.`ORDER_NO`=eo.`ORDER_NO`, 
            eoc.`PRIME_LINE_NO`=eo.`PRIME_LINE_NO`, 
			eoc.`ORDER_TYPE`=eo.`ORDER_TYPE`, 
            eoc.`CREATED_DATE`=eo.`CREATED_DATE`, 
            eoc.`PARENT_RO`=eo.`PARENT_RO`, 
            eoc.`PARENT_CREATED_DATE`=eo.`PARENT_CREATED_DATE`, 
            eoc.`PLANNING_UNIT_SKU_CODE`=eo.`PLANNING_UNIT_SKU_CODE`,
            eoc.`PROCUREMENT_UNIT_SKU_CODE`=eo.`PROCUREMENT_UNIT_SKU_CODE`, 
            eoc.`QTY`=eo.`QTY`, 
            eoc.`ORDERD_DATE`=eo.`ORDERD_DATE`, 
            eoc.`CURRENT_ESTIMATED_DELIVERY_DATE`=eo.`CURRENT_ESTIMATED_DELIVERY_DATE`, 
            eoc.`REQ_DELIVERY_DATE`=eo.`REQ_DELIVERY_DATE`,
            eoc.`AGREED_DELIVERY_DATE`=eo.`AGREED_DELIVERY_DATE`, 
            eoc.`SUPPLIER_NAME`=eo.`SUPPLIER_NAME`, 
            eoc.`PRICE`=eo.`PRICE`, 
            eoc.`SHIPPING_COST`=eo.`SHIPPING_COST`, 
            eoc.`SHIP_BY`=eo.`SHIP_BY`, 
            eoc.`RECPIENT_NAME`=eo.`RECPIENT_NAME`, 
            eoc.`RECPIENT_COUNTRY`=eo.`RECPIENT_COUNTRY`, 
            eoc.`STATUS`=eo.`STATUS`, 
            eoc.`CHANGE_CODE`=eo.`CHANGE_CODE`, 
            eoc.`PROCUREMENT_AGENT_ID`=eo.`PROCUREMENT_AGENT_ID`, 
            eoc.`LAST_MODIFIED_DATE`=eo.`LAST_MODIFIED_DATE`, 
            eoc.`FILE_NAME`=eo.`FILE_NAME`,
            eoc.ACTIVE=IF(eo.`CHANGE_CODE`=2,0,1)
            WHERE eo.ERP_ORDER_ID IS NOT NULL;
        INSERT INTO log VALUES (null, now(), CONCAT(ROW_COUNT(), "-Updates and Deletes done"));
	END LOOP getFileName;
END$$

DELIMITER ;



USE `fasp`;
DROP procedure IF EXISTS `buildErpShipment`;

DELIMITER $$
USE `fasp`$$
CREATE DEFINER=`faspUser`@`%` PROCEDURE `buildErpShipment`(VAR_DT VARCHAR(10))
BEGIN
	DECLARE VAR_FINISHED INTEGER DEFAULT 0;
	DECLARE VAR_FILE_NAME varchar(45) DEFAULT "";

	DEClARE curErpShipment CURSOR FOR 
		SELECT DISTINCT eo.FILE_NAME FROM rm_erp_shipment eo WHERE eo.FILE_NAME!='QATDefault' AND eo.FILE_NAME>=CONCAT('shipment_data_',VAR_DT,'.xml') group by eo.FILE_NAME;

	-- declare NOT FOUND handler
	DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET VAR_FINISHED = 1;
	OPEN curErpShipment;
    INSERT INTO log VALUES (null, now(), "Starting buildErpShipment");
    getFileName: LOOP
		FETCH curErpShipment INTO VAR_FILE_NAME;
		IF VAR_FINISHED = 1 THEN 
			LEAVE getFileName;
		END IF;
        INSERT INTO log VALUES (null, now(), CONCAT("Starting loop for ",VAR_FILE_NAME));
		-- Do work
        -- First complete all Inserts
        INSERT IGNORE INTO rm_erp_shipment_consolidated 
			(`ERP_SHIPMENT_ID`, `ERP_ORDER_ID`, `KN_SHIPMENT_NO`, `ORDER_NO`, `PRIME_LINE_NO`, 
            `BATCH_NO`, `EXPIRY_DATE`, `PROCUREMENT_UNIT_SKU_CODE`, `SHIPPED_QTY`, `DELIVERED_QTY`, 
            `ACTUAL_SHIPMENT_DATE`, `ACTUAL_DELIVERY_DATE`, `ARRIVAL_AT_DESTINATION_DATE`, `STATUS`, `CHANGE_CODE`, 
            `LAST_MODIFIED_DATE`, `FILE_NAME`, `ACTIVE`)
			SELECT 
				es.`ERP_SHIPMENT_ID`, eoc.`ERP_ORDER_ID`, es.`KN_SHIPMENT_NO`, es.`ORDER_NO`, es.`PRIME_LINE_NO`, 
				es.`BATCH_NO`, es.`EXPIRY_DATE`, es.`PROCUREMENT_UNIT_SKU_CODE`, es.`SHIPPED_QTY`, es.`DELIVERED_QTY`, 
				es.`ACTUAL_SHIPMENT_DATE`, es.`ACTUAL_DELIVERY_DATE`, es.`ARRIVAL_AT_DESTINATION_DATE`, es.`STATUS`, es.`CHANGE_CODE`, 
				es.`LAST_MODIFIED_DATE`, es.`FILE_NAME`, 1
			FROM rm_erp_shipment es LEFT JOIN rm_erp_order_consolidated eoc ON es.ORDER_NO=eoc.ORDER_NO and es.PRIME_LINE_NO=eoc.PRIME_LINE_NO WHERE es.FILE_NAME=VAR_FILE_NAME;
		INSERT INTO log VALUES (null, now(), CONCAT(ROW_COUNT(), "-Inserts done"));
        -- Now Update all the Updates
        UPDATE 
			rm_erp_shipment_consolidated eoc 
        LEFT JOIN (SELECT eo2.* FROM (SELECT MAX(ERP_SHIPMENT_ID) `ERP_SHIPMENT_ID` FROM rm_erp_shipment WHERE FILE_NAME=VAR_FILE_NAME group by ORDER_NO, PRIME_LINE_NO, KN_SHIPMENT_NO, BATCH_NO) AS eo1 LEFT JOIN rm_erp_shipment eo2 ON eo1.ERP_SHIPMENT_ID=eo2.ERP_SHIPMENT_ID) AS eo ON eo.ORDER_NO=eoc.ORDER_NO and eo.PRIME_LINE_NO=eoc.PRIME_LINE_NO and eo.KN_SHIPMENT_NO=eoc.KN_SHIPMENT_NO and eo.BATCH_NO=eoc.BATCH_NO
        SET 
			eoc.`EXPIRY_DATE`=eo.`EXPIRY_DATE`,
            eoc.`PROCUREMENT_UNIT_SKU_CODE`=eo.`PROCUREMENT_UNIT_SKU_CODE`, 
            eoc.`SHIPPED_QTY`=eo.`SHIPPED_QTY`, 
            eoc.`DELIVERED_QTY`=eo.`DELIVERED_QTY`,
            eoc.`ACTUAL_SHIPMENT_DATE`=eo.`ACTUAL_SHIPMENT_DATE`, 
            eoc.`ACTUAL_DELIVERY_DATE`=eo.`ACTUAL_DELIVERY_DATE`, 
            eoc.`ARRIVAL_AT_DESTINATION_DATE`=eo.`ARRIVAL_AT_DESTINATION_DATE`, 
            eoc.`STATUS`=eo.`STATUS`, 
            eoc.`CHANGE_CODE`=eo.`CHANGE_CODE`, 
            eoc.`LAST_MODIFIED_DATE`=eo.`LAST_MODIFIED_DATE`, 
            eoc.`FILE_NAME`=eo.`FILE_NAME`, 
            eoc.`ACTIVE`=IF(eo.`CHANGE_CODE`=2,0,1)
            WHERE eo.ERP_SHIPMENT_ID IS NOT NULL;
        INSERT INTO log VALUES (null, now(), CONCAT(ROW_COUNT(), "-Updates and Deletes done"));
	END LOOP getFileName;
END$$

DELIMITER ;



CREATE TABLE `fasp`.`rm_erp_shipment_linking` (
  `ERP_SHIPMENT_LINKING_ID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `PROCUREMENT_AGENT_ID` INT UNSIGNED NOT NULL,
  `PARENT_SHIPMENT_ID` INT UNSIGNED NOT NULL,
  `RO_NO` VARCHAR(45) NOT NULL,
  `RO_PRIME_LINE_NO` VARCHAR(45) NOT NULL,
  `ACTIVE` TINYINT UNSIGNED NOT NULL,
  `CREATED_DATE` DATETIME NOT NULL,
  `CREATED_BY` INT UNSIGNED NOT NULL,
  `LAST_MODIFIED_DATE` DATETIME NOT NULL,
  `LAST_MODIFIED_BY` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`ERP_SHIPMENT_LINKING_ID`));

ALTER TABLE `fasp`.`rm_erp_shipment_linking` 
ADD INDEX `fk_rm_erp_shipment_linking_procurementAgentId_idx` (`PROCUREMENT_AGENT_ID` ASC),
ADD INDEX `fk_rm_erp_shipment_linking_shipmentId_idx` (`PARENT_SHIPMENT_ID` ASC),
ADD INDEX `fk_rm_erp_shipment_linking_createdBy_idx` (`CREATED_BY` ASC),
ADD INDEX `fk_rm_erp_shipment_linking_lastModifiedBy_idx` (`LAST_MODIFIED_BY` ASC);
;
ALTER TABLE `fasp`.`rm_erp_shipment_linking` 
ADD CONSTRAINT `fk_rm_erp_shipment_linking_procurementAgentId`
  FOREIGN KEY (`PROCUREMENT_AGENT_ID`)
  REFERENCES `fasp`.`rm_procurement_agent` (`PROCUREMENT_AGENT_ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_rm_erp_shipment_linking_shipmentId`
  FOREIGN KEY (`PARENT_SHIPMENT_ID`)
  REFERENCES `fasp`.`rm_shipment` (`SHIPMENT_ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_rm_erp_shipment_linking_createdBy`
  FOREIGN KEY (`CREATED_BY`)
  REFERENCES `fasp`.`us_user` (`USER_ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_rm_erp_shipment_linking_lastModifiedBy`
  FOREIGN KEY (`LAST_MODIFIED_BY`)
  REFERENCES `fasp`.`us_user` (`USER_ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;




USE `fasp`;
DROP procedure IF EXISTS `getShipmentListForManualLinking`;

USE `fasp`;
DROP procedure IF EXISTS `fasp`.`getShipmentListForManualLinking`;
;

DELIMITER $$
USE `fasp`$$
CREATE DEFINER=`faspUser`@`%` PROCEDURE `getShipmentListForManualLinking`(PROGRAM_ID INT(10), PLANNING_UNIT_ID TEXT, VERSION_ID INT (10))
BEGIN
    SET @programId = PROGRAM_ID;
    SET @planningUnitIds = PLANNING_UNIT_ID;
    SET @procurementAgentId = 1;
    SET @versionId = VERSION_ID;
    IF @versionId = -1 THEN
        SELECT MAX(pv.VERSION_ID) INTO @versionId FROM rm_program_version pv WHERE pv.PROGRAM_ID=@programId;
    END IF;
   
SELECT
        st.SHIPMENT_ID, st.SHIPMENT_TRANS_ID, st.SHIPMENT_QTY, st.EXPECTED_DELIVERY_DATE, st.PRODUCT_COST, st.SKU_CODE,
        st.PROCUREMENT_AGENT_ID, st.PROCUREMENT_AGENT_CODE, st.`COLOR_HTML_CODE`, st.`PROCUREMENT_AGENT_LABEL_ID`, st.`PROCUREMENT_AGENT_LABEL_EN`, st.`PROCUREMENT_AGENT_LABEL_FR`, st.`PROCUREMENT_AGENT_LABEL_SP`, st.`PROCUREMENT_AGENT_LABEL_PR`,
        st.FUNDING_SOURCE_ID, st.FUNDING_SOURCE_CODE, st.`FUNDING_SOURCE_LABEL_ID`, st.`FUNDING_SOURCE_LABEL_EN`, st.`FUNDING_SOURCE_LABEL_FR`, st.`FUNDING_SOURCE_LABEL_SP`, st.`FUNDING_SOURCE_LABEL_PR`,
        st.BUDGET_ID, st.BUDGET_CODE, st.`BUDGET_LABEL_ID`, st.`BUDGET_LABEL_EN`, st.`BUDGET_LABEL_FR`, st.`BUDGET_LABEL_SP`, st.`BUDGET_LABEL_PR`,
        st.SHIPMENT_STATUS_ID, st.`SHIPMENT_STATUS_LABEL_ID`, st.`SHIPMENT_STATUS_LABEL_EN`, st.`SHIPMENT_STATUS_LABEL_FR`, st.`SHIPMENT_STATUS_LABEL_SP`, st.`SHIPMENT_STATUS_LABEL_PR`,
        st.PLANNING_UNIT_ID, st.`PLANNING_UNIT_LABEL_ID`, st.`PLANNING_UNIT_LABEL_EN`, st.`PLANNING_UNIT_LABEL_FR`, st.`PLANNING_UNIT_LABEL_SP`, st.`PLANNING_UNIT_LABEL_PR`,
        st.ORDER_NO, st.PRIME_LINE_NO,st.`NOTES`
FROM (
        SELECT
            s.SHIPMENT_ID, COALESCE(st.RECEIVED_DATE, st.EXPECTED_DELIVERY_DATE) AS EXPECTED_DELIVERY_DATE,st.SHIPMENT_QTY, st.RATE, st.PRODUCT_COST, st.FREIGHT_COST, st.ACCOUNT_FLAG, st.SHIPMENT_TRANS_ID, papu.SKU_CODE,
            pa.`PROCUREMENT_AGENT_ID`, pa.`PROCUREMENT_AGENT_CODE`, pa.`COLOR_HTML_CODE`, pa.`LABEL_ID` `PROCUREMENT_AGENT_LABEL_ID`, pa.`LABEL_EN` `PROCUREMENT_AGENT_LABEL_EN`, pa.LABEL_FR `PROCUREMENT_AGENT_LABEL_FR`, pa.LABEL_SP `PROCUREMENT_AGENT_LABEL_SP`, pa.LABEL_PR `PROCUREMENT_AGENT_LABEL_PR`,
            fs.`FUNDING_SOURCE_ID`, fs.`FUNDING_SOURCE_CODE`, fs.LABEL_ID `FUNDING_SOURCE_LABEL_ID`, fs.LABEL_EN `FUNDING_SOURCE_LABEL_EN`, fs.LABEL_FR `FUNDING_SOURCE_LABEL_FR`, fs.LABEL_SP `FUNDING_SOURCE_LABEL_SP`, fs.LABEL_PR `FUNDING_SOURCE_LABEL_PR`,
            shs.SHIPMENT_STATUS_ID, shs.LABEL_ID `SHIPMENT_STATUS_LABEL_ID`, shs.LABEL_EN `SHIPMENT_STATUS_LABEL_EN`, shs.LABEL_FR `SHIPMENT_STATUS_LABEL_FR`, shs.LABEL_SP `SHIPMENT_STATUS_LABEL_SP`, shs.LABEL_PR `SHIPMENT_STATUS_LABEL_PR`,
            sc.CURRENCY_ID `SHIPMENT_CURRENCY_ID`, sc.`CURRENCY_CODE` `SHIPMENT_CURRENCY_CODE`, s.CONVERSION_RATE_TO_USD `SHIPMENT_CONVERSION_RATE_TO_USD`,
            sc.LABEL_ID `SHIPMENT_CURRENCY_LABEL_ID`, sc.LABEL_EN `SHIPMENT_CURRENCY_LABEL_EN`, sc.LABEL_FR `SHIPMENT_CURRENCY_LABEL_FR`, sc.LABEL_SP `SHIPMENT_CURRENCY_LABEL_SP`, sc.LABEL_PR `SHIPMENT_CURRENCY_LABEL_PR`,
            st.ACTIVE, st.`ORDER_NO`, st.`PRIME_LINE_NO`,
            b.BUDGET_ID, b.BUDGET_CODE, b.LABEL_ID `BUDGET_LABEL_ID`, b.LABEL_EN `BUDGET_LABEL_EN`, b.LABEL_FR `BUDGET_LABEL_FR`, b.LABEL_SP `BUDGET_LABEL_SP`, b.LABEL_PR `BUDGET_LABEL_PR`,
            st.PLANNING_UNIT_ID, pu.LABEL_ID `PLANNING_UNIT_LABEL_ID`, pu.LABEL_EN `PLANNING_UNIT_LABEL_EN`, pu.LABEL_FR `PLANNING_UNIT_LABEL_FR`, pu.LABEL_SP `PLANNING_UNIT_LABEL_SP`, pu.LABEL_PR `PLANNING_UNIT_LABEL_PR`,st.`NOTES`
FROM (
    SELECT st.SHIPMENT_ID, MAX(st.VERSION_ID) MAX_VERSION_ID FROM rm_shipment s LEFT JOIN rm_shipment_trans st ON s.SHIPMENT_ID=st.SHIPMENT_ID WHERE (@versiONId=-1 OR st.VERSION_ID<=@versiONId) AND s.PROGRAM_ID=@programId GROUP BY st.SHIPMENT_ID
) ts
    LEFT JOIN rm_shipment s ON ts.SHIPMENT_ID=s.SHIPMENT_ID
    LEFT JOIN rm_shipment_trans st ON ts.SHIPMENT_ID=st.SHIPMENT_ID AND ts.MAX_VERSION_ID=st.VERSION_ID
    LEFT JOIN vw_planning_unit pu ON st.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID
    LEFT JOIN vw_procurement_agent pa ON st.PROCUREMENT_AGENT_ID=pa.PROCUREMENT_AGENT_ID
    LEFT JOIN vw_funding_source fs ON st.FUNDING_SOURCE_ID=fs.FUNDING_SOURCE_ID
    LEFT JOIN vw_shipment_status shs ON st.SHIPMENT_STATUS_ID=shs.SHIPMENT_STATUS_ID
    LEFT JOIN vw_currency sc ON s.CURRENCY_ID=sc.CURRENCY_ID
    LEFT JOIN vw_budget b ON st.BUDGET_ID=b.BUDGET_ID
    LEFT JOIN rm_manual_tagging mt ON mt.SHIPMENT_ID=ts.SHIPMENT_ID AND mt.ACTIVE
    LEFT JOIN rm_procurement_agent_planning_unit papu ON papu.PROCUREMENT_AGENT_ID=pa.PROCUREMENT_AGENT_ID AND papu.PLANNING_UNIT_ID=st.PLANNING_UNIT_ID
    WHERE st.ERP_FLAG=0 AND st.ACTIVE AND st.ACCOUNT_FLAG AND st.SHIPMENT_STATUS_ID IN (3,4,5,6,9,7) AND (LENGTH(@planningUnitIds)=0 OR FIND_IN_SET(st.PLANNING_UNIT_ID,@planningUnitIds))   AND (mt.SHIPMENT_ID IS NULL OR mt.ACTIVE=0) AND st.PROCUREMENT_AGENT_ID=@procurementAgentId
) st  
WHERE
IF(st.EXPECTED_DELIVERY_DATE < CURDATE() - INTERVAL 6 MONTH, st.SHIPMENT_STATUS_ID!=7 , st.SHIPMENT_STATUS_ID IN (3,4,5,6,9,7))
ORDER BY st.EXPECTED_DELIVERY_DATE aSC;
END$$

DELIMITER ;
;

