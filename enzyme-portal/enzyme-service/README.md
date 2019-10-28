# Enzyme-Service

Enzyme service for the [Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) project. The main function of this service is to enrich the Enzyme Portal data by pulling enzyme information from publicly available enzyme resources. The enzyme data providers featured in this service includes [Intenz](https://www.ebi.ac.uk/intenz/), [Reactome](https://reactome.org/) and more.

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
    private IntenzService intenzService;
    @Autowired
    private ReactomeService reactomeService;

    /**
     * Test of getIntenz method, of class IntenzService.
     */
    @Test
    public void testGetIntenz() {
        log.info("getIntenz");
        Collection<String> ecList = Arrays.asList("6.1.1.1", "2.1.1.1");
        List<Intenz> result = intenzService.getIntenz(ecList);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(2)));

    }

    @Test
    public void testFindPathwayById() {
        log.info("testFindPathwayById");
        String pathwayId = "R-HSA-4086398";
        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);
        assertNotNull(result);
        result.subscribe(r -> log.info("Result : " + r));
  

    }

```
## Authors

* [Joseph Sampson](https://www.linkedin.com/in/joseph-sampson-o-66399b30/)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) software released under the [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)
