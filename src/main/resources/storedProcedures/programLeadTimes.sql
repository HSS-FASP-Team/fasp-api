CREATE DEFINER=`faspUser`@`localhost` PROCEDURE `programLeadTimes`(VAR_PROGRAM_ID INT(10), VAR_PROCUREMENT_AGENT_IDS VARCHAR(255), VAR_PLANNING_UNIT_IDS TEXT)
BEGIN
	-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	-- Report no 14
	-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    -- VAR_PROGRAM_ID is the program that you want to run the report for
    -- VAR_PROCUREMENT_AGENT_IDS is the list of Procurement Agents you want to include in the report
    -- VAR_PLANNING_UNIT_IDS is the list of Planning Units that you want to see the report for
    SET @programId = VAR_PROGRAM_ID;

    SET @sqlString = "";
    SET @sqlString = CONCAT(@sqlString, "SELECT * FROM (SELECT ");
    SET @sqlString = CONCAT(@sqlString, "	rc.REALM_COUNTRY_ID, c.COUNTRY_CODE, c.COUNTRY_CODE2, c.LABEL_ID `COUNTRY_LABEL_ID`, c.LABEL_EN `COUNTRY_LABEL_EN`, c.LABEL_FR `COUNTRY_LABEL_FR`, c.LABEL_SP `COUNTRY_LABEL_SP`, c.LABEL_PR `COUNTRY_LABEL_PR`, "); 
    SET @sqlString = CONCAT(@sqlString, "   p.PROGRAM_ID, p.PROGRAM_CODE, p.LABEL_ID `PROGRAM_LABEL_ID`, p.LABEL_EN `PROGRAM_LABEL_EN`, p.LABEL_FR `PROGRAM_LABEL_FR`, p.LABEL_SP `PROGRAM_LABEL_SP`, p.LABEL_PR `PROGRAM_LABEL_PR`, "); 
    SET @sqlString = CONCAT(@sqlString, "	pu.PLANNING_UNIT_ID, pu.LABEL_ID `PLANNING_UNIT_LABEL_ID`, pu.LABEL_EN `PLANNING_UNIT_LABEL_EN`, pu.LABEL_FR `PLANNING_UNIT_LABEL_FR`, pu.LABEL_SP `PLANNING_UNIT_LABEL_SP`, pu.LABEL_PR `PLANNING_UNIT_LABEL_PR`, "); 
    SET @sqlString = CONCAT(@sqlString, "	0 `PROCUREMENT_AGENT_ID`, 'Local' `PROCUREMENT_AGENT_CODE`, l.LABEL_ID `PROCUREMENT_AGENT_LABEL_ID`, l.LABEL_EN `PROCUREMENT_AGENT_LABEL_EN`, l.LABEL_FR `PROCUREMENT_AGENT_LABEL_FR`, l.LABEL_SP `PROCUREMENT_AGENT_LABEL_SP`, l.LABEL_PR `PROCUREMENT_AGENT_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	null `PLANNED_TO_SUBMITTED_LEAD_TIME`, null `SUBMITTED_TO_APPROVED_LEAD_TIME`, null `APPROVED_TO_SHIPPED_LEAD_TIME`, null `SHIPPED_TO_ARRIVED_BY_AIR_LEAD_TIME`, null `SHIPPED_TO_ARRIVED_BY_SEA_LEAD_TIME`, null `SHIPPED_TO_ARRIVED_BY_ROAD_LEAD_TIME`, null `ARRIVED_TO_DELIVERED_LEAD_TIME`, ppu.LOCAL_PROCUREMENT_LEAD_TIME ");
    SET @sqlString = CONCAT(@sqlString, "FROM vw_program p ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN rm_realm_country rc ON p.REALM_COUNTRY_ID=rc.REALM_COUNTRY_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN vw_country c ON rc.COUNTRY_ID=c.COUNTRY_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN rm_program_planning_unit ppu ON p.PROGRAM_ID=ppu.PROGRAM_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN vw_planning_unit pu ON ppu.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN ap_label l ON l.LABEL_ID=108 ");
    SET @sqlString = CONCAT(@sqlString, "WHERE p.PROGRAM_ID=@programId AND ppu.ACTIVE AND pu.ACTIVE ");
    IF LENGTH(VAR_PLANNING_UNIT_IDS)>0 THEN
	SET @sqlString = CONCAT(@sqlString, "AND ppu.PLANNING_UNIT_ID IN (" , VAR_PLANNING_UNIT_IDS , ") ");
    END IF;
    
    SET @sqlString = CONCAT(@sqlString, "UNION  ");
	
    SET @sqlString = CONCAT(@sqlString, "SELECT  ");
    SET @sqlString = CONCAT(@sqlString, "	rc.REALM_COUNTRY_ID, c.COUNTRY_CODE, c.COUNTRY_CODE2, c.LABEL_ID `COUNTRY_LABEL_ID`, c.LABEL_EN `COUNTRY_LABEL_EN`, c.LABEL_FR `COUNTRY_LABEL_FR`, c.LABEL_SP `COUNTRY_LABEL_SP`, c.LABEL_PR `COUNTRY_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	p.PROGRAM_ID, p.PROGRAM_CODE, p.LABEL_ID `PROGRAM_LABEL_ID`, p.LABEL_EN `PROGRAM_LABEL_EN`, p.LABEL_FR `PROGRAM_LABEL_FR`, p.LABEL_SP `PROGRAM_LABEL_SP`, p.LABEL_PR `PROGRAM_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	pu.PLANNING_UNIT_ID, pu.LABEL_ID `PLANNING_UNIT_LABEL_ID`, pu.LABEL_EN `PLANNING_UNIT_LABEL_EN`, pu.LABEL_FR `PLANNING_UNIT_LABEL_FR`, pu.LABEL_SP `PLANNING_UNIT_LABEL_SP`, pu.LABEL_PR `PLANNING_UNIT_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	0 `PROCUREMENT_AGENT_ID`, 'Not selected' `PROCUREMENT_AGENT_CODE`, l.LABEL_ID `PROCUREMENT_AGENT_LABEL_ID`, l.LABEL_EN `PROCUREMENT_AGENT_LABEL_EN`, l.LABEL_FR `PROCUREMENT_AGENT_LABEL_FR`, l.LABEL_SP `PROCUREMENT_AGENT_LABEL_SP`, l.LABEL_PR `PROCUREMENT_AGENT_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	p.PLANNED_TO_SUBMITTED_LEAD_TIME, p.SUBMITTED_TO_APPROVED_LEAD_TIME, p.APPROVED_TO_SHIPPED_LEAD_TIME, p.SHIPPED_TO_ARRIVED_BY_AIR_LEAD_TIME, p.SHIPPED_TO_ARRIVED_BY_SEA_LEAD_TIME, p.SHIPPED_TO_ARRIVED_BY_ROAD_LEAD_TIME, p.ARRIVED_TO_DELIVERED_LEAD_TIME, null `LOCAL_PROCUREMENT_LEAD_TIME` ");
    SET @sqlString = CONCAT(@sqlString, "FROM vw_program p ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN rm_realm_country rc ON p.REALM_COUNTRY_ID=rc.REALM_COUNTRY_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN vw_country c ON rc.COUNTRY_ID=c.COUNTRY_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN rm_program_planning_unit ppu ON p.PROGRAM_ID=ppu.PROGRAM_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN vw_planning_unit pu ON ppu.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN ap_label l ON l.LABEL_ID=453 ");
    SET @sqlString = CONCAT(@sqlString, "WHERE p.PROGRAM_ID=@programId AND ppu.ACTIVE AND pu.ACTIVE ");
    IF LENGTH(VAR_PLANNING_UNIT_IDS)>0 THEN
    	SET @sqlString = CONCAT(@sqlString, "AND ppu.PLANNING_UNIT_ID IN (" , VAR_PLANNING_UNIT_IDS , ") ");
    END IF;
    
    SET @sqlString = CONCAT(@sqlString, "UNION  ");
    
    SET @sqlString = CONCAT(@sqlString, "SELECT  ");
    SET @sqlString = CONCAT(@sqlString, "	rc.REALM_COUNTRY_ID, c.COUNTRY_CODE, c.COUNTRY_CODE2, c.LABEL_ID `COUNTRY_LABEL_ID`, c.LABEL_EN `COUNTRY_LABEL_EN`, c.LABEL_FR `COUNTRY_LABEL_FR`, c.LABEL_SP `COUNTRY_LABEL_SP`, c.LABEL_PR `COUNTRY_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	p.PROGRAM_ID, p.PROGRAM_CODE, p.LABEL_ID `PROGRAM_LABEL_ID`, p.LABEL_EN `PROGRAM_LABEL_EN`, p.LABEL_FR `PROGRAM_LABEL_FR`, p.LABEL_SP `PROGRAM_LABEL_SP`, p.LABEL_PR `PROGRAM_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	pu.PLANNING_UNIT_ID, pu.LABEL_ID `PLANNING_UNIT_LABEL_ID`, pu.LABEL_EN `PLANNING_UNIT_LABEL_EN`, pu.LABEL_FR `PLANNING_UNIT_LABEL_FR`, pu.LABEL_SP `PLANNING_UNIT_LABEL_SP`, pu.LABEL_PR `PLANNING_UNIT_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	pa.PROCUREMENT_AGENT_ID, pa.PROCUREMENT_AGENT_CODE, pa.LABEL_ID `PROCUREMENT_AGENT_LABEL_ID`, pa.LABEL_EN `PROCUREMENT_AGENT_LABEL_EN`, pa.LABEL_FR `PROCUREMENT_AGENT_LABEL_FR`, pa.LABEL_SP `PROCUREMENT_AGENT_LABEL_SP`, pa.LABEL_PR `PROCUREMENT_AGENT_LABEL_PR`, ");
    SET @sqlString = CONCAT(@sqlString, "	p.PLANNED_TO_SUBMITTED_LEAD_TIME, pa.SUBMITTED_TO_APPROVED_LEAD_TIME, pa.APPROVED_TO_SHIPPED_LEAD_TIME, ");
    SET @sqlString = CONCAT(@sqlString, "	p.SHIPPED_TO_ARRIVED_BY_AIR_LEAD_TIME, p.SHIPPED_TO_ARRIVED_BY_SEA_LEAD_TIME, p.SHIPPED_TO_ARRIVED_BY_ROAD_LEAD_TIME, p.ARRIVED_TO_DELIVERED_LEAD_TIME, null LOCAL_PROCUREMENT_LEAD_TIME ");
    SET @sqlString = CONCAT(@sqlString, "FROM vw_program p ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN rm_realm_country rc ON p.REALM_COUNTRY_ID=rc.REALM_COUNTRY_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN vw_country c ON rc.COUNTRY_ID=c.COUNTRY_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN rm_program_planning_unit ppu ON p.PROGRAM_ID=ppu.PROGRAM_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN vw_planning_unit pu ON ppu.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID ");
    SET @sqlString = CONCAT(@sqlString, "LEFT JOIN vw_procurement_agent pa ON pa.ACTIVE ");
    SET @sqlString = CONCAT(@sqlString, "WHERE p.PROGRAM_ID=@programId AND ppu.ACTIVE AND pu.ACTIVE ");
    IF LENGTH(VAR_PLANNING_UNIT_IDS)>0 THEN
	SET @sqlString = CONCAT(@sqlString, "AND ppu.PLANNING_UNIT_ID IN (" , VAR_PLANNING_UNIT_IDS , ") ");
    END IF;
    IF LENGTH(VAR_PROCUREMENT_AGENT_IDS)>0 THEN
	SET @sqlString = CONCAT(@sqlString, "AND pa.PROCUREMENT_AGENT_ID IN (" , VAR_PROCUREMENT_AGENT_IDS , ") ");
    END IF;
    SET @sqlString = CONCAT(@sqlString, ") p1 ORDER BY p1.PROGRAM_ID, p1.PLANNING_UNIT_ID, IFNULL(p1.PROCUREMENT_AGENT_ID,0) ");
    PREPARE S1 FROM @sqlString;
    EXECUTE S1;
END
