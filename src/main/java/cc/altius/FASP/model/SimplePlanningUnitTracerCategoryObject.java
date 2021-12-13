/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model;

import com.fasterxml.jackson.annotation.JsonView;

/**
 *
 * @author akil
 */
public class SimplePlanningUnitTracerCategoryObject extends SimpleObjectWithMultiplier {

    @JsonView(Views.InternalView.class)
    private SimpleForecastingUnitTracerCategoryObject forecastingUnit;

    public SimplePlanningUnitTracerCategoryObject() {
        super();
    }

    public SimplePlanningUnitTracerCategoryObject(Integer id, Label label, double multiplier, SimpleForecastingUnitTracerCategoryObject forecastingUnit) {
        super(id, label, multiplier);
        this.forecastingUnit = forecastingUnit;
    }

    public SimpleForecastingUnitTracerCategoryObject getForecastingUnit() {
        return forecastingUnit;
    }

    public void setForecastingUnit(SimpleForecastingUnitTracerCategoryObject forecastingUnit) {
        this.forecastingUnit = forecastingUnit;
    }
}
