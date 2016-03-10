/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.model;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author joseph
 */
@XmlRootElement(name = "database")
@XmlType(propOrder = {"name", "description", "release", "release_date", "entry_count", "entries"})
public class Database {

    private Entries entries;
    private String name;
    private String description;
    private String release;
    private LocalDate release_date;
    private long entry_count;

    @XmlElement(name = "entries")
    public Entries getEntries() {
        return entries;
    }

    public void setEntries(Entries entries) {
        this.entries = entries;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "release")
    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "release_date")
    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    @XmlElement(name = "entry_count")
    public long getEntry_count() {
        return entry_count;
    }

    public void setEntry_count(long entry_count) {
        this.entry_count = entry_count;
    }

}
