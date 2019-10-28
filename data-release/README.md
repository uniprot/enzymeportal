# Data Release

The Enzyme Portal integrates data from different enzyme resources. This repository contains various essential modules in the Enzyme Portal release pipeline. 
Key features includes ;
* Experimental evidence analysis.
* Log Analysis.
* Pdbe service.
* ChEMBL service for Inhibitors & Activators.
* ChEBI service for cofactors & reaction participants.
* Metabolight service for metabolites.
* Reaction service for Rhea & Kegg cross references
* Reactome service for pathways.
* XML Generator for data index service.
* Brenda service for kinetic parameters.

## Built With

[Maven](https://maven.apache.org/)- Dependency Management


```bash
> mvn clean install
```

## Usage

An overview on how to run the accompanied scripts.
```shell

# Experimental evidence
> sh eev.sh $db_config

# To run in LSF
> sh submitOneLFS.sh $db_config ./${script-name}
# ${script-name} includes the following ;
1. pdb.sh
2. chembl.sh
3. rheareactions.sh
4. reactant.sh
5. chebiCompounds.sh (chebi compounds & unique metabolite(metabolite.sh))
6. chebiCofactors.sh

# XML Generator
> sh submitLSF.sh ./enzyme-centric-xml-generator.sh $db_config
> sh submitLSF.sh ./protein-centric-xml-generator.sh $db_config

```
## Authors

* [Joseph Sampson](https://www.linkedin.com/in/joseph-sampson-o-66399b30/)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) software released under the [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)
