/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.report;

import cc.altius.FASP.model.SimpleObject;
import cc.altius.FASP.model.Views;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;

/**
 *
 * @author akil
 */
public class ShipmentOverviewPlanningUnitSplit implements Serializable {

    @JsonView(Views.ReportView.class)
    private SimpleObject planningUnit;
    @JsonView(Views.ReportView.class)
    private int multiplier;
    @JsonView(Views.ReportView.class)
    private long plannedShipmentQty;
    @JsonView(Views.ReportView.class)
    private long orderedShipmentQty;

    public SimpleObject getPlanningUnit() {
        return planningUnit;
    }

    public void setPlanningUnit(SimpleObject planningUnit) {
        this.planningUnit = planningUnit;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public long getPlannedShipmentQty() {
        return plannedShipmentQty;
    }

    public void setPlannedShipmentQty(long plannedShipmentQty) {
        this.plannedShipmentQty = plannedShipmentQty;
    }

    public long getOrderedShipmentQty() {
        return orderedShipmentQty;
    }

    public void setOrderedShipmentQty(long orderedShipmentQty) {
        this.orderedShipmentQty = orderedShipmentQty;
    }

}
