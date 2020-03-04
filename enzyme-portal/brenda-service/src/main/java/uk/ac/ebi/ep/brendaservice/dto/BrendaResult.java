package uk.ac.ebi.ep.brendaservice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joseph
 */
public class BrendaResult implements Serializable {

    private List<Brenda> brenda;
    private List<Ph> ph;
    private List<Temperature> temperature;
    private boolean km;

    public BrendaResult(boolean hasKm) {
        this.km = hasKm;
    }

    public BrendaResult(List<Brenda> brenda, boolean hasKm) {
        this.brenda = brenda;
        this.km = hasKm;
    }

    public BrendaResult(List<Brenda> brenda, List<Ph> ph, List<Temperature> temperature, boolean hasKm) {
        this.brenda = brenda;
        this.ph = ph;
        this.temperature = temperature;
        this.km = hasKm;
    }

    public List<Brenda> getBrenda() {
        if (brenda == null) {
            return new ArrayList<>();
        }
        return brenda;
    }

    public void setBrenda(List<Brenda> brenda) {
        this.brenda = brenda;
    }

    public List<Ph> getPh() {
        if (ph == null) {
            return new ArrayList<>();
        }
        return ph;
    }

    public void setPh(List<Ph> ph) {
        this.ph = ph;
    }

    public List<Temperature> getTemperature() {
        if (temperature == null) {
            return new ArrayList<>();
        }
        return temperature;
    }

    public void setTemperature(List<Temperature> temperature) {
        this.temperature = temperature;
    }

    public boolean isKm() {
        return km;
    }

    public void setKm(boolean km) {
        this.km = km;
    }

}
