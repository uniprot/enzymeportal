# Index-Service

Index service for the [Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) project. This is a wrapper for the [EBI Search service](https://www.ebi.ac.uk/ebisearch/swagger.ebi). This service provides endpoints for enzyme-centric & protein-centric views respectively.

## Built With

[Maven](https://maven.apache.org/)- Dependency Management


```bash
> mvn clean install
```

## Usage

Test use-cases to demonstrate how to use this module.
```java
import.*


    @Test
    public void testShouldReceiveJsonAsString() {
        log.info("testShouldReceiveJsonAsString");
        String content = "{\"suggestions\":[{\"suggestion\":\"kinase\"},{\"suggestion\":\"kinase kinases\"}]}";

        String searchTerm = "kinas";

        Mono<String> result = this.webClient.get()
                .uri(indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl() + AUTO_COMPLETE_URL, searchTerm)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);

        StepVerifier.create(result)
                .expectNext(content)
                .expectComplete()
                .verify(Duration.ofSeconds(30));

        assertThat(result.block(), containsString(content));

    }

    @Test
    public void testGetEnzymeEntries() {
        String ec = "3.1.4.35";
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), ec);
        
        QueryBuilder queryBuilder = defaultQueryBuilder(query);
        
        Mono<EnzymeEntry> entry = indexService.getEnzymePageEntry(queryBuilder);
        
        assertNotNull(entry);
        entry.subscribe(e -> System.out.println("Enzyme Page  " + e.getEnzymeName() + " Associated Protein : " + e.getProteinGroupEntry()));
        assertThat(entry.blockOptional().orElse(new EnzymeEntry()).getProteinGroupEntry(), hasSize(greaterThanOrEqualTo(1)));
        
    }

    @Test
    public void testSearchAssociatedProteins_with_ec() {
        log.info("testSearchAssociatedProteins_with_ec");

        String term = "sildenafil";
        String ec = "3.1.4.35";

        String searchTerm = String.format("%s AND %s%s", term, IndexQueryType.EC.getQueryType(), ec);

        QueryBuilder queryBuilder = defaultQueryBuilder(searchTerm);

        ProteinGroupSearchResult result = proteinCentricService.searchForProteins(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

```
## Authors

* [Joseph Sampson](https://www.linkedin.com/in/joseph-sampson-o-66399b30/)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) software released under the [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)

