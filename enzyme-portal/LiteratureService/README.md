# LiteratureService

Rest client for the Europe PMC service for the [Enzyme Portal Website](https://www.ebi.ac.uk/enzymeportal/).

## Built With

[Maven](https://maven.apache.org/)- Dependency Management


```bash
> mvn clean install
```

## Usage

Test use-cases to demonstrate how to use this module.
```java
import.*

    @Autowired
    private LiteratureService literatureService;
    @Autowired
    private PmcRestService pmcRestService;
    @Test
    public void testFindPublicationsByKeyword() {

        String keyword = "cancer";
        int expectedResultSize = 2;

        Optional<EuropePMC> result = pmcRestService.findPublicationsByKeyword(keyword);

        assertNotNull(result);
        assertThat(result.get().getResultList().getResult(), hasSize(greaterThan(expectedResultSize)));
        assertThat(result.get().getResultList().getResult(), hasSize(greaterThanOrEqualTo(expectedResultSize)));

    }

    @Test
    public void testGetCitationsByAccession() {

        String accession = "P42346";
        int limit = 20;

        List<LabelledCitation> result = literatureService.getCitationsByAccession(accession, limit);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThan(1)));
        assertThat(result, hasSize(lessThanOrEqualTo(limit)));

    }


```
## Authors

* [Joseph Sampson](https://www.linkedin.com/in/joseph-sampson-o-66399b30/)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) software released under the [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)
