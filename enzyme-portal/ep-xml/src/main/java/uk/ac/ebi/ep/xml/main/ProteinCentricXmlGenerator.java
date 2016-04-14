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
import uk.ac.ebi.ep.xml.config.ProteinBatchConfig;
import uk.ac.ebi.ep.xml.util.Preconditions;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public final class ProteinCentricXmlGenerator {
    private ProteinCentricXmlGenerator() {}

    public static void main(String[] args) throws Exception {
        String profile;

        Preconditions.checkArgument(args.length != 1, "Please provide required parameters: \n\t0 - profile name");

        profile = args[0];

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerShutdownHook();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(DataConfig.class, ProteinBatchConfig.class);
        context.scan("uk.ac.ebi.ep.data.dataconfig");
        context.refresh();

        runXmlGeneration(context);
        validateXml(context);
    }

    private static void runXmlGeneration(ApplicationContext context)
            throws NoSuchJobException, JobParametersInvalidException, JobExecutionAlreadyRunningException,
                   JobRestartException, JobInstanceAlreadyCompleteException {
        Job proteinCentricJob = context.getBean("proteinCentricJob", Job.class);

        JobLauncher launcher = context.getBean(JobLauncher.class);
        launcher.run(proteinCentricJob, new JobParameters());
    }

    private static void validateXml(ApplicationContext context) {
        Environment env = context.getEnvironment();

        String xmlFile = env.getProperty("ep.protein.centric.xml.dir");
        String[] ebeyeXSDs = env.getProperty("ep.ebeye.xsd").split(",");


        EnzymePortalXmlValidator.validateXml(xmlFile, ebeyeXSDs);
    }
}