# Enzyme Portal Website

The [Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) web project.

## Built With

[Maven](https://maven.apache.org/)- Dependency Management


```bash
> mvn clean deploy
```

## Usage

Test use-cases to demonstrate how dependent services are used.
```java
import.*

    @Autowired
    private EntryPageService entryPageService;

    /**
     * Test of getDefaultEnzymeModel method, of class EntryPageService.
     */
    @Test
    public void testGetDefaultEnzymeModel() {
        log.info("testGetDefaultEnzymeModel");
        String accession = "O76074";
        String proteinName = "cGMP-specific 3',5'-cyclic phosphodiesterase";
        EnzymeModel result = entryPageService.getDefaultEnzymeModel(accession);
        assertNotNull(result);
        assertThat(result.getProteinName()).isEqualTo(proteinName);
        assertEquals(result.getProteinName(), proteinName);
        assertThat(result.getRelatedspecies()).isNotEmpty();
        assertThat(result.getRelatedspecies(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getEntryType()).isZero();
        assertThat(result.getSpecies().getCommonname()).isEqualToIgnoringCase("Human");
    }

    /**
     * Test of showEntryEnzymePage method, of class EntryPageService.
     */
    @Test
    public void testShowEntryEnzymePage() {
        log.info("testShowEntryEnzymePage");
        String accession = "O76074";
        EnzymeModel model = entryPageService.getDefaultEnzymeModel(accession);
        assertThat(model).isNotNull();
        EnzymeEntryPage result = entryPageService.showEntryEnzymePage(model).getEnzyme();
        assertNotNull(result);

        assertThat(result.getAccession()).isEqualTo(accession);

        assertThat(result.getSynonyms()).isNotEmpty();
        assertThat(result.getFunction()).isNotEmpty();
        assertThat(result.getEnzymeHierarchies(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getSequence()).isEqualTo(875);

    }

```
## Authors

* [Joseph Sampson](https://www.linkedin.com/in/joseph-sampson-o-66399b30/)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) software released under the [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)