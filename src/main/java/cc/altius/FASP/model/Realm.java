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
 * @author altius
 */
public class Realm extends BaseModel implements Serializable {

    @JsonView(Views.InternalView.class)
    private Integer realmId;
    @JsonView(Views.InternalView.class)
    private Label label;
    @JsonView(Views.InternalView.class)
    private String realmCode;
    private boolean defaultRealm;
    @JsonView(Views.InternalView.class)
    private int minMosMinGaurdrail;
    @JsonView(Views.InternalView.class)
    private int minMosMaxGaurdrail;
    @JsonView(Views.InternalView.class)
    private int maxMosMaxGaurdrail;
    @JsonView(Views.InternalView.class)
    private int minQplTolerance;
    @JsonView(Views.InternalView.class)
    private int minQplToleranceCutOff;
    @JsonView(Views.InternalView.class)
    private int maxQplTolerance;
    @JsonView(Views.InternalView.class)
    private int actualConsumptionMonthsInPast;
    @JsonView(Views.InternalView.class)
    private int forecastConsumptionMonthsInPast;
    @JsonView(Views.InternalView.class)
    private int inventoryMonthsInPast;
    @JsonView(Views.InternalView.class)
    private int minCountForMode;
    @JsonView(Views.InternalView.class)
    private double minPercForMode;

    public Realm(Integer realmId, Label label, String realmCode, int minMosMinGaurdrail, int minMosMaxGaurdrail, int maxMosMaxGaurdrail, int minQplTolerance, int minQplToleranceCutOff, int maxQplTolerance, int actualConsumptionMonthsInPast, int forecastConsumtionMonthsInPast, int inventoryMonthsInPast, int minCountForMode, double minPercForMode) {
        if (realmId == null || realmId == 0) {
            realmId = -1;
        }
        this.realmId = realmId;
        this.label = label;
        this.realmCode = realmCode;
        this.minMosMinGaurdrail = minMosMinGaurdrail;
        this.minMosMaxGaurdrail = minMosMaxGaurdrail;
        this.maxMosMaxGaurdrail = maxMosMaxGaurdrail;
        this.minQplTolerance = minQplTolerance;
        this.minQplToleranceCutOff = minQplToleranceCutOff;
        this.maxQplTolerance = maxQplTolerance;
        this.actualConsumptionMonthsInPast = actualConsumptionMonthsInPast;
        this.forecastConsumptionMonthsInPast = forecastConsumtionMonthsInPast;
        this.inventoryMonthsInPast = inventoryMonthsInPast;
    }

    public Realm(Integer realmId, Label label, String realmCode) {
        if (realmId == null || realmId == 0) {
            realmId = -1;
        }
        this.realmId = realmId;
        this.label = label;
        this.realmCode = realmCode;
    }

    public Realm() {
    }

    public Realm(Integer realmId) {
        if (realmId == null || realmId == 0) {
            realmId = -1;
        }
        this.realmId = realmId;
    }

    public Integer getRealmId() {
        return realmId;
    }

    public void setRealmId(Integer realmId) {
        if (realmId == null || realmId == 0) {
            realmId = -1;
        }
        this.realmId = realmId;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getRealmCode() {
        return realmCode;
    }

    public void setRealmCode(String realmCode) {
        this.realmCode = realmCode;
    }

    public boolean isDefaultRealm() {
        return defaultRealm;
    }

    public void setDefaultRealm(boolean defaultRealm) {
        this.defaultRealm = defaultRealm;
    }

    public int getMinMosMinGaurdrail() {
        return minMosMinGaurdrail;
    }

    public void setMinMosMinGaurdrail(int minMosMinGaurdrail) {
        this.minMosMinGaurdrail = minMosMinGaurdrail;
    }

    public int getMinMosMaxGaurdrail() {
        return minMosMaxGaurdrail;
    }

    public void setMinMosMaxGaurdrail(int minMosMaxGaurdrail) {
        this.minMosMaxGaurdrail = minMosMaxGaurdrail;
    }

    public int getMaxMosMaxGaurdrail() {
        return maxMosMaxGaurdrail;
    }

    public void setMaxMosMaxGaurdrail(int maxMosMaxGaurdrail) {
        this.maxMosMaxGaurdrail = maxMosMaxGaurdrail;
    }

    public int getMinQplTolerance() {
        return minQplTolerance;
    }

    public void setMinQplTolerance(int minQplTolerance) {
        this.minQplTolerance = minQplTolerance;
    }

    public int getMinQplToleranceCutOff() {
        return minQplToleranceCutOff;
    }

    public void setMinQplToleranceCutOff(int minQplToleranceCutOff) {
        this.minQplToleranceCutOff = minQplToleranceCutOff;
    }

    public int getMaxQplTolerance() {
        return maxQplTolerance;
    }

    public void setMaxQplTolerance(int maxQplTolerance) {
        this.maxQplTolerance = maxQplTolerance;
    }

    public int getActualConsumptionMonthsInPast() {
        return actualConsumptionMonthsInPast;
    }

    public void setActualConsumptionMonthsInPast(int actualConsumptionMonthsInPast) {
        this.actualConsumptionMonthsInPast = actualConsumptionMonthsInPast;
    }

    public int getForecastConsumptionMonthsInPast() {
        return forecastConsumptionMonthsInPast;
    }

    public void setForecastConsumptionMonthsInPast(int forecastConsumptionMonthsInPast) {
        this.forecastConsumptionMonthsInPast = forecastConsumptionMonthsInPast;
    }

    public int getInventoryMonthsInPast() {
        return inventoryMonthsInPast;
    }

    public void setInventoryMonthsInPast(int inventoryMonthsInPast) {
        this.inventoryMonthsInPast = inventoryMonthsInPast;
    }

    public int getMinCountForMode() {
        return minCountForMode;
    }

    public void setMinCountForMode(int minCountForMode) {
        this.minCountForMode = minCountForMode;
    }

    public double getMinPercForMode() {
        return minPercForMode;
    }

    public void setMinPercForMode(double minPercForMode) {
        this.minPercForMode = minPercForMode;
    }

    @Override
    public String toString() {
        return "Realm{" + "realmId=" + realmId + ", label=" + label + ", realmCode=" + realmCode + ", defaultRealm=" + defaultRealm + '}';
    }

}
