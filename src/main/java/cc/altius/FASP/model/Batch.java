/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model;

import cc.altius.FASP.framework.JsonDateDeserializer;
import cc.altius.FASP.framework.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author akil
 */
public class Batch implements Serializable {

    @JsonView({Views.ArtmisView.class, Views.GfpVanView.class, Views.InternalView.class, Views.ReportView.class})
    private int batchId;
    @JsonView({Views.ArtmisView.class, Views.GfpVanView.class, Views.InternalView.class, Views.ReportView.class})
    private String batchNo;
    @JsonView({Views.ArtmisView.class, Views.GfpVanView.class, Views.InternalView.class, Views.ReportView.class})
    private boolean autoGenerated;
    @JsonView(Views.InternalView.class)
    private int planningUnitId;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonView({Views.ArtmisView.class, Views.GfpVanView.class, Views.InternalView.class, Views.ReportView.class})
    private Date expiryDate;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonView({Views.ArtmisView.class, Views.GfpVanView.class, Views.InternalView.class, Views.ReportView.class})
    private Date createdDate;

    public Batch() {
    }

    public Batch(int batchId, int planningUnitId, String batchNo, boolean autoGenerated, Date expiryDate, Date creatDate) {
        this.batchId = batchId;
        this.planningUnitId = planningUnitId;
        this.batchNo = batchNo;
        this.expiryDate = expiryDate;
        this.createdDate = creatDate;
        this.autoGenerated = autoGenerated;
    }

    public Batch(int batchId, int planningUnitId, String batchNo, boolean autoGenerated, Date expiryDate) {
        this.batchId = batchId;
        this.batchNo = batchNo;
        this.planningUnitId = planningUnitId;
        this.autoGenerated = autoGenerated;
        this.expiryDate = expiryDate;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getPlanningUnitId() {
        return planningUnitId;
    }

    public void setPlanningUnitId(int planningUnitId) {
        this.planningUnitId = planningUnitId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isAutoGenerated() {
        return autoGenerated;
    }

    public void setAutoGenerated(boolean autoGenerated) {
        this.autoGenerated = autoGenerated;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.batchId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Batch other = (Batch) obj;
        if (this.batchId != other.batchId) {
            return false;
        }
        return true;
    }

}
