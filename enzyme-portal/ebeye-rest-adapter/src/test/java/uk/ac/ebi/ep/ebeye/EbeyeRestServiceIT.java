package uk.ac.ebi.ep.ebeye;

/**
 * Tests the behaviour of the {@link EbeyeRestService}.
 *
 * @author joseph
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {EbeyeConfig.class})
public class EbeyeRestServiceIT {
    //@Autowired
    //private EbeyeRestService ebeyeRestService;
//
//    //@Test
//    @Ignore(value = "EBeye Autocomplete search was done.")
//    public void partial_term_phos_sent_to_Ebeye_autocomplete_search_returns_valid_suggestions() throws Exception {
//        String searchTerm = "phos";
//
//        List<Suggestion> result =
//                ebeyeRestService.autocompleteSearch(searchTerm).stream().sorted().collect(Collectors.toList());
//
//        assertThat(result, hasSize(greaterThan(0)));
//
//        result.forEach(suggestion -> assertThat(suggestion.getSuggestedKeyword(), startsWith(searchTerm)));
//    }
//
//    @Test
//    public void query_with_limit_of_5_sent_to_ebeyeSearch_returns_at_most_5_accessions() throws Exception {
//        String query = "kinase";
//        int limit = 5;
//
//        List<String> actualAccs = ebeyeRestService.queryForUniqueAccessions(query, limit);
//        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));
//    }
//
//    @Test
//    public void query_with_limit_of_800_sent_to_ebeyeSearch_returns_at_most_5_accessions() throws Exception {
//        String query = "kinase";
//        int limit = 800;
//
//        List<String> actualAccs = ebeyeRestService.queryForUniqueAccessions(query, limit);
//        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));
//    }
}