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
  int K_ANY = 7;
  /** RegularExpression Id. */
  int K_AS = 8;
  /** RegularExpression Id. */
  int K_ASC = 9;
  /** RegularExpression Id. */
  int K_AVG = 10;
  /** RegularExpression Id. */
  int K_BETWEEN = 11;
  /** RegularExpression Id. */
  int K_BINARY_INTEGER = 12;
  /** RegularExpression Id. */
  int K_BOOLEAN = 13;
  /** RegularExpression Id. */
  int K_BY = 14;
  /** RegularExpression Id. */
  int K_CHAR = 15;
  /** RegularExpression Id. */
  int K_COMMENT = 16;
  /** RegularExpression Id. */
  int K_COMMIT = 17;
  /** RegularExpression Id. */
  int K_CONNECT = 18;
  /** RegularExpression Id. */
  int K_COUNT = 19;
  /** RegularExpression Id. */
  int K_DATE = 20;
  /** RegularExpression Id. */
  int K_DELETE = 21;
  /** RegularExpression Id. */
  int K_DESC = 22;
  /** RegularExpression Id. */
  int K_DISTINCT = 23;
  /** RegularExpression Id. */
  int K_EXCLUSIVE = 24;
  /** RegularExpression Id. */
  int K_EXISTS = 25;
  /** RegularExpression Id. */
  int K_EXIT = 26;
  /** RegularExpression Id. */
  int K_FLOAT = 27;
  /** RegularExpression Id. */
  int K_FOR = 28;
  /** RegularExpression Id. */
  int K_FROM = 29;
  /** RegularExpression Id. */
  int K_GROUP = 30;
  /** RegularExpression Id. */
  int K_HAVING = 31;
  /** RegularExpression Id. */
  int K_IN = 32;
  /** RegularExpression Id. */
  int K_INSERT = 33;
  /** RegularExpression Id. */
  int K_INTEGER = 34;
  /** RegularExpression Id. */
  int K_INTERSECT = 35;
  /** RegularExpression Id. */
  int K_INTO = 36;
  /** RegularExpression Id. */
  int K_IS = 37;
  /** RegularExpression Id. */
  int K_LIKE = 38;
  /** RegularExpression Id. */
  int K_LOCK = 39;
  /** RegularExpression Id. */
  int K_MAX = 40;
  /** RegularExpression Id. */
  int K_MIN = 41;
  /** RegularExpression Id. */
  int K_MINUS = 42;
  /** RegularExpression Id. */
  int K_MODE = 43;
  /** RegularExpression Id. */
  int K_NATURAL = 44;
  /** RegularExpression Id. */
  int K_NOT = 45;
  /** RegularExpression Id. */
  int K_NOWAIT = 46;
  /** RegularExpression Id. */
  int K_NULL = 47;
  /** RegularExpression Id. */
  int K_NUMBER = 48;
  /** RegularExpression Id. */
  int K_OF = 49;
  /** RegularExpression Id. */
  int K_ONLY = 50;
  /** RegularExpression Id. */
  int K_OR = 51;
  /** RegularExpression Id. */
  int K_ORDER = 52;
  /** RegularExpression Id. */
  int K_PRIOR = 53;
  /** RegularExpression Id. */
  int K_QUIT = 54;
  /** RegularExpression Id. */
  int K_READ = 55;
  /** RegularExpression Id. */
  int K_REAL = 56;
  /** RegularExpression Id. */
  int K_ROLLBACK = 57;
  /** RegularExpression Id. */
  int K_ROW = 58;
  /** RegularExpression Id. */
  int K_SELECT = 59;
  /** RegularExpression Id. */
  int K_SET = 60;
  /** RegularExpression Id. */
  int K_SHARE = 61;
  /** RegularExpression Id. */
  int K_SMALLINT = 62;
  /** RegularExpression Id. */
  int K_START = 63;
  /** RegularExpression Id. */
  int K_SUM = 64;
  /** RegularExpression Id. */
  int K_TABLE = 65;
  /** RegularExpression Id. */
  int K_TRANSACTION = 66;
  /** RegularExpression Id. */
  int K_UNION = 67;
  /** RegularExpression Id. */
  int K_UPDATE = 68;
  /** RegularExpression Id. */
  int K_VALUES = 69;
  /** RegularExpression Id. */
  int K_VARCHAR2 = 70;
  /** RegularExpression Id. */
  int K_VARCHAR = 71;
  /** RegularExpression Id. */
  int K_WHERE = 72;
  /** RegularExpression Id. */
  int K_WITH = 73;
  /** RegularExpression Id. */
  int K_WORK = 74;
  /** RegularExpression Id. */
  int K_WRITE = 75;
  /** RegularExpression Id. */
  int S_NUMBER = 76;
  /** RegularExpression Id. */
  int FLOAT = 77;
  /** RegularExpression Id. */
  int INTEGER = 78;
  /** RegularExpression Id. */
  int DIGIT = 79;
  /** RegularExpression Id. */
  int LINE_COMMENT = 80;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 81;
  /** RegularExpression Id. */
  int S_VARNAME = 82;
  /** RegularExpression Id. */
  int S_IDENTIFIER = 83;
  /** RegularExpression Id. */
  int LETTER = 84;
  /** RegularExpression Id. */
  int SPECIAL_CHARS = 85;
  /** RegularExpression Id. */
  int S_BIND = 86;
  /** RegularExpression Id. */
  int S_CHAR_LITERAL = 87;
  /** RegularExpression Id. */
  int S_QUOTED_IDENTIFIER = 88;

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
