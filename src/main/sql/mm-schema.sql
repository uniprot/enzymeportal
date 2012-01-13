--------------------------------------------------------
--  File created - Friday-January-13-2012   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence S_ENTRY_ID
--------------------------------------------------------

   CREATE SEQUENCE  "S_ENTRY_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 100000 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence S_XREF_ID
--------------------------------------------------------

   CREATE SEQUENCE  "S_XREF_ID"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1000000 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table MM_ACCESSION
--------------------------------------------------------

  CREATE TABLE "MM_ACCESSION" 
   (	"ID" NUMBER(10,0), 
	"ACCESSION" VARCHAR2(255 CHAR), 
	"ACC_INDEX" NUMBER(10,0)
   ) ;
--------------------------------------------------------
--  DDL for Table MM_ENTRY
--------------------------------------------------------

  CREATE TABLE "MM_ENTRY" 
   (	"ID" NUMBER(10,0), 
	"DB_NAME" VARCHAR2(255 CHAR), 
	"ENTRY_ID" VARCHAR2(255 CHAR), 
	"ENTRY_NAME" VARCHAR2(1000 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table MM_XREF
--------------------------------------------------------

  CREATE TABLE "MM_XREF" 
   (	"ID" NUMBER(10,0), 
	"FROM_ENTRY" NUMBER(10,0), 
	"TO_ENTRY" NUMBER(10,0), 
	"RELATIONSHIP" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Index MM_ACCESSION_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_ACCESSION_INDEX" ON "MM_ACCESSION" ("ACCESSION") 
  ;
--------------------------------------------------------
--  DDL for Index MM_DBNAME_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_DBNAME_INDEX" ON "MM_ENTRY" ("DB_NAME") 
  ;
--------------------------------------------------------
--  DDL for Index MM_ENTRY_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MM_ENTRY_UK1" ON "MM_ENTRY" ("DB_NAME", "ENTRY_ID") 
  ;
--------------------------------------------------------
--  DDL for Index MM_FROMENTRY_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_FROMENTRY_INDEX" ON "MM_XREF" ("FROM_ENTRY") 
  ;
--------------------------------------------------------
--  DDL for Index MM_TOENTRY_INDEX
--------------------------------------------------------

  CREATE INDEX "MM_TOENTRY_INDEX" ON "MM_XREF" ("TO_ENTRY") 
  ;
--------------------------------------------------------
--  DDL for Index MM_XREF_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MM_XREF_UK1" ON "MM_XREF" ("FROM_ENTRY", "TO_ENTRY", "RELATIONSHIP") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C0068898
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C0068898" ON "MM_ACCESSION" ("ID", "ACC_INDEX") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C0068901
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C0068901" ON "MM_ENTRY" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C0068906
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C0068906" ON "MM_XREF" ("ID") 
  ;
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
--  Ref Constraints for Table MM_XREF
--------------------------------------------------------

  ALTER TABLE "MM_XREF" ADD CONSTRAINT "FK7208F05AC6CD2D0E" FOREIGN KEY ("FROM_ENTRY")
	  REFERENCES "MM_ENTRY" ("ID") ENABLE;
 
  ALTER TABLE "MM_XREF" ADD CONSTRAINT "FK7208F05ADC43031F" FOREIGN KEY ("TO_ENTRY")
	  REFERENCES "MM_ENTRY" ("ID") ENABLE;
