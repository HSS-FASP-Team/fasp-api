/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model.report;

import cc.altius.FASP.model.Views;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author akil
 */
public class ShipmentGlobalDemandOutput implements Serializable {

    @JsonView(Views.ReportView.class)
    private List<ShipmentGlobalDemandShipmentList> shipmentList;
    @JsonView(Views.ReportView.class)
    private List<ShipmentGlobalDemandDateSplit> dateSplitList;
    @JsonView(Views.ReportView.class)
    private List<ShipmentGlobalDemandCountrySplit> countrySplitList;
    @JsonView(Views.ReportView.class)
    private List<ShipmentGlobalDemandCountryShipmentSplit> countryShipmentSplitList;

    public List<ShipmentGlobalDemandShipmentList> getShipmentList() {
        return shipmentList;
    }

    public void setShipmentList(List<ShipmentGlobalDemandShipmentList> shipmentList) {
        this.shipmentList = shipmentList;
    }

    public List<ShipmentGlobalDemandDateSplit> getDateSplitList() {
        return dateSplitList;
    }

    public void setDateSplitList(List<ShipmentGlobalDemandDateSplit> dateSplitList) {
        this.dateSplitList = dateSplitList;
    }

    public List<ShipmentGlobalDemandCountrySplit> getCountrySplitList() {
        return countrySplitList;
    }

    public void setCountrySplitList(List<ShipmentGlobalDemandCountrySplit> countrySplitList) {
        this.countrySplitList = countrySplitList;
    }

    public List<ShipmentGlobalDemandCountryShipmentSplit> getCountryShipmentSplitList() {
        return countryShipmentSplitList;
    }

    public void setCountryShipmentSplitList(List<ShipmentGlobalDemandCountryShipmentSplit> countryShipmentSplitList) {
        this.countryShipmentSplitList = countryShipmentSplitList;
    }

}
