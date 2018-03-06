
package uk.ac.ebi.ep.xml.transformer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author joseph
 */
public class EcNumber implements Comparable<EcNumber>, Serializable {

    private static final long serialVersionUID = 1L;
    protected boolean selected;
    private Integer ec;
    protected String family;
    protected transient List<String> families = new ArrayList<>();
    protected EnzymeFamily enzymeFamily;

    public EcNumber() {

    }


    public EcNumber(Integer ec) {
        this.ec = ec;
        selected = Boolean.FALSE;
    }

    public EcNumber(Integer ec, boolean selected) {
        this.selected = selected;
        this.ec = ec;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getEc() {
        return ec;
    }

    public void setEc(Integer ec) {
        this.ec = ec;
    }

    public String getFamily() {
        family = computeEcToFamilyName(getEc());
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

    public String computeEcToFamilyName(int ec) {

        if (ec == 1) {

            return EnzymeFamily.OXIDOREDUCTASES.getName();
        }
        if (ec == 2) {
            return EnzymeFamily.TRANSFERASES.getName();
        }
        if (ec == 3) {
            return EnzymeFamily.HYDROLASES.getName();
        }
        if (ec == 4) {
            return EnzymeFamily.LYASES.getName();
        }
        if (ec == 5) {
            return EnzymeFamily.ISOMERASES.getName();
        }
        if (ec == 6) {
            return EnzymeFamily.LIGASES.getName();
        }

        return "Invalid Ec Number";
    }

    public Integer computeFamilyNameToEc(String family) {

        if (family.equalsIgnoreCase(EnzymeFamily.OXIDOREDUCTASES.getName())) {
            //return 1;
            return EnzymeClass.OXIDOREDUCTASES.getFamilyName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.TRANSFERASES.getName())) {
            return EnzymeClass.TRANSFERASES.getFamilyName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.HYDROLASES.getName())) {
            return EnzymeClass.HYDROLASES.getFamilyName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.LYASES.getName())) {
            return EnzymeClass.LYASES.getFamilyName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.ISOMERASES.getName())) {
            return EnzymeClass.ISOMERASES.getFamilyName();
        }
        if (family.equalsIgnoreCase(EnzymeFamily.LIGASES.getName())) {
            return EnzymeClass.LIGASES.getFamilyName();
        }
        return 0;
    }

    /**
     *
     * @param ec
     * @return enzyme family
     */
    public String computeFamily(String ec) {

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

    public String computeEc(String family) {

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

    @Override
    public int compareTo(EcNumber o) {
      return  ec.compareTo(o.getEc());
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

    public enum EnzymeClass {

        OXIDOREDUCTASES(1),
        TRANSFERASES(2),
        HYDROLASES(3),
        LYASES(4),
        ISOMERASES(5),
        LIGASES(6);

        private final int familyName;

        private EnzymeClass(int name) {
            this.familyName = name;
        }

        public int getFamilyName() {
            return familyName;
        }

        public String value() {
            return name();
        }

        public static EnzymeClass fromValue(String v) {
            return valueOf(v);
        }

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.ec);
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
        return Objects.equals(this.ec, other.ec);
    }

    @Override
    public String toString() {
        return "EcNumber{" + "ec=" + ec + ", family=" + getFamily() + ", families=" + getFamilies() + '}';
    }
    
}
