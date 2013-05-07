package uk.ac.ebi.ep.util;

import java.util.List;

import org.junit.Test;
import uk.ac.ebi.ep.enzyme.model.Molecule;

import static junit.framework.Assert.assertEquals;

/**
 * @author rafa
 * @since 2013-05-03
 */
public class EPUtilTest {

    @Test
    public void testParseTextForInhibitors() {
        String text;
        List<Molecule> inhibitors;
        String[] names;

        text = "Inhibited by sucrose";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("sucrose", inhibitors.get(0).getName());

        text = "Not inhibited by sucrose.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(0, inhibitors.size());

        text = "Inhibited by a and b.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(2, inhibitors.size());
        assertEquals("a", inhibitors.get(0).getName());
        assertEquals("b", inhibitors.get(1).getName());

        text = "Inhibited by c, b, a. Activated by m, n. Does this and that." +
                " Inactivated by foo.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "a", "b", "c", "foo" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Inactivated by bar or Baz.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(2, inhibitors.size());
        assertEquals("bar", inhibitors.get(0).getName());
        assertEquals("Baz", inhibitors.get(1).getName());

        text = "Activated by something.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(0, inhibitors.size());

        text = "Inhibited by z; inactivated by y and x.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "x", "y", "z" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Inhibited by EDTA, zinc, silver, copper, and activated by" +
                " magnesium and calcium. A blah blah...";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "copper","EDTA","silver","zinc" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Is inhibited by H2O2. 1,10-phenanthroline inhibits the" +
                " activity slightly, but ...";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("H2O2", inhibitors.get(0).getName());

        text = "Inhibited by manganese, copper, mercury, and iron ions.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "copper","iron ions","manganese","mercury" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Completely inhibited by 5 mM N-ethylmaleimide or 0.1 mM Cu2+." +
                " Partially inhibited by 0.1 uM Fe2+ or 2 mM Hg2+.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "Cu2+", "Fe2+", "Hg2+", "N-ethylmaleimide"};
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Completely inhibited by CuCl2, FeCl3, HgCl2 and" +
                " N-bromosuccinimide. Moderately inhibited by AgCl, AlCl3," +
                " Pb(CH3COO)2 and dithiothreitol. BaCl2, CaCl2, KCl, MgCl2," +
                " MnCl2, NaCl, ZnCl2, ethylenediaminetetraacetic acid," +
                " N-ethylmaleimide, iodoacetic acid and" +
                " p-chloromercuribenzoic acid have little or no effect on" +
                " activity.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "AgCl", "AlCl3", "CuCl2", "dithiothreitol",
                "FeCl3", "HgCl2", "N-bromosuccinimide", "Pb(CH3COO)2" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Completely inhibited by 10 mM p-coumaric acid, this" +
                " inhibition is rapid, reversible and non-competitive." +
                " Completely inhibited by 0.1 mM Cu2+, 0.1 mM Hg2+ and 10 mM" +
                " caffeic acid. Partially inhibited by 5 mM N-ethylmaleimide," +
                " 1 mM diethylpyrocarbonate and 1 mM acetyl-CoA.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "acetyl-CoA", "caffeic acid", "Cu2+",
                "diethylpyrocarbonate", "Hg2+", "N-ethylmaleimide",
                "p-coumaric acid"};
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Inhibited by high levels of AMP.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("AMP", inhibitors.get(0).getName());

        text = "Inhibited competitively by nicotinic acid with a Ki of" +
                " 0.49 mM. Inhibited by thiol-specific compounds" +
                " p-chloromercuribenzoate, DTNB, Ag2SO4, HgCl2, CuCl2 and" +
                " N-ethylmaleimide. No inhibition by o-phenanthroline," +
                " 8-hydroxyquinoline, EDTA, disodium" +
                " 4,5-dihydroxy-m-benzenedisulfonate, fluoride, azide, KCl," +
                " LiCl, NaCl, BaCl2, MnCl2, MgCl2, PBCl, ZnCl2, CoCl2, SnCl2," +
                " FeSO4, FeCl3, NiCl2, CdCl2, AlCl3, iodoacetic acid," +
                " hydro-xylamine, phenylhydrazine, semicarbazide, cysteamine," +
                " alpha,alpha-dipyridyl and urea.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "Ag2SO4", "CuCl2", "DTNB", "HgCl2",
                "N-ethylmaleimide", "nicotinic acid",
                "p-chloromercuribenzoate" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "2-hydroxy-dATPase activity is inhibited by 2-OH-dADP," +
                " 8-OH-dGDP and 8-OH-dGTP. 8-OH-dGTPase activity is" +
                " inhibited by 8-OH-dGDP, 2-OH-dADP and 2-OH-dATP";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "2-OH-dADP", "2-OH-dATP", "8-OH-dGDP",
                "8-OH-dGTP" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Inhibited with low affinity by edelfosine.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("edelfosine", inhibitors.get(0).getName());

        text = "Inhibited by captopril and, to a lesser extent, by" +
                " lisinopril, trandolaprilat, fosinoprilat and enalaprilat.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "captopril", "enalaprilat", "fosinoprilat",
                "lisinopril", "trandolaprilat" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Inhibited by AIM-100" +
                " (4-amino-5,6-biaryl-furo[2,3-d]pyrimidine), which" +
                " suppresses activating phosphorylation at Tyr-284." +
                " Repressed by dasatinib.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("AIM-100 (4-amino-5,6-biaryl-furo[2,3-d]pyrimidine)",
                inhibitors.get(0).getName());

        text = "Inhibited by metalloproteinase inhibitor 3 (TIMP-3), but" +
                " not by TIMP-1, TIMP-2 and TIMP-4.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("metalloproteinase inhibitor 3 (TIMP-3)",
                inhibitors.get(0).getName());

        text = "Inhibited by adenosine deaminase inhibitor 2-DCF, but not" +
                " by adenosine deaminase 1 inhibitor EHNA.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("adenosine deaminase inhibitor 2-DCF",
                inhibitors.get(0).getName());

        text = "Activated by manganese or magnesium ions. In the presence of" +
                " magnesium ions, the enzyme is activated by bicarbonate" +
                " while in the presence of manganese ions, the enzyme is" +
                " inhibited by bicarbonate. In the absence of magnesium and" +
                " bicarbonate, the enzyme is weakly activated by calcium.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("bicarbonate", inhibitors.get(0).getName());

        text = "Inhibited by NaBH4 in the presence of FBP.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(1, inhibitors.size());
        assertEquals("NaBH4", inhibitors.get(0).getName());

        text = "Inhibited by pyridoxal 5-phosphate, bathophenanthroline," +
                " mersalyl, p-hydroxymercuribenzoate and tannic acid.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "bathophenanthroline", "mersalyl",
                "p-hydroxymercuribenzoate", "pyridoxal 5-phosphate",
                "tannic acid" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }

        text = "Inhibited by acetylation at Lys-642 and activated by" +
                " deacetylation.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(0, inhibitors.size());

        text = "Activated by calcium/calmodulin. Inhibited by the G protein" +
                " beta and gamma subunit complex.\n";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(0, inhibitors.size());

        text = "Inhibited by substrate concentrations above 0.5 mM.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        assertEquals(0, inhibitors.size());

        // P14618
        text = "Isoform M2 is allosterically activated by D-fructose" +
                " 1,6-bisphosphate (FBP). Inhibited by oxalate and" +
                " 3,3',5-triiodo-L-thyronine (T3). The activity of the" +
                " tetrameric form is inhibited by PML. Selective binding to" +
                " tyrosine-phosphorylated peptides releases the allosteric" +
                " activator FBP, leading to inhibition of PKM enzymatic" +
                " activity, this diverts glucose metabolites from energy" +
                " production to anabolic processes when cells are stimulated" +
                " by certain growth factors. Glycolytic flux are highly" +
                " dependent on de novo biosynthesis of serine and glycine," +
                " and serine is a natural ligand and allosteric activator of" +
                " isoform M2.";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        // oxalate, 3,3',5-triiodo-L-thyronine, PML
        assertEquals(3, inhibitors.size());
/*
        text = "Inhibited by foo, a nice drug which does this, and by bar," +
                " a weird drug which does that";
        inhibitors = EPUtil.parseTextForInhibitors(text);
        names = new String[]{ "bar", "foo" };
        assertEquals(names.length, inhibitors.size());
        for (int i = 0; i < inhibitors.size(); i++){
            assertEquals(names[i], inhibitors.get(i).getName());
        }
*/
    }

    @Test
    public void testParseTextForActivators() throws Exception {
        String text;
        List<Molecule> activators;
        String[] names;

        text = "Activated by ADP.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "ADP" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by ADP";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "ADP" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by citrate";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "citrate" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by calmodulin";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "calmodulin" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by c-di-GMP.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "c-di-GMP" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by magnesium ions.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "magnesium ions" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by ascorbate and magnesium ions.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "ascorbate", "magnesium ions" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Allosterically activated by various compounds, including ATP.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "ATP" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Is not activated by Mn2+, Mg2+, Ca2+, Zn2+ or Co2+.";
        activators = EPUtil.parseTextForActivators(text);
        assertEquals(0, activators.size());

        text = "Inhibited by acetylation at Lys-642 and activated by" +
                " deacetylation.";
        activators = EPUtil.parseTextForActivators(text);
        assertEquals(0, activators.size());

        text = "Activated by monovalent cations, such as potassium," +
                " rubidium or ammonium.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "ammonium", "potassium", "rubidium" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        // This one is not a small molecule!!
        text = "Phosphorylated and activated by pdk-1.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "pdk-1" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by Mg2+ or Mn2+ and strongly inhibited by Zn2+.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "Mg2+", "Mn2+" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by cytokinins to initiate phosphorelay signaling";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "cytokinins" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by divalent metal ions. Inhibited by certain" +
                " thiol reagents.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "divalent metal ions" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by ATP, inhibited by GTP, EDTA and" +
                " inorganic phosphate.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "ATP" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Inactivated by metal-chelating agents and" +
                " specifically activated by Zn2+ and Cl-.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "Cl-", "Zn2+" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by calcium/calmodulin." +
                " Inhibited by the G protein beta and gamma subunit complex.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "calcium/calmodulin" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Strongly inhibited by Hg (2+). Inhibited by Zn (2+)." +
                " Activated by Fe (2+), Mg (2+) and Ba (2+).";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "Ba (2+)", "Fe (2+)", "Mg (2+)" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Strongly activated by chloride. Specifically inhibited" +
                " by lisinopril, captopril and enalaprilat.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "chloride" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Is activated by phosphorylation (on histidine)" +
                " and is inhibited by PEP, 3-phosphoglycerate and succinate.";
        activators = EPUtil.parseTextForActivators(text);
        assertEquals(0, activators.size());

        text = "Activated by reductants such as dithiothreitol (DTT)," +
                " and by thioredoxin in vivo, following exposure to light.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "dithiothreitol (DTT)", "thioredoxin" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Inhibited by EDTA, zinc, silver, copper, and" +
                " activated by magnesium and calcium. A competitive" +
                " inhibition is observed with NAD at concentrations higher" +
                " than 5.6 mM, and with trilostane" +
                " (3,17-dihydroxy-4,5-epoxyandrost-2-ene-2-carbonitrile).";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "calcium", "magnesium" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Completely inhibited by Cu2+, Hg2+ and N-bromosuccinimide." +
                " Strongly inhibited by Ag+, Zn2+ and Pb2+. Moderately" +
                " inhibited by Fe3+, Al3+, Mn2+, dithiothreitol and" +
                " p-chloromercuribenzoic acid. Slightly activated by Mg2+" +
                " and Ca2+. Unaffected by Na+, K+, Ba2+, EDTA," +
                " iodoacetic acid and N-ethylmalaimide.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "Ca2+", "Mg2+" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        // FIXME: missing AMP
        text = "Activated by phosphorylation on Thr-183. Binding of AMP" +
                " to non-catalytic gamma subunit (PRKAG1, PRKAG2 or PRKAG3)" +
                " results in allosteric activation, inducing phosphorylation" +
                " on Thr-183. AMP-binding to gamma subunit also sustains" +
                " activity by preventing dephosphorylation of Thr-183. ADP" +
                " also stimulates Thr-183 phosphorylation, without" +
                " stimulating already phosphorylated AMPK. ATP promotes" +
                " dephosphorylation of Thr-183, rendering the enzyme" +
                " inactive. Under physiological conditions AMPK mainly" +
                " exists in its inactive form in complex with ATP, which" +
                " is much more abundant than AMP. AMPK is activated by" +
                " antihyperglycemic drug metformin, a drug prescribed to" +
                " patients with type 2 diabetes: in vivo, metformin seems to" +
                " mainly inhibit liver gluconeogenesis. However, metformin" +
                " can be used to activate AMPK in muscle and other cells " +
                " culture or ex vivo (Ref.11). Selectively inhibited by" +
                " compound C (6-[4-(2-Piperidin-1-yl-ethoxy)-phenyl)]-3-pyridin-4-yl-pyyrazolo[1,5-a] pyrimidine." +
                " Activated by resveratrol, a natural polyphenol present in" +
                " red wine, and S17834, a synthetic polyphenol.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "metformin", "resveratrol", "S17834" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Stabilized in the inactive form by an association between" +
                " the SH3 domain and the SH2-TK linker region, interactions" +
                " of the N-terminal cap, and contributions from an" +
                " N-terminal myristoyl group and phospholipids." +
                " Activated by autophosphorylation as well as by SRC-family" +
                " kinase-mediated phosphorylation. Activated by RIN1 binding" +
                " to the SH2 and SH3 domains. Also stimulated by cell death" +
                " inducers and DNA-damage. Phosphatidylinositol" +
                " 4,5-bisphosphate (PIP2), a highly abundant" +
                " phosphoinositide known to regulate cytoskeletal and" +
                " membrane proteins, inhibits also the tyrosine kinase" +
                " activity. Inhibited by ABI1, whose activity" +
                " is controlled by ABL1 itself through tyrosine" +
                " phosphorylation. Also inhibited by imatinib mesylate" +
                " (Gleevec) which is used for the treatment of chronic" +
                " myeloid leukemia (CML), and by VX-680, an inhibitor that" +
                " acts also on imatinib-resistant mutants.";
        activators = EPUtil.parseTextForActivators(text);
        assertEquals(0, activators.size());

        text = "Activity is increased by oligomerization. Activated by" +
                " citrate. Citrate and MID1IP1 promote oligomerization." +
                " Inhibited by malonyl-CoA.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "citrate" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by chloride and fluoride, but not bromide." +
                " Inhibited by MLN-4760, cFP_Leu, and EDTA, but not by the" +
                " ACE inhibitors linosipril, captopril and enalaprilat.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "chloride", "fluoride" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Inhibited by sphingosine. Inhibited by Mn2+, Zn2+, and Cu2+" +
                " in a dose-dependent manner. Slightly activated by Ca2+" +
                " in a dose-dependent manner.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "Ca2+" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Inhibited by hydroxamate-type metalloproteinase inhibitors" +
                " such as marimastat. Inhibited by metalloproteinase" +
                " inhibitor 2 (TIMP-2) and TIMP-3 at nanomolar" +
                " concentrations. Not significantly inhibited by TIMP-1 at" +
                " concentrations of up to 100 nM. Not activated by PMA or" +
                " ionomycin.";
        activators = EPUtil.parseTextForActivators(text);
        assertEquals(0, activators.size());

        text = "Activated in response to insulin. Three specific sites, one" +
                " in the kinase domain (Thr-309) and the two other ones in" +
                " the C-terminal regulatory region (Ser-474 and Tyr-475)," +
                " need to be phosphorylated for its full activation.";
        activators = EPUtil.parseTextForActivators(text);
        assertEquals(0, activators.size());

        text = "When the two monomeric subunits are covalently linked by a" +
                " S-S bond, the enzyme is essentially inactive. When the" +
                " disulfide bond is reduced, its component sulfhydryls can" +
                " associate with K-keto acids through formation of a" +
                " thiohemiacetal, resulting in enzyme activation. Activated" +
                " by glyoxylate, irrespective to the substitution found at" +
                " Cys-127. That suggests the presence of a second activation" +
                " site, possibly Cys-177.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "glyoxylate" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "3'-5' exonuclease activity is activated by sodium and" +
                " manganese. 3'-5' exonuclease and 3'-phosphodiesterase" +
                " activities are stimulated in presence of PCNA.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "manganese", "sodium" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by ethanol. Also activated by Co2+, Zn2+ and" +
                " glycerol. Inhibited by EDTA, inorganic phosphate," +
                " nucleosides and Ca2+. Unaffected by fluoride and tartrate.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "Co2+", "ethanol", "glycerol", "Zn2+" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        // P14618
        text = "Isoform M2 is allosterically activated by D-fructose" +
                " 1,6-bisphosphate (FBP). Inhibited by oxalate and" +
                " 3,3',5-triiodo-L-thyronine (T3). The activity of the" +
                " tetrameric form is inhibited by PML. Selective binding to" +
                " tyrosine-phosphorylated peptides releases the allosteric" +
                " activator FBP, leading to inhibition of PKM enzymatic" +
                " activity, this diverts glucose metabolites from energy" +
                " production to anabolic processes when cells are stimulated" +
                " by certain growth factors. Glycolytic flux are highly" +
                " dependent on de novo biosynthesis of serine and glycine," +
                " and serine is a natural ligand and allosteric activator of" +
                " isoform M2.";
        activators = EPUtil.parseTextForActivators(text);

        // D-fructose 1,6-bisphosphate (FBP)
        assertEquals(1, activators.size());
        assertEquals("D-fructose 1,6-bisphosphate (FBP)",
                activators.iterator().next().getName());

        text = "Activated by manganese or magnesium ions. In the presence" +
                " of magnesium ions, the enzyme is activated by bicarbonate" +
                " while in the presence of manganese ions, the enzyme is" +
                " inhibited by bicarbonate. In the absence of magnesium and" +
                " bicarbonate, the enzyme is weakly activated by calcium.";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "bicarbonate", "calcium", "magnesium ions",
                "manganese" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

        text = "Activated by ligand-binding and subsequent phosphorylation." +
                " Inactivated through dephosphorylation by receptor protein" +
                " tyrosine phosphatase beta and zeta complex (PTPRB/PTPRZ1)" +
                " when there is no stimulation by a ligand. Staurosporine," +
                " crizotinib and CH5424802 act as inhibitors of ALK kinase" +
                " activity.";
        activators = EPUtil.parseTextForActivators(text);
        assertEquals(0, activators.size());

        text = "Allosterically activated by Mg-ATP, and inactivated by" +
                " inorganic phosphate";
        activators = EPUtil.parseTextForActivators(text);
        names = new String[]{ "Mg-ATP" };
        assertEquals(names.length, activators.size());
        for (int i = 0; i < activators.size(); i++){
            assertEquals(names[i], activators.get(i).getName());
        }

    }

}
