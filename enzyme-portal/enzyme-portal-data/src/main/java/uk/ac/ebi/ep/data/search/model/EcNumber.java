/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.search.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author joseph
 */
public class EcNumber {

    protected boolean selected;
    private String ec;
    protected String family;
    protected List<String> families = new ArrayList<>();
    protected EnzymeFamily enzymeFamily;

    public EcNumber() {

    }

    public EcNumber(String ec) {
        this.ec = ec;
    }

    public EcNumber(String ec, boolean selected) {
        this.selected = selected;
        this.ec = ec;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getFamily() {
        family = computeFamily(getEc());
        families.add(family);
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public List<String> getFamilies() {
        getFamily();
        return families.stream().distinct().collect(Collectors.toList());
    }

    /**
     *
     * @param ec
     * @return enzyme family
     */
    protected String computeFamily(String ec) {

        if (ec.startsWith("1")) {

            return EnzymeFamily.OXIDOREDUCTASES.getName();
        }
        if (ec.startsWith("2")) {
            return EnzymeFamily.TRANSFERASES.getName();
        }
        if (ec.startsWith("3")) {
            return EnzymeFamily.HYDROLASES.getName();
        }
        if (ec.startsWith("4")) {
            return EnzymeFamily.LYASES.getName();
        }
        if (ec.startsWith("5")) {
            return EnzymeFamily.ISOMERASES.getName();
        }
        if (ec.startsWith("6")) {
            return EnzymeFamily.LIGASES.getName();
        }

        return "Invalid Ec Number";
    }

    protected String computeEc(String family) {

        if (family.equalsIgnoreCase(EnzymeFamily.OXIDOREDUCTASES.getName())) {
            //return "1";
            return EcClass.OXIDOREDUCTASES.getName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.TRANSFERASES.getName())) {
            return EcClass.TRANSFERASES.getName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.HYDROLASES.getName())) {
            return EcClass.HYDROLASES.getName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.LYASES.getName())) {
            return EcClass.LYASES.getName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.ISOMERASES.getName())) {
            return EcClass.ISOMERASES.getName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.LIGASES.getName())) {
            return EcClass.LIGASES.getName();
        }
        return "invalid enzyme family";
    }

    public enum EnzymeFamily {

        OXIDOREDUCTASES("Oxidoreductases"),
        TRANSFERASES("Transferases"),
        HYDROLASES("Hydrolases"),
        LYASES("Lyases"),
        ISOMERASES("Isomerases"),
        LIGASES("Ligases");

        private final String name;

        private EnzymeFamily(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public enum EcClass {

        OXIDOREDUCTASES("1"),
        TRANSFERASES("2"),
        HYDROLASES("3"),
        LYASES("4"),
        ISOMERASES("5"),
        LIGASES("6");

        private final String name;

        private EcClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String value() {
            return name();
        }

        public static EcClass fromValue(String v) {
            return valueOf(v);
        }

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.family);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EcNumber other = (EcNumber) obj;
        return Objects.equals(this.family, other.family);
    }

    @Override
    public String toString() {
        return "EcNumber{" + "ec=" + ec + ", family=" + getFamily() + ", families=" + getFamilies() + '}';
    }

}
