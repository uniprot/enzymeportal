package uk.ac.ebi.ep.xml.main;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.xml.config.ProteinGroupsBatchConfig;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinGroupXmlGenerator {

    private ProteinGroupXmlGenerator() {
    }

    public static void main(String[] args) throws Exception {
        String profile;
        
        long heapSize = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
    

        //Preconditions.checkArgument(args.length != 1, "Please provide required parameters: \n\t0 - profile name");
        profile = args[0];

        proteinGroupBatch(profile);
    }

    private static void validateXml(ApplicationContext context) {
        Environment env = context.getEnvironment();

        String xmlFile = env.getProperty("ep.protein.centric.xml.dir");
        String[] ebeyeXSDs = env.getProperty("ep.ebeye.xsd").split(",");

        EnzymePortalXmlValidator.validateXml(xmlFile, ebeyeXSDs);
    }

    public static void proteinGroupBatch(String profile) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerShutdownHook();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(DataConfig.class, ProteinGroupsBatchConfig.class);
        context.scan("uk.ac.ebi.ep.data.dataconfig");
        context.refresh();

        runProteinGroupBatch(context);
        validateXml(context);
    }

    private static void runProteinGroupBatch(ApplicationContext context)
            throws NoSuchJobException, JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        Job proteinGroupJob = context.getBean("readDataFromDBJob", Job.class);

        JobLauncher launcher = context.getBean(JobLauncher.class);
        launcher.run(proteinGroupJob, new JobParameters());
    }
}
