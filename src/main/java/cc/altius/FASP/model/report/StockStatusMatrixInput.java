/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.report;

import cc.altius.FASP.framework.JsonDateDeserializer;
import cc.altius.FASP.framework.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author akil
 */
public class StockStatusMatrixInput implements Serializable {

    private int programId;
    private int versionId;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date startDate;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date stopDate;
    private String[] planningUnitIds;
    private String[] tracerCategoryIds;
    private boolean includePlannedShipments;

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public boolean isIncludePlannedShipments() {
        return includePlannedShipments;
    }

    public void setIncludePlannedShipments(boolean includePlannedShipments) {
        this.includePlannedShipments = includePlannedShipments;
    }

    public String[] getPlanningUnitIds() {
        return planningUnitIds;
    }

    public void setPlanningUnitIds(String[] planningUnitIds) {
        this.planningUnitIds = planningUnitIds;
    }

    public String[] getTracerCategoryIds() {
        return tracerCategoryIds;
    }

    public void setTracerCategoryIds(String[] tracerCategoryIds) {
        this.tracerCategoryIds = tracerCategoryIds;
    }

//    public String getPlanningUnitIdsString() {
//        if (this.planningUnitIds == null) {
//            return "";
//        } else {
//            String opt = String.join("','", this.planningUnitIds);
//            if (this.planningUnitIds.length > 0) {
//                return "'" + opt + "'";
//            } else {
//                return opt;
//            }
//        }
//    }

}
