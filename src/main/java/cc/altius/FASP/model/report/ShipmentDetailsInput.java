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
import java.util.Date;

/**
 *
 * @author akil
 */
public class ShipmentDetailsInput {

    private int programId;
    private int versionId;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date startDate;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date stopDate;
    private String[] fundingSourceIds;
    private String[] budgetIds;
    private String[] planningUnitIds;
    private int reportView; // 1 = Planning Units, 2 = Forecasting Units

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

    public String[] getFundingSourceIds() {
        return fundingSourceIds;
    }

    public void setFundingSourceIds(String[] fundingSourceIds) {
        this.fundingSourceIds = fundingSourceIds;
    }

    public String getFundingSourceIdsString() {
        if (this.fundingSourceIds == null) {
            return "";
        } else {
            return String.join(",", this.fundingSourceIds);
        }
    }

    public String[] getBudgetIds() {
        return budgetIds;
    }

    public void setBudgetIds(String[] budgetIds) {
        this.budgetIds = budgetIds;
    }

    public String getBudgetIdsString() {
        if (this.budgetIds == null) {
            return "";
        } else {
            return String.join(",", this.budgetIds);
        }
    }

    public String[] getPlanningUnitIds() {
        return planningUnitIds;
    }

    public void setPlanningUnitIds(String[] planningUnitIds) {
        this.planningUnitIds = planningUnitIds;
    }

    public String getPlanningUnitIdsString() {
        if (this.planningUnitIds == null) {
            return "";
        } else {
            return String.join(",", this.planningUnitIds);
        }
    }

    public int getReportView() {
        return reportView;
    }

    public void setReportView(int reportView) {
        this.reportView = reportView;
    }

}
