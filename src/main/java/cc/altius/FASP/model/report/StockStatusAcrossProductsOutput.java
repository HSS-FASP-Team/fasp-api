/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.report;

import cc.altius.FASP.model.SimpleObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author akil
 */
public class StockStatusAcrossProductsOutput implements Serializable {

    private SimpleObject planningUnit;
    private Map<String, StockStatusAcrossProductsForProgram> programData;

    public StockStatusAcrossProductsOutput() {
        this.programData = new HashMap<>();
    }

    public SimpleObject getPlanningUnit() {
        return planningUnit;
    }

    public void setPlanningUnit(SimpleObject planningUnit) {
        this.planningUnit = planningUnit;
    }

    public Map<String, StockStatusAcrossProductsForProgram> getProgramData() {
        return programData;
    }

    public void setProgramData(Map<String, StockStatusAcrossProductsForProgram> programData) {
        this.programData = programData;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.planningUnit);
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
        final StockStatusAcrossProductsOutput other = (StockStatusAcrossProductsOutput) obj;
        if (!Objects.equals(this.planningUnit.getId(), other.planningUnit.getId())) {
            return false;
        }
        return true;
    }
    
}
