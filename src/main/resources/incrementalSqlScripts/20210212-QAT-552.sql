    SET @programId = 2531;
    SET @newPlanningUnitId = 3869;
    SET @oldPlanningUnitId = 3870;
    SET @dt = '2021-02-16 05:00:00';
    UPDATE rm_program_planning_unit ppu SET ppu.PLANNING_UNIT_ID=@newPlanningUnitId, ppu.LAST_MODIFIED_DATE=@dt WHERE ppu.PROGRAM_ID=@programId and ppu.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_realm_country_planning_unit rcpu SET rcpu.PLANNING_UNIT_ID=@newPlanningUnitId, rcpu.LAST_MODIFIED_DATE=@dt WHERE rcpu.REALM_COUNTRY_ID=51 and rcpu.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_consumption c LEFT JOIN rm_consumption_trans ct ON c.CONSUMPTION_ID=ct.CONSUMPTION_ID SET ct.PLANNING_UNIT_ID=@newPlanningUnitId WHERE c.PROGRAM_ID=@programId AND ct.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_shipment s LEFT JOIN rm_shipment_trans st ON s.SHIPMENT_ID=st.SHIPMENT_ID SET st.PLANNING_UNIT_ID=@newPlanningUnitId WHERE s.PROGRAM_ID=@programId AND st.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_supply_plan_amc sma SET sma.PLANNING_UNIT_ID=@newPlanningUnitId WHERE sma.PROGRAM_ID=@programId and sma.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_supply_plan_batch_qty spbq SET spbq.PLANNING_UNIT_ID=@newPlanningUnitId WHERE spbq.PLANNING_UNIT_ID=@oldPlanningUnitId and spbq.PROGRAM_ID=@programId;
    UPDATE rm_batch_info bi SET bi.PLANNING_UNIT_ID=@newPlanningUnitId where bi.PROGRAM_ID=@programId and bi.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_problem_report pr SET pr.DATA3=@newPlanningUnitId where pr.PROGRAM_ID=@programId AND pr.DATA3=@oldPlanningUnitId;
    
    SET @newPlanningUnitId = 6363;
    SET @oldPlanningUnitId = 3872;
    UPDATE rm_program_planning_unit ppu SET ppu.PLANNING_UNIT_ID=@newPlanningUnitId, ppu.LAST_MODIFIED_DATE=@dt WHERE ppu.PROGRAM_ID=@programId and ppu.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_realm_country_planning_unit rcpu SET rcpu.PLANNING_UNIT_ID=@newPlanningUnitId, rcpu.LAST_MODIFIED_DATE=@dt WHERE rcpu.REALM_COUNTRY_ID=51 and rcpu.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_consumption c LEFT JOIN rm_consumption_trans ct ON c.CONSUMPTION_ID=ct.CONSUMPTION_ID SET ct.PLANNING_UNIT_ID=@newPlanningUnitId WHERE c.PROGRAM_ID=@programId AND ct.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_shipment s LEFT JOIN rm_shipment_trans st ON s.SHIPMENT_ID=st.SHIPMENT_ID SET st.PLANNING_UNIT_ID=@newPlanningUnitId WHERE s.PROGRAM_ID=@programId AND st.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_supply_plan_amc sma SET sma.PLANNING_UNIT_ID=@newPlanningUnitId WHERE sma.PROGRAM_ID=@programId and sma.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_supply_plan_batch_qty spbq SET spbq.PLANNING_UNIT_ID=@newPlanningUnitId WHERE spbq.PLANNING_UNIT_ID=@oldPlanningUnitId and spbq.PROGRAM_ID=@programId;
    UPDATE rm_batch_info bi SET bi.PLANNING_UNIT_ID=@newPlanningUnitId where bi.PROGRAM_ID=@programId and bi.PLANNING_UNIT_ID=@oldPlanningUnitId;
    UPDATE rm_problem_report pr SET pr.DATA3=@newPlanningUnitId where pr.PROGRAM_ID=@programId AND pr.DATA3=@oldPlanningUnitId;