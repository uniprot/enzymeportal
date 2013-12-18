--------------------------------------------------------
--  File created - Thursday-May-16-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence S_ENTRY_ID
--------------------------------------------------------

   CREATE SEQUENCE  "S_ENTRY_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 16294685 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_XREF_ID
--------------------------------------------------------

   CREATE SEQUENCE  "S_XREF_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 74500887 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table MM_ACCESSION
--------------------------------------------------------

  CREATE TABLE "MM_ACCESSION" 
   (	"ID" NUMBER(10,0), 
	"ACCESSION" VARCHAR2(255 CHAR), 
	"ACC_INDEX" NUMBER(10,0)
   ) ;
 

   COMMENT ON COLUMN "MM_ACCESSION"."ID" IS 'Entry ID (internal to the MegaMap)';
 
   COMMENT ON COLUMN "MM_ACCESSION"."ACCESSION" IS 'The accession number, as provided by the source database.';
 
   COMMENT ON COLUMN "MM_ACCESSION"."ACC_INDEX" IS 'Order of the accessions. If there is a primary one - like in UniProt - this will have index 0 (zero).';
 
   COMMENT ON TABLE "MM_ACCESSION"  IS 'Entry accessions, for those databases which provide them.';

--------------------------------------------------------
--  DDL for Table MM_ENTRY
--------------------------------------------------------

  CREATE TABLE "MM_ENTRY" 
   (	"ID" NUMBER(10,0), 
	"DB_NAME" VARCHAR2(255 CHAR), 
	"ENTRY_ID" VARCHAR2(255 CHAR), 
	"ENTRY_NAME" VARCHAR2(1000 CHAR)
   ) ;
 

   COMMENT ON COLUMN "MM_ENTRY"."ID" IS 'Entry ID (internal to the MegaMap).';
 
   COMMENT ON COLUMN "MM_ENTRY"."DB_NAME" IS 'Database name (CV).';
 
   COMMENT ON COLUMN "MM_ENTRY"."ENTRY_ID" IS 'Entry ID as provided by the source database.';
 
   COMMENT ON COLUMN "MM_ENTRY"."ENTRY_NAME" IS 'Entry name as provided by the source database.';
 
   COMMENT ON TABLE "MM_ENTRY"  IS 'Database entries.';

--------------------------------------------------------
--  DDL for Table MM_XREF
--------------------------------------------------------

  CREATE TABLE "MM_XREF" 
   (	"ID" NUMBER(10,0), 
	"FROM_ENTRY" NUMBER(10,0), 
	"TO_ENTRY" NUMBER(10,0), 
	"RELATIONSHIP" VARCHAR2(255 CHAR)
   ) ;
 

   COMMENT ON COLUMN "MM_XREF"."ID" IS 'Xref ID (internal to MegaMap, not used).';
 
   COMMENT ON COLUMN "MM_XREF"."FROM_ENTRY" IS 'MM internal ID of the entry which is source of the cross-reference.';
 
   COMMENT ON COLUMN "MM_XREF"."TO_ENTRY" IS 'MM internal ID of the entry which is target of the cross-reference.';
 
   COMMENT ON COLUMN "MM_XREF"."RELATIONSHIP" IS 'Relationship between the source and target entries (CV).';
 
   COMMENT ON TABLE "MM_XREF"  IS 'Cross-references between entries considered in the enzyme portal.';

--------------------------------------------------------
--  DDL for Table UNIPROT2COMPOUND
--------------------------------------------------------

  CREATE TABLE "UNIPROT2COMPOUND" 
   (	"COMPOUND_NAME" VARCHAR2(1000 CHAR), 
	"COMPOUND_DB" VARCHAR2(255 CHAR), 
	"COMPOUND_ID" VARCHAR2(255 CHAR), 
	"RELATIONSHIP" VARCHAR2(255 CHAR), 
	"UNIPROT_ID" VARCHAR2(255 CHAR), 
	"UNIPROT_NAME" VARCHAR2(1000 CHAR), 
	"UNIPROT_ACCESSION" VARCHAR2(255 CHAR)
   ) ;
 

   COMMENT ON COLUMN "UNIPROT2COMPOUND"."COMPOUND_NAME" IS 'The name of the compound in the source database.';
 
   COMMENT ON COLUMN "UNIPROT2COMPOUND"."COMPOUND_DB" IS 'Cross references from ChEBI are either direct (inhibitors/activators) or
inferred from the EC classification (cofactors, reaction participants in
Rhea). Other databases - DrugBank, ChEMBL - are cross referenced with no
intermediate step.';
 
   COMMENT ON COLUMN "UNIPROT2COMPOUND"."COMPOUND_ID" IS 'The ID of the compound in the source database.';
 
   COMMENT ON COLUMN "UNIPROT2COMPOUND"."RELATIONSHIP" IS 'The relationship between the compound and the UniProt entry (CV).';
 
   COMMENT ON COLUMN "UNIPROT2COMPOUND"."UNIPROT_ID" IS 'The UniProt ID ("entry name").';
 
   COMMENT ON COLUMN "UNIPROT2COMPOUND"."UNIPROT_NAME" IS 'The recommended name of the UniProt entry.';
 
   COMMENT ON COLUMN "UNIPROT2COMPOUND"."UNIPROT_ACCESSION" IS 'The UniProt accession (the primary one).';
 
   COMMENT ON TABLE "UNIPROT2COMPOUND"  IS 'Created from MM_ENTRY, MM_ACCESSION and MM_XREF for quick retrieval of
compounds related to a UniProt entry.';

--------------------------------------------------------
--  DDL for Index MM_ACCESSION_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_ACCESSION_INDEX" ON "MM_ACCESSION" ("ACCESSION");

--------------------------------------------------------
--  DDL for Index MM_DBNAME_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_DBNAME_INDEX" ON "MM_ENTRY" ("DB_NAME");

--------------------------------------------------------
--  DDL for Index MM_ENTRY_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MM_ENTRY_UK1" ON "MM_ENTRY" ("DB_NAME", "ENTRY_ID");

--------------------------------------------------------
--  DDL for Index MM_FROMENTRY_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_FROMENTRY_INDEX" ON "MM_XREF" ("FROM_ENTRY");

--------------------------------------------------------
--  DDL for Index MM_TOENTRY_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_TOENTRY_INDEX" ON "MM_XREF" ("TO_ENTRY");

--------------------------------------------------------
--  DDL for Index MM_XREF_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MM_XREF_UK1" ON "MM_XREF" ("FROM_ENTRY", "TO_ENTRY", "RELATIONSHIP");

--------------------------------------------------------
--  DDL for Index SYS_C0068898
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C0068898" ON "MM_ACCESSION" ("ID", "ACC_INDEX");

--------------------------------------------------------
--  DDL for Index SYS_C0068901
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C0068901" ON "MM_ENTRY" ("ID");

--------------------------------------------------------
--  DDL for Index SYS_C0068906
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C0068906" ON "MM_XREF" ("ID");

--------------------------------------------------------
--  DDL for Index UNIPROT2COMPOUND_IND_UPACC
--------------------------------------------------------

  CREATE INDEX "UNIPROT2COMPOUND_IND_UPACC" ON "UNIPROT2COMPOUND" ("UNIPROT_ACCESSION");

--------------------------------------------------------
--  DDL for Index UNIPROT2COMPOUND_IND_UPID
--------------------------------------------------------

  CREATE INDEX "UNIPROT2COMPOUND_IND_UPID" ON "UNIPROT2COMPOUND" ("UNIPROT_ID");

--------------------------------------------------------
--  Constraints for Table MM_ACCESSION
--------------------------------------------------------

  ALTER TABLE "MM_ACCESSION" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "MM_ACCESSION" MODIFY ("ACCESSION" NOT NULL ENABLE);
 
  ALTER TABLE "MM_ACCESSION" MODIFY ("ACC_INDEX" NOT NULL ENABLE);
 
  ALTER TABLE "MM_ACCESSION" ADD PRIMARY KEY ("ID", "ACC_INDEX") ENABLE;

--------------------------------------------------------
--  Constraints for Table MM_ENTRY
--------------------------------------------------------

  ALTER TABLE "MM_ENTRY" ADD CONSTRAINT "MM_ENTRY_UK1" UNIQUE ("DB_NAME", "ENTRY_ID") ENABLE;
 
  ALTER TABLE "MM_ENTRY" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "MM_ENTRY" MODIFY ("DB_NAME" NOT NULL ENABLE);
 
  ALTER TABLE "MM_ENTRY" ADD PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "MM_ENTRY" MODIFY ("ENTRY_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table MM_XREF
--------------------------------------------------------

  ALTER TABLE "MM_XREF" ADD CONSTRAINT "MM_XREF_UK1" UNIQUE ("FROM_ENTRY", "TO_ENTRY", "RELATIONSHIP") ENABLE;
 
  ALTER TABLE "MM_XREF" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "MM_XREF" MODIFY ("FROM_ENTRY" NOT NULL ENABLE);
 
  ALTER TABLE "MM_XREF" MODIFY ("TO_ENTRY" NOT NULL ENABLE);
 
  ALTER TABLE "MM_XREF" MODIFY ("RELATIONSHIP" NOT NULL ENABLE);
 
  ALTER TABLE "MM_XREF" ADD PRIMARY KEY ("ID") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table MM_ACCESSION
--------------------------------------------------------

  ALTER TABLE "MM_ACCESSION" ADD CONSTRAINT "MM_ACCESSION_MM_ENTRY_FK1" FOREIGN KEY ("ID")
	  REFERENCES "MM_ENTRY" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table MM_XREF
--------------------------------------------------------

  ALTER TABLE "MM_XREF" ADD CONSTRAINT "FK7208F05AC6CD2D0E" FOREIGN KEY ("FROM_ENTRY")
	  REFERENCES "MM_ENTRY" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "MM_XREF" ADD CONSTRAINT "FK7208F05ADC43031F" FOREIGN KEY ("TO_ENTRY")
	  REFERENCES "MM_ENTRY" ("ID") ON DELETE CASCADE ENABLE;
/
