CREATE DEFINER=`faspUser`@`localhost` PROCEDURE `getShipmentLinkingNotifications`(PROGRAM_ID INT(10), PLANNING_UNIT_ID TEXT, VERSION_ID INT (10))
BEGIN
    SET @programId = PROGRAM_ID;
    SET @planningUnitIds = PLANNING_UNIT_ID;
    SET @procurementAgentId = 1;
    SET @versionId = VERSION_ID;
    IF @versionId = -1 THEN
        SELECT MAX(pv.VERSION_ID) INTO @versionId FROM rm_program_version pv WHERE pv.PROGRAM_ID=@programId;
    END IF;
   
SELECT n.`NOTIFICATION_ID`,n.ADDRESSED,n.NOTIFICATION_TYPE_ID,lnt.*,st.`SHIPMENT_ID`,st.`ORDER_NO`,st.`PRIME_LINE_NO`,eo.RO_NO,eo.RO_PRIME_LINE_NO,
st.PLANNING_UNIT_ID, pu.LABEL_ID `PLANNING_UNIT_LABEL_ID`, pu.LABEL_EN `PLANNING_UNIT_LABEL_EN`, pu.LABEL_FR `PLANNING_UNIT_LABEL_FR`,
 pu.LABEL_SP `PLANNING_UNIT_LABEL_SP`, pu.LABEL_PR `PLANNING_UNIT_LABEL_PR`,
 papu1.PLANNING_UNIT_ID AS ERP_PLANNING_UNIT_ID, pu1.LABEL_ID `ERP_PLANNING_UNIT_LABEL_ID`, pu1.LABEL_EN `ERP_PLANNING_UNIT_LABEL_EN`, pu1.LABEL_FR `ERP_PLANNING_UNIT_LABEL_FR`,
 pu1.LABEL_SP `ERP_PLANNING_UNIT_LABEL_SP`, pu1.LABEL_PR `ERP_PLANNING_UNIT_LABEL_PR`,st.`EXPECTED_DELIVERY_DATE`,
 ss.`SHIPMENT_STATUS_ID`,ss.`LABEL_EN` AS SHIPMENT_STATUS_LABEL_EN,ss.`LABEL_FR` AS SHIPMENT_STATUS_LABEL_FR,ss.`LABEL_ID`  AS SHIPMENT_STATUS_LABEL_ID,ss.`LABEL_PR` AS SHIPMENT_STATUS_LABEL_PR,ss.`LABEL_SP` AS SHIPMENT_STATUS_LABEL_SP,
 pa.`PROCUREMENT_AGENT_ID`, pa.`PROCUREMENT_AGENT_CODE`, pa.`COLOR_HTML_CODE`, pa.`LABEL_ID` `PROCUREMENT_AGENT_LABEL_ID`, pa.`LABEL_EN` `PROCUREMENT_AGENT_LABEL_EN`, pa.LABEL_FR `PROCUREMENT_AGENT_LABEL_FR`, pa.LABEL_SP `PROCUREMENT_AGENT_LABEL_SP`, pa.LABEL_PR `PROCUREMENT_AGENT_LABEL_PR`,st.`SHIPMENT_QTY`,
 n.NOTES,m.CONVERSION_FACTOR,n.SHIPMENT_ID AS PARENT_SHIPMENT_ID,eo.STATUS
 FROM (
SELECT st.SHIPMENT_ID, MAX(st.VERSION_ID) MAX_VERSION_ID 
FROM rm_shipment s LEFT JOIN rm_shipment_trans st ON s.SHIPMENT_ID=st.SHIPMENT_ID 
WHERE (@versiONId=-1 OR st.VERSION_ID<=@versiONId) AND s.PROGRAM_ID=@programId GROUP BY st.SHIPMENT_ID
)  ts
LEFT JOIN rm_shipment s ON s.`SHIPMENT_ID`=ts.`SHIPMENT_ID`
LEFT JOIN rm_erp_notification n ON n.`SHIPMENT_ID`=s.`PARENT_SHIPMENT_ID`
LEFT JOIN ap_notification_type nt ON nt.`NOTIFICATION_TYPE_ID`=n.`NOTIFICATION_TYPE_ID`
LEFT JOIN ap_label lnt ON lnt.`LABEL_ID`=nt.`LABEL_ID`
LEFT JOIN rm_shipment_trans st ON ts.SHIPMENT_ID=st.SHIPMENT_ID AND ts.MAX_VERSION_ID=st.VERSION_ID
LEFT JOIN vw_planning_unit pu ON st.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID
LEFT JOIN rm_procurement_agent_planning_unit papu ON papu.PROCUREMENT_AGENT_ID=1 AND papu.PLANNING_UNIT_ID=st.PLANNING_UNIT_ID
LEFT JOIN (SELECT e.PLANNING_UNIT_SKU_CODE,e.RO_NO,e.RO_PRIME_LINE_NO,e.ORDER_NO,e.PRIME_LINE_NO ,e.`STATUS`
FROM rm_erp_order e WHERE e.`ERP_ORDER_ID` IN (SELECT MAX(e.`ERP_ORDER_ID`)  AS ERP_ORDER_ID 
FROM rm_erp_order e
GROUP BY e.`RO_NO`,e.`RO_PRIME_LINE_NO`,e.`ORDER_NO`,e.`PRIME_LINE_NO`)) AS eo 
ON eo.ORDER_NO=st.ORDER_NO AND eo.PRIME_LINE_NO=st.PRIME_LINE_NO 
LEFT JOIN rm_procurement_agent_planning_unit papu1 ON papu1.PROCUREMENT_AGENT_ID=1 AND LEFT(papu1.SKU_CODE,12)=eo.PLANNING_UNIT_SKU_CODE
LEFT JOIN vw_planning_unit pu1 ON papu1.PLANNING_UNIT_ID=pu1.PLANNING_UNIT_ID
LEFT JOIN rm_shipment_status_mapping ssm ON ssm.`EXTERNAL_STATUS_STAGE`=eo.STATUS
LEFT JOIN vw_shipment_status ss ON ss.`SHIPMENT_STATUS_ID`=ssm.`SHIPMENT_STATUS_ID`
LEFT JOIN vw_procurement_agent pa ON st.PROCUREMENT_AGENT_ID=pa.PROCUREMENT_AGENT_ID
LEFT JOIN rm_manual_tagging m ON m.ORDER_NO=n.ORDER_NO AND m.PRIME_LINE_NO=n.PRIME_LINE_NO AND m.SHIPMENT_ID=n.SHIPMENT_ID AND m.ACTIVE
WHERE n.`NOTIFICATION_ID` IS NOT NULL AND n.ACTIVE AND (LENGTH(@planningUnitIds)=0 OR FIND_IN_SET(st.PLANNING_UNIT_ID,@planningUnitIds))   
    AND st.PROCUREMENT_AGENT_ID=@procurementAgentId GROUP BY n.`NOTIFICATION_ID`;
END
