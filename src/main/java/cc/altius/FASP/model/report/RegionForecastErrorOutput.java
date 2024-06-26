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
import java.util.Objects;

/**
 *
 * @author akil
 */
public class RegionForecastErrorOutput implements Serializable {

    @JsonView(Views.ReportView.class)
    private SimpleObject region;
    @JsonView(Views.ReportView.class)
    private Double actualQty;
    @JsonView(Views.ReportView.class)
    private Double forecastQty;
    @JsonView(Views.ReportView.class)
    private Double sumOfForecast;
    @JsonView(Views.ReportView.class)
    private Double sumOfActual;
    private Double sumOfAbsDiff;
    @JsonView(Views.ReportView.class)
    private Integer daysOfStockOut;

    public RegionForecastErrorOutput(SimpleObject region) {
        this.region = region;
    }

    public SimpleObject getRegion() {
        return region;
    }

    public void setRegion(SimpleObject region) {
        this.region = region;
    }

    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }

    public Double getForecastQty() {
        return forecastQty;
    }

    public void setForecastQty(Double forecastQty) {
        this.forecastQty = forecastQty;
    }

    public Double getSumOfForecast() {
        return sumOfForecast;
    }

    public void setSumOfForecast(Double sumOfForecast) {
        this.sumOfForecast = sumOfForecast;
    }

    public Double getSumOfActual() {
        return sumOfActual;
    }

    public void setSumOfActual(Double sumOfActual) {
        this.sumOfActual = sumOfActual;
    }

    public Double getSumOfAbsDiff() {
        return sumOfAbsDiff;
    }

    public void setSumOfAbsDiff(Double sumOfAbsDiff) {
        this.sumOfAbsDiff = sumOfAbsDiff;
    }

    public Integer getDaysOfStockOut() {
        return daysOfStockOut;
    }

    public void setDaysOfStockOut(Integer daysOfStockOut) {
        this.daysOfStockOut = daysOfStockOut;
    }
    
    @JsonView(Views.ReportView.class)
    public Double getErrorPerc() {
        if (this.sumOfActual == null || this.sumOfActual == 0 || this.sumOfAbsDiff == null) {
            return null;
        } else {
            return this.sumOfAbsDiff / this.sumOfActual;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.region);
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
        final RegionForecastErrorOutput other = (RegionForecastErrorOutput) obj;
        if (!Objects.equals(this.region, other.region)) {
            return false;
        }
        return true;
    }

}
