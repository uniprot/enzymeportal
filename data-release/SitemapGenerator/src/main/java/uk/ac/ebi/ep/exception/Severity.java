/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.exception;

/**
 * The Severity class is used to specify the severity of an exception that has occurred
 * within the project. This is used to allow the catching component to act upon the caught
 * exception in the most appropriate manner.
 * @author Joseph
 */
public enum Severity {
    /**
     * WARNING level indicates that this exception should be logged, but that it has
     * no adverse effect on the operation of the system in any respect.
     */
    WARNING,
    /**
     * TASK AFFECTING exceptions indicate that the current task will be unable to
     * complete, but that the project's operation is otherwise unaffected.
     */
    TASK_AFFECTING,
    /**
     * COMPONENT AFFECTING exceptions indicate that the current component will not
     * continue functioning in its current state, but that the system can be
     * recovered without requiring a software or hardware re-initialisation.
     */
    COMPONENT_AFFECTING,
    /**
     * SYSTEM AFFECTING exceptions indicate that the Application is unable to continue
     * functioning. To rectify the problem, a software restart or complete hardware
     * shutdown/reboot may be required.
     */
    SYSTEM_AFFECTING,
    /**
     * EXTERNAL SYSTEM AFFECTING exceptions indicate that an external system is
     * adversely affected by the exception encountered. This is the most critical
     * of all reportable severities, as it may require manual hardware or software
     * re-initialisation of those external systems.
     */
    EXTERNAL_SYSTEM_AFFECTING
}
