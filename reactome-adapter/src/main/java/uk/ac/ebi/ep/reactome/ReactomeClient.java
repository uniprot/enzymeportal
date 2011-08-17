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

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
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
import org.jdom.Document;
import org.jdom.input.DOMBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeClient {

//********************************* VARIABLES ********************************//
    private final String SERVICE_URL_NAME="http://www.reactome.org:8080/caBIOWebApp/services/caBIOService";
    private final Object[] EMPTY_ARG = new Object[]{};
    private Service caBIOService;
    private SOAPFactory soapFactory;
    


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static void createGeneratePathwayDiagramInSVGCall() {

    }
    public static void main(String[] args) {
        ReactomeClient reactomeClient = new ReactomeClient();
        try {
            reactomeClient.initReactomeService();
            //211000,74160
            //reactomeClient.testCaBIO();
            reactomeClient.testQueryPathwaysForReferenceEntities();
            //reactomeClient.generatePathwayDiagramInSVG();
            //reactomeClient.testBatchLoading();
            //reactomeClient.outputBioPAX();
            //reactomeClient.testLoadPathway();

           // reactomeClient.testReaction();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void outputBioPAX() throws Exception {
        Service service = new Service();
        Call call = (Call) service.createCall();
        String url="http://www.reactome.org:8080/caBIOWebApp/services/BioPAXExporter";
        call.setTargetEndpointAddress(url);
        call.setOperationName(new QName(url,
                                        "getBioPAXModel"));
        call.addParameter("arg1",
                          org.apache.axis.encoding.XMLType.XSD_LONG,
                          ParameterMode.IN);
        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
        String output = (String) call.invoke(new Object[]{new Long(69278L)});
        String fileName = "/Users/hongcao/Desktop/Pathways/MitoticCellCycle.owl";
        FileOutputStream fos = new FileOutputStream(fileName);
        PrintStream ps = new PrintStream(fos);
        ps.print(output);
        ps.close();
        fos.close();
    }
    public void parsePathways(Pathway[] pathways) throws Exception {
        //Call listPathwayParticipantsForId = createCall("listPathwayParticipantsForId");
            for (Pathway p : pathways) {
                //List<Event> eventList = p.getOrthologousEvent();
                List<Event> eventList = p.getHasComponent();
                if (eventList != null) {
                    for (Event event: eventList) {
                        if (event instanceof Reaction) {
                            long id = event.getId();
                            String name = event.toString();
                            System.out.println("event: " +name);
                        }
                    }
                }
                /*
                EventEntity[] participants = (EventEntity[]) listPathwayParticipantsForId.invoke(
                                                                 new Object[]{p.getId()});
                System.out.println(p.getName() + ": " + participants.length);
                // Print out participants
                for (EventEntity entity : participants)
                    System.out.printf("    %d: %s%n", entity.getId(), entity.getName());
                 *
                 */
            }

    }

    /**
     * Test the batch loading operations: queryByIds and queryByObjects.
     *
     */
    public void testBatchLoading() throws Exception {
        Call call = createCall("queryByIds");
        // call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
        List<Long> ids = new ArrayList<Long>();
        ids.add(new Long(176606)); //418553
        //ids.add(109581L);
        //ids.add(76031L);
        //ids.add(110654L);
        //ids.add(72571L);
        //ids.add(76182L);
        Object[] rtn = (Object[]) call.invoke(new Object[]{ids.toArray()});
        System.out.println("Output from queryByIds(): " + rtn.length);
        for (int i = 0; i < rtn.length; i++) {
            System.out.println(i);
            printOutput(rtn[i]);
        }
        // Test for queryByObjects()
        List<Object> objects = new ArrayList<Object>();
        Pathway pathway = new Pathway();
        //pathway.setId(new Long(418346));
        pathway.setId(new Long(176606));
        objects.add(pathway);
        Reaction reaction = new Reaction();
        //reaction.setId(new Long(418553));
        reaction.setId(new Long(176606));
        objects.add(reaction);
        call = createCall("queryByObjects");
        rtn = (Object[]) call.invoke(new Object[]{objects.toArray()});
        System.out.println("Output from queryByObjects(): " + rtn.length);
        for (int i = 0; i < rtn.length; i++) {
            System.out.println(i);
            printOutput(rtn[i]);
        }
        //displaySOAPResponseBody(call);
        //showCall(call);
    }

    /**
     * Test the operation for fully loading a Pathway object. A fully loaded Pathway has all its contained
     * Events loaded recursively.
     *
     */
    public void testLoadPathway() throws Exception {
        Call call = null;
        call = createCall("loadPathwayForId");
        // These two pathways don't work correctly: 163200 should be loaded first
        // but not. To much memory is used for this pathway
        Long[] ids = new Long[] {
            418346L,
            //163200L
        };
        for (Long id : ids) {
            Pathway pathway = (Pathway) call.invoke(new Object[]{id});
            printOutput(pathway);
        }
        //A contained Pathway in Apoptosis
        Pathway pathway = (Pathway) call.invoke(new Object[]{109581L});
        printOutput(pathway);
        // Apoptosis
        call = createCall("loadPathwayForObject");
        pathway = new Pathway();
        pathway.setId(109581);
        pathway = (Pathway) call.invoke(new Object[]{pathway});
        printOutput(pathway);
        call = createCall("queryById");
        pathway = (Pathway) call.invoke(new Object[]{109581l});
        call = createCall("loadPathwayForObject");
        pathway = (Pathway) call.invoke(new Object[]{pathway});
        //showCall(call);
    }

    public void generatePathwayDiagramInSVG() throws Exception {
        Pathway pathway = new Pathway();
        //pathway.setId(69278L);
        //pathway.setId(15869L);
        pathway.setId(418346);
        Call call = createCall("generatePathwayDiagramInSVG");
        String svg = (String) call.invoke(new Object[]{pathway});
        String fileName = "/Users/hongcao/Desktop/Pathways/MitoticCellCycle2.svg";
        //String fileName = "/Users/guanming/Desktop/NucleotideMetabolism.svg";
        FileOutputStream fos = new FileOutputStream(fileName);
        PrintStream ps = new PrintStream(fos);
        ps.print(svg);
        ps.close();
        fos.close();
    }
    public void testQueryPathwaysForReferenceEntities() throws Exception {
        long time1 = System.currentTimeMillis();
        //String[] identifiers = new String[3];
        //identifiers[0] = "Q9Y266";
        //identifiers[1] = "P17480";
        //identifiers[2] = "P20248";
        Call call = null;
        String[] identifiers = new String[1];
        identifiers[0] = "P61218";
        //identifiers[0] = "REACT_6763.1";
        call = createCall("queryPathwaysForReferenceIdentifiers");
        Pathway[] pathways = (Pathway[]) call.invoke(new Object[]{identifiers});
        this.parsePathways(pathways);
        long time2 = System.currentTimeMillis();
        System.out.printf("Pathway for a list of identifiers: %d (%d)%n",
                pathways.length, (time2 - time1));

        //getEvents(pathways);
        outputArray(pathways);
    }

    public void getEvents(Pathway[] pathways) throws Exception {
        Call listPathwayParticipantsForId = createCall("listPathwayParticipantsForId");

            for (Pathway p : pathways) {
                /*
                //List<Event> eventList = p.getOrthologousEvent();
                List<Event> eventList = p.getHasComponent();
                List<Integer> idList = new ArrayList<Integer>();
                if (eventList != null) {
                    for (Event event: eventList) {
                        Long id = event.getId();
                        idList.add(id.intValue());
                        String name = event.toString();
                        System.out.println("event: " +name);
                    }
                }
                this.testCaBIO(idList);
         *
         */
                
                EventEntity[] participants = (EventEntity[]) listPathwayParticipantsForId.invoke(
                                                                 new Object[]{p.getId()});
                System.out.println(p.getName() + ": " + participants.length);
                // Print out participants
                for (EventEntity entity : participants)
                    System.out.printf("    %d: %s%n", entity.getId(), entity.getName());
               
            }


    }
    private Object[] callQuery(String para1,
                               String para2,
                               Object para3,
                               Object[] paras,
                               Call call) throws Exception {
        paras[0] = para1;
        paras[1] = para2;
        paras[2] = para3;
        return (Object[]) call.invoke(paras);
    }


    public void testReaction() throws Exception {
        Call call = createCall("listByQuery");
        Object[] paras = new Object[3];
        /*
        Object[] rtn = callQuery(Taxon.class.getName(),
                "scientificName",
                "homo sapiens",
                paras,
                call);
        // There should be one instance
        Taxon humanTaxon = (Taxon) rtn[0];
        System.out.println("Human Taxon: " + humanTaxon);
         *
         */
        Object[] rtn = callQuery(Reaction.class.getName(),
                "id",
                "176606",
                paras,
                call);
        System.out.printf("Total human pathways: %d%n", rtn.length);
        for (int i = 0; i < rtn.length; i++) {
            Reaction reaction = (Reaction) rtn[i];
            System.out.printf("%d -> %s%n", reaction.getId(), reaction.getName());
        }

    }

    private void outputArray(Object[] objects) throws Exception {
        for (Object obj : objects) {
            printOutput(obj);
        }
    }
    public void initReactomeService() throws SOAPException {
        BasicConfigurator.configure();
        soapFactory = SOAPFactory.newInstance();
    }

    public void testCaBIO(List<Integer> dbIds) throws Exception {
        Call call = createCall("queryById");
        // Test cases
        /*
        int[] dbIds = new int[]{
                76131, // ReferenceMolecule
                30390, // DatabaseIdentifier
                156678, // Reaction: Activation of CDC25C
                69044, // LiteratureReference
                76182, // Complex: Calcium:calmodulin [nucleoplasm]
                //73644, // Use Reaction to test Regulation
                114263, // Another Reaction
                72782, // Regulation: 'eIF5' is required for 'eIF2:GTP is hydrolyzed, eIFs are released'
                71672, // Modification: L-selenocysteinyl group at 47 of UniProt:P07203 Glutathione
                       // peroxidase 1 (EC 1.11.1.9) (GSHPx-1) (Cellular glutathione peroxidase)
                71673, // Entity has the above modification: glutathione peroxidase monomer [cytosol]
                109581, // Pathway: Apoptosis
                // This instance is
                139943, // EquivalentEventSet: PLC-mediated hydrolysis
                163362, // ConceptualEvent: Dephosphorylation of phosphoChREBP by PP2A
                //83932, // nucleoside transporter activity of solute carrier family 29 (nucleoside
                       // transporters), member 1 [plasma membrane]
                74016, // SimpleEntity: Calcium (cytosol)
                68640, // DefinedSet: E2F transcription factors [nucleoplasm]
                165962, // CandidateSet: TSC2 [cytosol]
                // deleted in release 21
                //109842, // EntityWithAccessionedSequence: phospho-ERK-1 [cytosol]
        };
         * *
         */
        for (int dbId : dbIds) {
            long time1 = System.currentTimeMillis();
            Object obj = call.invoke(new Object[]{new Long(dbId)});
            long time2 = System.currentTimeMillis();
            System.out.println("Time: " + (time2 - time1));
            printOutput(obj);
        }
    }
    public void testCaBIO() throws Exception {
        Call call = createCall("queryById");
        // Test cases

        int[] dbIds = new int[]{176606
            /*
                76131, // ReferenceMolecule
                30390, // DatabaseIdentifier
                156678, // Reaction: Activation of CDC25C
                69044, // LiteratureReference
                76182, // Complex: Calcium:calmodulin [nucleoplasm]
                //73644, // Use Reaction to test Regulation
                114263, // Another Reaction
                72782, // Regulation: 'eIF5' is required for 'eIF2:GTP is hydrolyzed, eIFs are released'
                71672, // Modification: L-selenocysteinyl group at 47 of UniProt:P07203 Glutathione
                       // peroxidase 1 (EC 1.11.1.9) (GSHPx-1) (Cellular glutathione peroxidase)
                71673, // Entity has the above modification: glutathione peroxidase monomer [cytosol]
                109581, // Pathway: Apoptosis
                // This instance is
                139943, // EquivalentEventSet: PLC-mediated hydrolysis
                163362, // ConceptualEvent: Dephosphorylation of phosphoChREBP by PP2A
                //83932, // nucleoside transporter activity of solute carrier family 29 (nucleoside
                       // transporters), member 1 [plasma membrane]
                74016, // SimpleEntity: Calcium (cytosol)
                68640, // DefinedSet: E2F transcription factors [nucleoplasm]
                165962, // CandidateSet: TSC2 [cytosol]
                // deleted in release 21
                //109842, // EntityWithAccessionedSequence: phospho-ERK-1 [cytosol]
             * 
             */
        };

        for (int dbId : dbIds) {
            long time1 = System.currentTimeMillis();
            Object obj = call.invoke(new Object[]{new Long(dbId)});
            long time2 = System.currentTimeMillis();
            System.out.println("Time: " + (time2 - time1));
            printOutput(obj);
        }
    }

    private void printOutput(Object obj) throws Exception {
        System.out.printf("%s -> %s%n", obj.getClass(), obj.toString());
        Method[] methods = obj.getClass().getMethods();
        // Get all getMethods
        for (Method m : methods) {
            String mName = m.getName();
            if (mName.startsWith("get")) {
                String propName = lowerFirst(mName.substring(3));
                System.out.printf("\t%s: %s%n", propName, m.invoke(obj, EMPTY_ARG));
            }
        }
    }

    private String lowerFirst(String propName) {
        return propName.substring(0, 1).toLowerCase() + propName.substring(1);
    }

    private Call createCall(String callName) throws Exception {
        if (caBIOService == null) {
            caBIOService = new Service(SERVICE_URL_NAME + "?wsdl",
                                       new QName(SERVICE_URL_NAME,
                                                 "CaBioDomainWSEndPointService"));
        }
        String portName = "caBIOService";
        Call call = (Call) caBIOService.createCall(new QName(SERVICE_URL_NAME, portName),
                                                   callName);
        registerTypeMappings(call);
        return call;
    }

    private void registerTypeMappings(Call call) {
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
