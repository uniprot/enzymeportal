<%-- 
    Document   : chembl-target
    Created on : Sep 17, 2018, 2:27:46 PM
    Author     : Joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fieldset>

    <div class=" large-12  targetBox" style="max-width: 800px; height: 500px;max-height: 800px;">
        <br/>
        <legend class="tile-text">Activity Charts </legend>

            <object data="https://www.ebi.ac.uk/chembl/embed/#target_report_card/${enzymeModel.chemblTargetId}/activity_charts" width="100%" height="100%"></object>
    
    </div>
       
</fieldset>






