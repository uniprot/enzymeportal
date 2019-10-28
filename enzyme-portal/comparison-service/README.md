# Comparison-Service

Comparison service for the [Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) project. The main function of this service is to compare enzymes based on protein functions, structures, reactions, pathways, small molecule compounds and diseases. 

## Built With

[Maven](https://maven.apache.org/)- Dependency Management


```bash
> mvn clean install
```

## Usage

Test use-cases to demonstrate how to use this module.
```java
import.*

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ComparisonServiceIT {

    @Autowired
    private ComparisonService comparisonService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(comparisonService).isNotNull();
    }

    /**
     * Test of getComparisonProteinModel method, of class ComparisonService.
     */
    @Test
    public void testGetComparisonProteinModel() {
        log.info("testGetComparisonProteinModel");
        String accession = "O76074";
        String proteinName = "cGMP-specific 3',5'-cyclic phosphodiesterase";
        ComparisonProteinModel result = comparisonService.getComparisonProteinModel(accession);
        assertNotNull(result);
        assertThat(result.getProteinName()).isEqualTo(proteinName);
        assertEquals(result.getProteinName(), proteinName);

    }

    /**
     * Test of getCompareEnzymeMolecule method, of class ComparisonService.
     */
    @Test
    public void testGetCompareEnzymeMolecule() {
        log.info("testGetComparisonProteinModel");
        String accession = "P51160";
        Molecule result = comparisonService.getCompareEnzymeMolecule(accession);
        assertNotNull(result);
        List<Compound> cofactors = result.getCofactors();

        assertThat(cofactors, hasSize(greaterThanOrEqualTo(1)));

        List<Compound> inhibitors = result.getInhibitors();

        assertThat(inhibitors, hasSize(greaterThanOrEqualTo(1)));
        List<Compound> activators = result.getActivators();

        assertThat(activators, hasSize(0));
    }

    /**
     * Test of getCompareEnzymeReactionPathay method, of class
     * ComparisonService.
     */
    @Test
    public void testGetCompareEnzymeReactionPathay() {
        log.info("testGetCompareEnzymeReactionPathay");
        String accession = "O76074";
        ReactionPathway result = comparisonService.getCompareEnzymeReactionPathay(accession);
        assertNotNull(result);
        assertNotNull(result.getReaction());
        assertNotNull(result.getPathways());

        assertThat(result.getReaction().getId()).isEqualTo("RHEA:16957");
        assertThat(result.getPathways(), hasSize(greaterThanOrEqualTo(1)));
        
        assertThat(result.getPathways().stream().findFirst().get()).isEqualToIgnoringCase("R-HSA-418457");
    }

    /**
     * Test of getCompareEnzymeDisease method, of class ComparisonService.
     */
    @Test
    public void testGetCompareEnzymeDisease() {
        log.info("testGetCompareEnzymeDisease");
        String accession = "P51160";
        String diseaseName = "Achromatopsia 5 (ACHM5)";
        List<Disease> result = comparisonService.getCompareEnzymeDisease(accession);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertEquals(diseaseName.trim(), result.stream().findFirst().get().getName().trim());
        assertThat(result.stream().findFirst().get().getEvidences()).isNotEmpty();
    }

}



```
## Authors

* [Joseph Sampson](https://www.linkedin.com/in/joseph-sampson-o-66399b30/)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) software released under the [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)

