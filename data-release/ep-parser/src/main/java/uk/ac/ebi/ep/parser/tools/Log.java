package uk.ac.ebi.ep.parser.tools;

import java.util.Objects;

/**
 *
 * @author Joseph
 */
public class Log {

    private String searchTerm;
    private Long count;

    public Log(String searchTerm, Long count) {
        this.searchTerm = searchTerm;
        this.count = count;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.searchTerm);
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
        final Log other = (Log) obj;
        return Objects.equals(this.searchTerm, other.searchTerm);
    }

    @Override
    public String toString() {
        return "Log{" + "searchTerm=" + searchTerm + ", count=" + count + '}';
    }

}
