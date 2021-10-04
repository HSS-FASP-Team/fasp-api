/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;

/**
 *
 * @author akil
 */
public class TreeNodeDataFu implements Serializable {

    @JsonView(Views.InternalView.class)
    private int nodeDataFuId;
    @JsonView(Views.InternalView.class)
    private SimpleUnitAndTracerObject forecastingUnit;
    @JsonView(Views.InternalView.class)
    private int lagInMonths;
    @JsonView(Views.InternalView.class)
    private SimpleObject usageType;
    @JsonView(Views.InternalView.class)
    private int noOfPersons;
    @JsonView(Views.InternalView.class)
    private int noOfForecastingUnitsPerPerson;
    @JsonView(Views.InternalView.class)
    private boolean oneTimeUsage;
    @JsonView(Views.InternalView.class)
    private double usageFrequency;
    @JsonView(Views.InternalView.class)
    private UsagePeriod usagePeriod;
    @JsonView(Views.InternalView.class)
    private Double repeatCount;
    @JsonView(Views.InternalView.class)
    private UsagePeriod repeatUsagePeriod;

    public int getNodeDataFuId() {
        return nodeDataFuId;
    }

    public void setNodeDataFuId(int nodeDataFuId) {
        this.nodeDataFuId = nodeDataFuId;
    }

    public SimpleUnitAndTracerObject getForecastingUnit() {
        return forecastingUnit;
    }

    public void setForecastingUnit(SimpleUnitAndTracerObject forecastingUnit) {
        this.forecastingUnit = forecastingUnit;
    }

    public int getLagInMonths() {
        return lagInMonths;
    }

    public void setLagInMonths(int lagInMonths) {
        this.lagInMonths = lagInMonths;
    }

    public SimpleObject getUsageType() {
        return usageType;
    }

    public void setUsageType(SimpleObject usageType) {
        this.usageType = usageType;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public int getNoOfForecastingUnitsPerPerson() {
        return noOfForecastingUnitsPerPerson;
    }

    public void setNoOfForecastingUnitsPerPerson(int noOfForecastingUnitsPerPerson) {
        this.noOfForecastingUnitsPerPerson = noOfForecastingUnitsPerPerson;
    }

    public boolean isOneTimeUsage() {
        return oneTimeUsage;
    }

    public void setOneTimeUsage(boolean oneTimeUsage) {
        this.oneTimeUsage = oneTimeUsage;
    }

    public double getUsageFrequency() {
        return usageFrequency;
    }

    public void setUsageFrequency(double usageFrequency) {
        this.usageFrequency = usageFrequency;
    }

    public UsagePeriod getUsagePeriod() {
        return usagePeriod;
    }

    public void setUsagePeriod(UsagePeriod usagePeriod) {
        this.usagePeriod = usagePeriod;
    }

    public Double getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Double repeatCount) {
        this.repeatCount = repeatCount;
    }

    public UsagePeriod getRepeatUsagePeriod() {
        return repeatUsagePeriod;
    }

    public void setRepeatUsagePeriod(UsagePeriod repeatUsagePeriod) {
        this.repeatUsagePeriod = repeatUsagePeriod;
    }


}