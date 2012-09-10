/* Generated By:JavaCC: Do not edit this line. DalSqlParserConstants.java */
package com.sqewd.open.dal.core.persistence.query.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface DalSqlParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int K_ALL = 5;
  /** RegularExpression Id. */
  int K_AND = 6;
  /** RegularExpression Id. */
  int K_QAND = 7;
  /** RegularExpression Id. */
  int K_ANY = 8;
  /** RegularExpression Id. */
  int K_AS = 9;
  /** RegularExpression Id. */
  int K_ASC = 10;
  /** RegularExpression Id. */
  int K_AVG = 11;
  /** RegularExpression Id. */
  int K_BETWEEN = 12;
  /** RegularExpression Id. */
  int K_BINARY_INTEGER = 13;
  /** RegularExpression Id. */
  int K_BOOLEAN = 14;
  /** RegularExpression Id. */
  int K_BY = 15;
  /** RegularExpression Id. */
  int K_CHAR = 16;
  /** RegularExpression Id. */
  int K_COMMENT = 17;
  /** RegularExpression Id. */
  int K_COMMIT = 18;
  /** RegularExpression Id. */
  int K_CONNECT = 19;
  /** RegularExpression Id. */
  int K_COUNT = 20;
  /** RegularExpression Id. */
  int K_DATE = 21;
  /** RegularExpression Id. */
  int K_DELETE = 22;
  /** RegularExpression Id. */
  int K_DESC = 23;
  /** RegularExpression Id. */
  int K_DISTINCT = 24;
  /** RegularExpression Id. */
  int K_EXCLUSIVE = 25;
  /** RegularExpression Id. */
  int K_EXISTS = 26;
  /** RegularExpression Id. */
  int K_EXIT = 27;
  /** RegularExpression Id. */
  int K_FLOAT = 28;
  /** RegularExpression Id. */
  int K_FOR = 29;
  /** RegularExpression Id. */
  int K_FROM = 30;
  /** RegularExpression Id. */
  int K_GROUP = 31;
  /** RegularExpression Id. */
  int K_HAVING = 32;
  /** RegularExpression Id. */
  int K_IN = 33;
  /** RegularExpression Id. */
  int K_INSERT = 34;
  /** RegularExpression Id. */
  int K_INTEGER = 35;
  /** RegularExpression Id. */
  int K_INTERSECT = 36;
  /** RegularExpression Id. */
  int K_INTO = 37;
  /** RegularExpression Id. */
  int K_IS = 38;
  /** RegularExpression Id. */
  int K_LIKE = 39;
  /** RegularExpression Id. */
  int K_LOCK = 40;
  /** RegularExpression Id. */
  int K_MAX = 41;
  /** RegularExpression Id. */
  int K_MIN = 42;
  /** RegularExpression Id. */
  int K_MINUS = 43;
  /** RegularExpression Id. */
  int K_MODE = 44;
  /** RegularExpression Id. */
  int K_NATURAL = 45;
  /** RegularExpression Id. */
  int K_NOT = 46;
  /** RegularExpression Id. */
  int K_NOWAIT = 47;
  /** RegularExpression Id. */
  int K_NULL = 48;
  /** RegularExpression Id. */
  int K_NUMBER = 49;
  /** RegularExpression Id. */
  int K_OF = 50;
  /** RegularExpression Id. */
  int K_ONLY = 51;
  /** RegularExpression Id. */
  int K_OR = 52;
  /** RegularExpression Id. */
  int K_QOR = 53;
  /** RegularExpression Id. */
  int K_ORDER = 54;
  /** RegularExpression Id. */
  int K_PRIOR = 55;
  /** RegularExpression Id. */
  int K_QUIT = 56;
  /** RegularExpression Id. */
  int K_READ = 57;
  /** RegularExpression Id. */
  int K_REAL = 58;
  /** RegularExpression Id. */
  int K_ROLLBACK = 59;
  /** RegularExpression Id. */
  int K_ROW = 60;
  /** RegularExpression Id. */
  int K_SELECT = 61;
  /** RegularExpression Id. */
  int K_SET = 62;
  /** RegularExpression Id. */
  int K_SHARE = 63;
  /** RegularExpression Id. */
  int K_SMALLINT = 64;
  /** RegularExpression Id. */
  int K_START = 65;
  /** RegularExpression Id. */
  int K_SUM = 66;
  /** RegularExpression Id. */
  int K_TABLE = 67;
  /** RegularExpression Id. */
  int K_TRANSACTION = 68;
  /** RegularExpression Id. */
  int K_UNION = 69;
  /** RegularExpression Id. */
  int K_UPDATE = 70;
  /** RegularExpression Id. */
  int K_VALUES = 71;
  /** RegularExpression Id. */
  int K_VARCHAR2 = 72;
  /** RegularExpression Id. */
  int K_VARCHAR = 73;
  /** RegularExpression Id. */
  int K_WHERE = 74;
  /** RegularExpression Id. */
  int K_WITH = 75;
  /** RegularExpression Id. */
  int K_WORK = 76;
  /** RegularExpression Id. */
  int K_WRITE = 77;
  /** RegularExpression Id. */
  int S_NUMBER = 78;
  /** RegularExpression Id. */
  int FLOAT = 79;
  /** RegularExpression Id. */
  int INTEGER = 80;
  /** RegularExpression Id. */
  int DIGIT = 81;
  /** RegularExpression Id. */
  int LINE_COMMENT = 82;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 83;
  /** RegularExpression Id. */
  int S_VARNAME = 84;
  /** RegularExpression Id. */
  int S_IDENTIFIER = 85;
  /** RegularExpression Id. */
  int LETTER = 86;
  /** RegularExpression Id. */
  int SPECIAL_CHARS = 87;
  /** RegularExpression Id. */
  int S_BIND = 88;
  /** RegularExpression Id. */
  int S_CHAR_LITERAL = 89;
  /** RegularExpression Id. */
  int S_QUOTED_IDENTIFIER = 90;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\r\"",
    "\"\\n\"",
    "\"ALL\"",
    "\";\"",
    "\"AND\"",
    "\"ANY\"",
    "\"AS\"",
    "\"ASC\"",
    "\"AVG\"",
    "\"BETWEEN\"",
    "\"BINARY_INTEGER\"",
    "\"BOOLEAN\"",
    "\"BY\"",
    "\"CHAR\"",
    "\"COMMENT\"",
    "\"COMMIT\"",
    "\"CONNECT\"",
    "\"COUNT\"",
    "\"DATE\"",
    "\"DELETE\"",
    "\"DESC\"",
    "\"DISTINCT\"",
    "\"EXCLUSIVE\"",
    "\"EXISTS\"",
    "\"EXIT\"",
    "\"FLOAT\"",
    "\"FOR\"",
    "\"FROM\"",
    "\"GROUP\"",
    "\"HAVING\"",
    "\"IN\"",
    "\"INSERT\"",
    "\"INTEGER\"",
    "\"INTERSECT\"",
    "\"INTO\"",
    "\"IS\"",
    "\"LIKE\"",
    "\"LOCK\"",
    "\"MAX\"",
    "\"MIN\"",
    "\"MINUS\"",
    "\"MODE\"",
    "\"NATURAL\"",
    "\"NOT\"",
    "\"NOWAIT\"",
    "\"NULL\"",
    "\"NUMBER\"",
    "\"OF\"",
    "\"ONLY\"",
    "\",\"",
    "\"OR\"",
    "\"ORDER\"",
    "\"PRIOR\"",
    "\"QUIT\"",
    "\"READ\"",
    "\"REAL\"",
    "\"ROLLBACK\"",
    "\"ROW\"",
    "\"SELECT\"",
    "\"SET\"",
    "\"SHARE\"",
    "\"SMALLINT\"",
    "\"START\"",
    "\"SUM\"",
    "\"TABLE\"",
    "\"TRANSACTION\"",
    "\"UNION\"",
    "\"UPDATE\"",
    "\"VALUES\"",
    "\"VARCHAR2\"",
    "\"VARCHAR\"",
    "\"WHERE\"",
    "\"WITH\"",
    "\"WORK\"",
    "\"WRITE\"",
    "<S_NUMBER>",
    "<FLOAT>",
    "<INTEGER>",
    "<DIGIT>",
    "<LINE_COMMENT>",
    "<MULTI_LINE_COMMENT>",
    "<S_VARNAME>",
    "<S_IDENTIFIER>",
    "<LETTER>",
    "<SPECIAL_CHARS>",
    "<S_BIND>",
    "<S_CHAR_LITERAL>",
    "<S_QUOTED_IDENTIFIER>",
    "\"(\"",
    "\")\"",
    "\"=\"",
    "\"!=\"",
    "\"#\"",
    "\"<>\"",
    "\">\"",
    "\">=\"",
    "\"<\"",
    "\"<=\"",
    "\"?\"",
    "\"+\"",
    "\"-\"",
    "\"||\"",
    "\"*\"",
    "\"/\"",
    "\"**\"",
    "\".\"",
  };

}
