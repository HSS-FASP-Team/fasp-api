/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.service;

import cc.altius.FASP.model.CustomUserDetails;
import cc.altius.FASP.model.HealthArea;
import cc.altius.FASP.model.pipeline.QatTempProgram;
import cc.altius.FASP.model.Region;
import cc.altius.FASP.model.pipeline.Pipeline;
import cc.altius.FASP.model.pipeline.PplConsumption;
import cc.altius.FASP.model.pipeline.PplProduct;
import cc.altius.FASP.model.pipeline.PplPrograminfo;
import cc.altius.FASP.model.pipeline.QatTempConsumption;
import cc.altius.FASP.model.pipeline.QatTempInventory;
import cc.altius.FASP.model.pipeline.QatTempPlanningUnitInventoryCount;
import cc.altius.FASP.model.pipeline.QatTempProgramPlanningUnit;
import cc.altius.FASP.model.pipeline.QatTempShipment;
import java.util.List;
import java.util.Map;
import cc.altius.FASP.model.pipeline.QatTempDataSource;
import cc.altius.FASP.model.pipeline.QatTempFundingSource;
import cc.altius.FASP.model.pipeline.QatTempProcurementAgent;

/**
 *
 * @author akil
 */
public interface PipelineDbService {

    public int savePipelineDbData(Pipeline pipeline, CustomUserDetails curUser, String fileName);

    public List<Map<String, Object>> getPipelineProgramList(CustomUserDetails curUser);

    public PplPrograminfo getPipelineProgramInfoById(int pipelineId, CustomUserDetails curUser);

    public int addQatTempProgram(QatTempProgram p, CustomUserDetails curUser, int pipelineId);

    public QatTempProgram getQatTempProgram(CustomUserDetails curUser, int pipelineId);

    public List<PplProduct> getPipelineProductListById(CustomUserDetails curUser, int pipelineId);

    public List<QatTempShipment> getPipelineShipmentdataById(int pipelineId, CustomUserDetails curUser);

    public int saveShipmentData(int pipelineId, QatTempShipment[] shipments, CustomUserDetails curUser);

    public int finalSaveProgramData(int pipelineId, CustomUserDetails curUser);

    public int saveQatTempProgramPlanningUnit(QatTempProgramPlanningUnit[] programPlanningUnits, CustomUserDetails curUser, int pipelineId);

    public List<QatTempProgramPlanningUnit> getQatTempPlanningUnitListByPipelienId(int pipelineId, CustomUserDetails curUser);

    public List<PplConsumption> getPipelineConsumptionById(CustomUserDetails curUser, int pipelineId);

    public List<Region> getQatTempRegionsById(CustomUserDetails curUser, int pipelineId);
    
    public int saveQatTempConsumption(QatTempConsumption[] consumption, CustomUserDetails curUser, int pipelineId);

    public List<QatTempConsumption> getQatTempConsumptionListByPipelienId(int pipelineId, CustomUserDetails curUser);

    public String getPipelineInventoryById(CustomUserDetails curUser, int pipelineId);

    public int saveQatTempInventory(QatTempInventory[] inventory, CustomUserDetails curUser, int pipelineId);

    public List<QatTempPlanningUnitInventoryCount> getQatTempPlanningUnitListInventoryCount(int pipelineId, CustomUserDetails curUser);

    public int saveQatTempDataSource(QatTempDataSource[] datasources, CustomUserDetails curUser, int pipelineId);

    public List<QatTempDataSource> getQatTempDataSourceListByPipelienId(int pipelineId, CustomUserDetails curUser);

    public int saveQatTempFundingSource(QatTempFundingSource[] fundingsources, CustomUserDetails curUser, int pipelineId);

    public List<QatTempFundingSource> getQatTempFundingSourceListByPipelienId(int pipelineId, CustomUserDetails curUser);

    public int saveQatTempProcurementAgent(QatTempProcurementAgent[] procurementAgents, CustomUserDetails curUser, int pipelineId);

    public List<QatTempProcurementAgent> getQatTempProcurementAgentListByPipelienId(int pipelineId, CustomUserDetails curUser);

    public void createRealmCountryPlanningUnits(int pipelineId, CustomUserDetails curUser, int realmCountryId);

}
