package uk.ac.ebi.ep.reactome;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import junit.framework.TestCase;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.log4j.BasicConfigurator;
import org.reactome.cabig.domain.*;
import org.reactome.servlet.InstanceNotFoundException;
import org.reactome.servlet.ReactomeRemoteException;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ServiceInitializer {

//********************************* VARIABLES ********************************//
    private static final String SERVICE_URL_NAME="http://www.reactome.org:8080/caBIOWebApp/services/caBIOService";    
    private static Service caBIOService;
    private static SOAPFactory soapFactory;

    private static Call generatePathwayDiagramInSVGCall = createCall("generatePathwayDiagramInSVG");
    private static Call queryPathwaysForReferenceIdentifiersCall = createCall("queryPathwaysForReferenceIdentifiers");

    public ServiceInitializer() {
    }
//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

     public Call generatePathwayDiagramInSVGNewCall() {
        return generatePathwayDiagramInSVGCall;
     }

     public Call queryPathwaysForReferenceIdentifiersNewCall() {
        return queryPathwaysForReferenceIdentifiersCall;
     }



    private static Call createCall(String callName) {
        BasicConfigurator.configure();
        try {
            soapFactory = SOAPFactory.newInstance();
        } catch (SOAPException ex) {
            Logger.getLogger(ServiceInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            caBIOService = new Service(SERVICE_URL_NAME + "?wsdl", new QName(SERVICE_URL_NAME, "CaBioDomainWSEndPointService"));
        } catch (ServiceException ex) {
            Logger.getLogger(ServiceInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        String portName = "caBIOService";
        Call call = null;
        try {
            call = (Call) caBIOService.createCall(new QName(SERVICE_URL_NAME, portName),
                                                   callName);
        } catch (ServiceException ex) {
            Logger.getLogger(ServiceInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        registerTypeMappings(call);
        return call;
    }

   private static void registerTypeMappings(Call call) {
        QName instanceNotFoundModel = new QName("http://www.reactome.org/caBIOWebApp/schema",
                                                "InstanceNotFoundException");
        call.registerTypeMapping(InstanceNotFoundException.class, instanceNotFoundModel,
                new BeanSerializerFactory(InstanceNotFoundException.class, instanceNotFoundModel),
                new BeanDeserializerFactory(InstanceNotFoundException.class, instanceNotFoundModel));
        QName reactomeAxisFaultModel = new QName("http://www.reactome.org/caBIOWebApp/schema",
                                                 "ReactomeRemoteException");
        call.registerTypeMapping(ReactomeRemoteException.class, reactomeAxisFaultModel,
                new BeanSerializerFactory(ReactomeRemoteException.class, reactomeAxisFaultModel),
                new BeanDeserializerFactory(ReactomeRemoteException.class, reactomeAxisFaultModel));
        QName CatalystActivityModel= new QName("http://www.reactome.org/caBIOWebApp/schema",
                                               "CatalystActivity");
        call.registerTypeMapping(CatalystActivity.class, CatalystActivityModel,
              new BeanSerializerFactory(CatalystActivity.class, CatalystActivityModel),
              new BeanDeserializerFactory(CatalystActivity.class, CatalystActivityModel));
        QName ComplexModel= new QName("http://www.reactome.org/caBIOWebApp/schema",
                                      "Complex");
        call.registerTypeMapping(Complex.class, ComplexModel,
              new BeanSerializerFactory(Complex.class, ComplexModel),
              new BeanDeserializerFactory(Complex.class, ComplexModel));
        QName DatabaseCrossReferenceModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "DatabaseCrossReference");
        call.registerTypeMapping(DatabaseCrossReference.class, DatabaseCrossReferenceModel,
              new BeanSerializerFactory(DatabaseCrossReference.class, DatabaseCrossReferenceModel),
              new BeanDeserializerFactory(DatabaseCrossReference.class, DatabaseCrossReferenceModel));
        QName EventModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Event");
        call.registerTypeMapping(Event.class, EventModel,
              new BeanSerializerFactory(Event.class, EventModel),
              new BeanDeserializerFactory(Event.class, EventModel));
        QName EventEntityModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "EventEntity");
        call.registerTypeMapping(EventEntity.class, EventEntityModel,
              new BeanSerializerFactory(EventEntity.class, EventEntityModel),
              new BeanDeserializerFactory(EventEntity.class, EventEntityModel));
        QName EventEntitySetModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "EventEntitySet");
        call.registerTypeMapping(EventEntitySet.class, EventEntitySetModel,
              new BeanSerializerFactory(EventEntitySet.class, EventEntitySetModel),
              new BeanDeserializerFactory(EventEntitySet.class, EventEntitySetModel));
        QName GeneOntologyModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "GeneOntology");
        call.registerTypeMapping(GeneOntology.class, GeneOntologyModel,
              new BeanSerializerFactory(GeneOntology.class, GeneOntologyModel),
              new BeanDeserializerFactory(GeneOntology.class, GeneOntologyModel));
        QName GeneOntologyRelationshipModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "GeneOntologyRelationship");
        call.registerTypeMapping(GeneOntologyRelationship.class, GeneOntologyRelationshipModel,
              new BeanSerializerFactory(GeneOntologyRelationship.class, GeneOntologyRelationshipModel),
              new BeanDeserializerFactory(GeneOntologyRelationship.class, GeneOntologyRelationshipModel));
        QName GenomeEncodedEntityModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "GenomeEncodedEntity");
        call.registerTypeMapping(GenomeEncodedEntity.class, GenomeEncodedEntityModel,
              new BeanSerializerFactory(GenomeEncodedEntity.class, GenomeEncodedEntityModel),
              new BeanDeserializerFactory(GenomeEncodedEntity.class, GenomeEncodedEntityModel));
        QName ModifiedResidueModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "ModifiedResidue");
        call.registerTypeMapping(ModifiedResidue.class, ModifiedResidueModel,
              new BeanSerializerFactory(ModifiedResidue.class, ModifiedResidueModel),
              new BeanDeserializerFactory(ModifiedResidue.class, ModifiedResidueModel));
        QName PathwayModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Pathway");
        call.registerTypeMapping(Pathway.class, PathwayModel,
              new BeanSerializerFactory(Pathway.class, PathwayModel),
              new BeanDeserializerFactory(Pathway.class, PathwayModel));
        QName PolymerModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Polymer");
        call.registerTypeMapping(Polymer.class, PolymerModel,
              new BeanSerializerFactory(Polymer.class, PolymerModel),
              new BeanDeserializerFactory(Polymer.class, PolymerModel));
        QName PublicationSourceModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "PublicationSource");
        call.registerTypeMapping(PublicationSource.class, PublicationSourceModel,
              new BeanSerializerFactory(PublicationSource.class, PublicationSourceModel),
              new BeanDeserializerFactory(PublicationSource.class, PublicationSourceModel));
        QName ReactionModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Reaction");
        call.registerTypeMapping(Reaction.class, ReactionModel,
              new BeanSerializerFactory(Reaction.class, ReactionModel),
              new BeanDeserializerFactory(Reaction.class, ReactionModel));
        QName ReferenceChemicalModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceChemical");
        call.registerTypeMapping(ReferenceChemical.class, ReferenceChemicalModel,
              new BeanSerializerFactory(ReferenceChemical.class, ReferenceChemicalModel),
              new BeanDeserializerFactory(ReferenceChemical.class, ReferenceChemicalModel));
        QName ReferenceEntityModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceEntity");
        call.registerTypeMapping(ReferenceEntity.class, ReferenceEntityModel,
              new BeanSerializerFactory(ReferenceEntity.class, ReferenceEntityModel),
              new BeanDeserializerFactory(ReferenceEntity.class, ReferenceEntityModel));
        QName ReferenceGeneModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceGene");
        call.registerTypeMapping(ReferenceGene.class, ReferenceGeneModel,
              new BeanSerializerFactory(ReferenceGene.class, ReferenceGeneModel),
              new BeanDeserializerFactory(ReferenceGene.class, ReferenceGeneModel));
        QName ReferenceProteinModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceProtein");
        call.registerTypeMapping(ReferenceProtein.class, ReferenceProteinModel,
              new BeanSerializerFactory(ReferenceProtein.class, ReferenceProteinModel),
              new BeanDeserializerFactory(ReferenceProtein.class, ReferenceProteinModel));
        QName ReferenceRNAModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceRNA");
        call.registerTypeMapping(ReferenceRNA.class, ReferenceRNAModel,
              new BeanSerializerFactory(ReferenceRNA.class, ReferenceRNAModel),
              new BeanDeserializerFactory(ReferenceRNA.class, ReferenceRNAModel));
        QName ReferenceSequenceModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceSequence");
        call.registerTypeMapping(ReferenceSequence.class, ReferenceSequenceModel,
              new BeanSerializerFactory(ReferenceSequence.class, ReferenceSequenceModel),
              new BeanDeserializerFactory(ReferenceSequence.class, ReferenceSequenceModel));
        QName RegulationModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Regulation");
        call.registerTypeMapping(Regulation.class, RegulationModel,
              new BeanSerializerFactory(Regulation.class, RegulationModel),
              new BeanDeserializerFactory(Regulation.class, RegulationModel));
        QName RegulationTypeModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "RegulationType");
        call.registerTypeMapping(RegulationType.class, RegulationTypeModel,
              new EnumSerializerFactory(RegulationType.class, RegulationTypeModel),
              new EnumDeserializerFactory(RegulationType.class, RegulationTypeModel));
        QName RegulatorModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Regulator");
        call.registerTypeMapping(Regulator.class, RegulatorModel,
              new BeanSerializerFactory(Regulator.class, RegulatorModel),
              new BeanDeserializerFactory(Regulator.class, RegulatorModel));
        QName SmallMoleculeEntityModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "SmallMoleculeEntity");
        call.registerTypeMapping(SmallMoleculeEntity.class, SmallMoleculeEntityModel,
              new BeanSerializerFactory(SmallMoleculeEntity.class, SmallMoleculeEntityModel),
              new BeanDeserializerFactory(SmallMoleculeEntity.class, SmallMoleculeEntityModel));
        QName SummationModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Summation");
        call.registerTypeMapping(Summation.class, SummationModel,
              new BeanSerializerFactory(Summation.class, SummationModel),
              new BeanDeserializerFactory(Summation.class, SummationModel));
        QName TaxonModel= new QName("http://www.reactome.org/caBIOWebApp/schema", "Taxon");
        call.registerTypeMapping(Taxon.class, TaxonModel,
              new BeanSerializerFactory(Taxon.class, TaxonModel),
              new BeanDeserializerFactory(Taxon.class, TaxonModel));
        // {http://localhost:8080/caBIOWebApp/services/caBIOService}ArrayOf_xsd_anyType
        QName arrayModel = new QName("http://www.reactome.org/caBIOWebApp/services/caBIOService", "ArrayOf_xsd_anyType");
        QName componentModel = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
        call.registerTypeMapping(Object[].class, arrayModel,
                new ArraySerializerFactory(Object.class, componentModel),
                new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://localhost:8080/caBIOWebApp/services/caBIOService", "queryByIds");
//        componentModel = new QName("http://www.w3.org/2001/XMLSchema", "long");
//        call.registerTypeMapping(Long[].class, arrayModel,
//                new ArraySerializerFactory(Long.class, componentModel),
//                new ArrayDeserializerFactory(componentModel));
        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfAnyType");
        componentModel = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
        call.registerTypeMapping(Object.class, arrayModel,
                new ArraySerializerFactory(Object.class, componentModel),
                new ArrayDeserializerFactory(componentModel));

//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfPathway");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Pathway");
//        call.registerTypeMapping(Pathway[].class, arrayModel,
//                new ArraySerializerFactory(ReferenceEntity.class, componentModel),
//                new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfEventEntity");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "EventEntity");
//        call.registerTypeMapping(EventEntity[].class, arrayModel,
//                new ArraySerializerFactory(EventEntity.class, componentModel),
//                new ArrayDeserializerFactory(componentModel));
//
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfCatalystActivity");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "CatalystActivity");
//        call.registerTypeMapping(CatalystActivity[].class, arrayModel,
//            new ArraySerializerFactory(CatalystActivity.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfComplex");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Complex");
//        call.registerTypeMapping(Complex[].class, arrayModel,
//            new ArraySerializerFactory(Complex.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfDatabaseCrossReference");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "DatabaseCrossReference");
//        call.registerTypeMapping(DatabaseCrossReference[].class, arrayModel,
//            new ArraySerializerFactory(DatabaseCrossReference.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfEvent");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Event");
//        call.registerTypeMapping(Event[].class, arrayModel,
//            new ArraySerializerFactory(Event.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfEventEntity");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "EventEntity");
//        call.registerTypeMapping(EventEntity[].class, arrayModel,
//            new ArraySerializerFactory(EventEntity.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfEventEntitySet");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "EventEntitySet");
//        call.registerTypeMapping(EventEntitySet[].class, arrayModel,
//            new ArraySerializerFactory(EventEntitySet.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfEventSet");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "EventSet");
//        call.registerTypeMapping(EventSet[].class, arrayModel,
//            new ArraySerializerFactory(EventSet.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfGeneOntology");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "GeneOntology");
//        call.registerTypeMapping(GeneOntology[].class, arrayModel,
//            new ArraySerializerFactory(GeneOntology.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfGeneOntologyRelationship");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "GeneOntologyRelationship");
//        call.registerTypeMapping(GeneOntologyRelationship[].class, arrayModel,
//            new ArraySerializerFactory(GeneOntologyRelationship.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfGenomeEncodedEntity");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "GenomeEncodedEntity");
//        call.registerTypeMapping(GenomeEncodedEntity[].class, arrayModel,
//            new ArraySerializerFactory(GenomeEncodedEntity.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfModifiedResidue");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ModifiedResidue");
//        call.registerTypeMapping(ModifiedResidue[].class, arrayModel,
//            new ArraySerializerFactory(ModifiedResidue.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfPathway");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Pathway");
//        call.registerTypeMapping(Pathway[].class, arrayModel,
//            new ArraySerializerFactory(Pathway.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfPolymer");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Polymer");
//        call.registerTypeMapping(Polymer[].class, arrayModel,
//            new ArraySerializerFactory(Polymer.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfPublicationSource");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "PublicationSource");
//        call.registerTypeMapping(PublicationSource[].class, arrayModel,
//            new ArraySerializerFactory(PublicationSource.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfReaction");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Reaction");
//        call.registerTypeMapping(Reaction[].class, arrayModel,
//            new ArraySerializerFactory(Reaction.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfReferenceChemical");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceChemical");
//        call.registerTypeMapping(ReferenceChemical[].class, arrayModel,
//            new ArraySerializerFactory(ReferenceChemical.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfReferenceEntity");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceEntity");
//        call.registerTypeMapping(ReferenceEntity[].class, arrayModel,
//            new ArraySerializerFactory(ReferenceEntity.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfReferenceGene");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceGene");
//        call.registerTypeMapping(ReferenceGene[].class, arrayModel,
//            new ArraySerializerFactory(ReferenceGene.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfReferenceProtein");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceProtein");
//        call.registerTypeMapping(ReferenceProtein[].class, arrayModel,
//            new ArraySerializerFactory(ReferenceProtein.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfReferenceRNA");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceRNA");
//        call.registerTypeMapping(ReferenceRNA[].class, arrayModel,
//            new ArraySerializerFactory(ReferenceRNA.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfReferenceSequence");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ReferenceSequence");
//        call.registerTypeMapping(ReferenceSequence[].class, arrayModel,
//            new ArraySerializerFactory(ReferenceSequence.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfRegulation");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Regulation");
//        call.registerTypeMapping(Regulation[].class, arrayModel,
//            new ArraySerializerFactory(Regulation.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfRegulationType");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "RegulationType");
//        call.registerTypeMapping(RegulationType[].class, arrayModel,
//            new ArraySerializerFactory(RegulationType.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfRegulator");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Regulator");
//        call.registerTypeMapping(Regulator[].class, arrayModel,
//            new ArraySerializerFactory(Regulator.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfSmallMoleculeEntity");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "SmallMoleculeEntity");
//        call.registerTypeMapping(SmallMoleculeEntity[].class, arrayModel,
//            new ArraySerializerFactory(SmallMoleculeEntity.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfSummation");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Summation");
//        call.registerTypeMapping(Summation[].class, arrayModel,
//            new ArraySerializerFactory(Summation.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
//        arrayModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "ArrayOfTaxon");
//        componentModel = new QName("http://www.reactome.org/caBIOWebApp/schema", "Taxon");
//        call.registerTypeMapping(Taxon[].class, arrayModel,
//            new ArraySerializerFactory(Taxon.class, componentModel),
//            new ArrayDeserializerFactory(componentModel));
    }

}
