package uk.ac.ebi.ep.xml.main;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.xml.config.ProteinBatchConfig;
import uk.ac.ebi.ep.xml.util.Preconditions;

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
        context.getEnvironment().setActiveProfiles(profile);
        context.register(DataConfig.class, ProteinBatchConfig.class);
        context.refresh();

        JobLocator jobLocator = context.getBean(JobLocator.class);

        Job job = jobLocator.getJob(ProteinBatchConfig.PROTEIN_CENTRIC_JOB);

        JobLauncher launcher = context.getBean(JobLauncher.class);
        launcher.run(job, new JobParameters());
    }
}