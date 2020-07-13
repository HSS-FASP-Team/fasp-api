/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model;

import java.io.Serializable;

/**
 *
 * @author akil
 */
public class ProcurementUnit extends BaseModel implements Serializable {

    private int procurementUnitId;
    private PlanningUnit planningUnit;
    private Label label;
    private SimpleObject unit;
    private Double multiplier;
    private SimpleObject supplier;
    private Double widthQty;
    private SimpleObject widthUnit;
    private Double heightQty;
    private SimpleObject heightUnit;
    private Double lengthQty;
    private SimpleObject lengthUnit;
    private Double weightQty;
    private SimpleObject weightUnit;
    private Double unitsPerCase;
    private Double unitsPerPallet;
    private Double unitsPerContainer;
    private String labeling;

    public ProcurementUnit() {
    }

    public ProcurementUnit(int procurementUnitId, Label label) {
        this.procurementUnitId = procurementUnitId;
        this.label = label;
    }

    public ProcurementUnit(int procurementUnitId, PlanningUnit planningUnit, Label label, SimpleObject unit, double multiplier) {
        this.procurementUnitId = procurementUnitId;
        this.planningUnit = planningUnit;
        this.label = label;
        this.unit = unit;
        this.multiplier = multiplier;
    }

    public int getProcurementUnitId() {
        return procurementUnitId;
    }

    public void setProcurementUnitId(int procurementUnitId) {
        this.procurementUnitId = procurementUnitId;
    }

    public PlanningUnit getPlanningUnit() {
        return planningUnit;
    }

    public void setPlanningUnit(PlanningUnit planningUnit) {
        this.planningUnit = planningUnit;
    }

    public SimpleObject getUnit() {
        return unit;
    }

    public void setUnit(SimpleObject unit) {
        this.unit = unit;
    }

    public SimpleObject getSupplier() {
        return supplier;
    }

    public void setSupplier(SimpleObject supplier) {
        this.supplier = supplier;
    }

    public SimpleObject getWidthUnit() {
        return widthUnit;
    }

    public void setWidthUnit(SimpleObject widthUnit) {
        this.widthUnit = widthUnit;
    }

    public SimpleObject getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(SimpleObject heightUnit) {
        this.heightUnit = heightUnit;
    }

    public SimpleObject getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(SimpleObject lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public SimpleObject getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(SimpleObject weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public Double getWidthQty() {
        return widthQty;
    }

    public void setWidthQty(Double widthQty) {
        this.widthQty = widthQty;
    }

    public Double getHeightQty() {
        return heightQty;
    }

    public void setHeightQty(Double heightQty) {
        this.heightQty = heightQty;
    }

    public Double getLengthQty() {
        return lengthQty;
    }

    public void setLengthQty(Double lengthQty) {
        this.lengthQty = lengthQty;
    }

    public Double getWeightQty() {
        return weightQty;
    }

    public void setWeightQty(Double weightQty) {
        this.weightQty = weightQty;
    }

    public Double getUnitsPerCase() {
        return unitsPerCase;
    }

    public void setUnitsPerCase(Double unitsPerCase) {
        this.unitsPerCase = unitsPerCase;
    }

    public Double getUnitsPerPallet() {
        return unitsPerPallet;
    }

    public void setUnitsPerPallet(Double unitsPerPallet) {
        this.unitsPerPallet = unitsPerPallet;
    }

    public Double getUnitsPerContainer() {
        return unitsPerContainer;
    }

    public void setUnitsPerContainer(Double unitsPerContainer) {
        this.unitsPerContainer = unitsPerContainer;
    }

    public String getLabeling() {
        return labeling;
    }

    public void setLabeling(String labeling) {
        this.labeling = labeling;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.procurementUnitId;
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
        final ProcurementUnit other = (ProcurementUnit) obj;
        if (this.procurementUnitId != other.procurementUnitId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProcurementUnit{" + "procurementUnitId=" + procurementUnitId + ", planningUnit=" + planningUnit + ", label=" + label + ", unit=" + unit + ", multiplier=" + multiplier + ", supplier=" + supplier + ", widthQty=" + widthQty + ", widthUnit=" + widthUnit + ", heightQty=" + heightQty + ", heightUnit=" + heightUnit + ", lengthQty=" + lengthQty + ", lengthUnit=" + lengthUnit + ", weightQty=" + weightQty + ", weightUnit=" + weightUnit + ", unitsPerCase=" + unitsPerCase + ", unitsPerPallet=" + unitsPerPallet + ", unitsPerContainer=" + unitsPerContainer + ", labeling=" + labeling + '}';
    }

}
