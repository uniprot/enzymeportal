package uk.ac.ebi.ep.xml.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 *
 * @author joseph
 */
@Slf4j
public class XmlStepExecutionListener implements StepExecutionListener {


    @Override
    public void beforeStep(StepExecution se) {
        log.debug("Step Execution Info :: " + se);
    }

    @Override
    public ExitStatus afterStep(StepExecution se) {
        log.debug("Step Execution Exit Status Info :: " + se);
        return se.getExitStatus();
    }
}
