package org.openspg.idea.conceptRule.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

/* Auto generated File */
%%

%class ConceptRuleLexer
%implements com.intellij.lexer.FlexLexer, org.openspg.idea.conceptRule.psi.ConceptRuleTypes
%unicode
%public
%column

%function advance
%type IElementType

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////// USER CODE //////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

%{
    private void goToState(int state) {
        yybegin(state);
        yypushback(yylength());
    }

    //-------------------------------------------------------------------------------------------------------------------
    private void trace(String tag) {
        int tokenStart = getTokenStart();
        int tokenEnd = Math.min(tokenStart + 40, zzBuffer.length());
        System.out.println("====" + tag + "\n{{ " + zzBuffer.subSequence(tokenStart, tokenEnd) + " }}");
    }
%}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////// REGEXPS DECLARATIONS //////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// NB !(!a|b) is "a - b"
// From the spec
IDENTIFIER = [:jletter:] [:jletterdigit:]*

DIGIT = [0-9]
DIGIT_OR_UNDERSCORE = [_0-9]
DIGITS = {DIGIT} | {DIGIT} {DIGIT_OR_UNDERSCORE}*
HEX_DIGIT_OR_UNDERSCORE = [_0-9A-Fa-f]

INTEGER_LITERAL = {DIGITS} | {HEX_INTEGER_LITERAL} | {BIN_INTEGER_LITERAL}
LONG_LITERAL = {INTEGER_LITERAL} [Ll]
HEX_INTEGER_LITERAL = 0 [Xx] {HEX_DIGIT_OR_UNDERSCORE}*
BIN_INTEGER_LITERAL = 0 [Bb] {DIGIT_OR_UNDERSCORE}*

FLOAT_LITERAL = ({DEC_FP_LITERAL} | {HEX_FP_LITERAL}) [Ff] | {DIGITS} [Ff]
DOUBLE_LITERAL = ({DEC_FP_LITERAL} | {HEX_FP_LITERAL}) [Dd]? | {DIGITS} [Dd]
DEC_FP_LITERAL = {DIGITS} {DEC_EXPONENT} | {DEC_SIGNIFICAND} {DEC_EXPONENT}?
DEC_SIGNIFICAND = "." {DIGITS} | {DIGITS} "." {DIGIT_OR_UNDERSCORE}*
DEC_EXPONENT = [Ee] [+-]? {DIGIT_OR_UNDERSCORE}*
HEX_FP_LITERAL = {HEX_SIGNIFICAND} {HEX_EXPONENT}
HEX_SIGNIFICAND = 0 [Xx] ({HEX_DIGIT_OR_UNDERSCORE}+ "."? | {HEX_DIGIT_OR_UNDERSCORE}* "." {HEX_DIGIT_OR_UNDERSCORE}+)
HEX_EXPONENT = [Pp] [+-]? {DIGIT_OR_UNDERSCORE}*

LINE = [^\n]*
LINE_COMMENT = "//"{LINE}
BLOCK_COMMENT = "/"\*([^*]|\*+[^*/])*(\*+"/")?
DOUBLE_QUOTED_STRING = \"([^\\\"\r\n]|\\[^\r\n])*\"?
SINGLE_QUOTED_STRING = '([^\\'\r\n]|\\[^\r\n])*'?

EOL =                           "\n"
WHITE_SPACE_CHAR =              [ \t\f]
WHITE_SPACE =                   {WHITE_SPACE_CHAR}+

ESCAPED_SYMBOLIC_NAME =         \`[^`]*\`

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////// STATES DECLARATIONS //////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Main states
%xstate TODO_STATE

%%
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////// RULES declarations ////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//-------------------------------------------------------------------------------------------------------------------
// State in the start of new line in block mode

//-------------------------------------------------------------------------------------------------------------------
// common: white-space, eol, comment
<YYINITIAL> {
    {LINE_COMMENT}           { return LINE_COMMENT;    }
    {BLOCK_COMMENT}          { return BLOCK_COMMENT;   }
    {WHITE_SPACE}            { return TokenType.WHITE_SPACE; }
}

//-------------------------------------------------------------------------------------------------------------------
// rulue block
<YYINITIAL> {
    {EOL}                   { return TokenType.NEW_LINE_INDENT; }

    "[[" { return OPEN_RULE_BLOCK; }
    "]]" { return CLOSE_RULE_BLOCK; }

    {INTEGER_LITERAL}        { return INTEGER_LITERAL; }
    {LONG_LITERAL}           { return INTEGER_LITERAL; }
    {FLOAT_LITERAL}          { return FLOAT_LITERAL;   }
    {DOUBLE_LITERAL}         { return FLOAT_LITERAL;   }
    {DOUBLE_QUOTED_STRING}   { return STRING_LITERAL;  }
    {SINGLE_QUOTED_STRING}   { return STRING_LITERAL;  }

    {ESCAPED_SYMBOLIC_NAME}  { return ESCAPED_SYMBOLIC_NAME; }

    "namespace"              { return NAMESPACE_KEYWORD;       }
    "rule"                   { return WRAPPER_RULE_KEYWORD;    }

    "Action"                 { return ACTION_KEYWORD;          }
    "Define"                 { return DEFINE_KEYWORD;          }
    "DefinePriority"         { return DEFINE_PRIORITY_KEYWORD; }
    "Description"            { return DESCRIPTION_KEYWORD;     }
    "GraphStructure"         { return GRAPH_STRUCTURE_KEYWORD; }
    "Rule"                   { return RULE_KEYWORD;            }
    "createEdgeInstance"     { return ADD_EDGE_KEYWORD;        }
    "createNodeInstance"     { return ADD_NODE_KEYWORD;        }
    "str_join"               { return STR_JOIN_KEYWORD;        }


    [C][Oo][Nn][Ss][Tt][Rr][Aa][Ii][Nn][Tt] {
        return CONSTRAINT_KEYWORD;
    }
    [S][Tt][Rr][Uu][Cc][Tt][Uu][Rr][Ee] {
        return STRUCTURE_KEYWORD;
    }


    [Aa][Bb][Ss]                      { return ABS_KEYWORD;          }
    [Aa][Cc][Cc][Uu][Mm][Uu][Ll][Aa][Tt][Ee] {
        return ACCUMULATE_KEYWORD;
    }
    [Aa][Ll][Ll]                      { return ALL_KEYWORD;          }
    [Aa][Nn][Dd]                      { return AND_KEYWORD;          }
    [Aa][Ss][Cc]                      { return ASC_KEYWORD;          }
    [Aa][Ss]                          { return AS_KEYWORD;           }
    [Aa][Vv][Gg][Ii][Ff]              { return AVGIF_KEYWORD;        }
    [Aa][Vv][Gg]                      { return AVG_KEYWORD;          }
    [Cc][Ee][Ii][Ll][Ii][Nn][Gg]      { return CEIL_KEYWORD;         }
    [Cc][Ee][Ii][Ll]                  { return CEIL_KEYWORD;         }
    [Cc][Oo][Mm][Mm][Ee][Nn][Tt]      { return COMMENT_KEYWORD;      }
    [Cc][Oo][Nn][Cc][Aa][Tt][Aa][Gg][Gg][Ii][Ff] {
        return CONCATAGGIF_KEYWORD;
    }
    [Cc][Oo][Uu][Nn][Tt][Ii][Ff]      { return COUNTIF_KEYWORD;      }
    [Cc][Oo][Uu][Nn][Tt]              { return COUNT_KEYWORD;        }
    [Dd][Ee][Ff][Ii][Nn][Ee][_][Pp][Rr][Ii][Oo][Rr][Ii][Tt][Yy] {
        return DEFINE_PRIORITY_KEYWORD;
    }
    [Dd][Ee][Ss][Cc][Rr][Ii][Pp][Tt][Ii][Oo][Nn] {
        return DESCRIPTION_KEYWORD;
    }
    [Dd][Ee][Ss][Cc]                  { return DESC_KEYWORD;         }
    [Dd][Ii][Ss][Tt][Ii][Nn][Cc][Tt][Gg][Ee][Tt] {
        return DISTINCT_GET_KEYWORD;
    }
    [Dd][Ii][Ss][Tt][Ii][Nn][Cc][Tt][_][Gg][Ee][Tt] {
        return DISTINCT_GET_KEYWORD;
    }
    [Ee][Dd][Gg][Ee][Ss]              { return EDGES_KEYWORD;        }
    [Ee][Xx][Ii][Ss][Tt]              { return EXIST_KEYWORD;        }
    [Ff][Aa][Ll][Ss][Ee]              { return FALSE_KEYWORD;        }
    [Ff][Ll][Oo][Oo][Rr]              { return FLOOR_KEYWORD;        }
    [Gg][Ee][Tt]                      { return GET_KEYWORD;          }
    [Gg][Rr][Oo][Uu][Pp]              { return GROUP_KEYWORD;        }
    [Hh][Ee][Aa][Dd]                  { return HEAD_KEYWORD;         }
    [Ii][Ff]                          { return IF_KEYWORD;           }
    [Ii][Nn]                          { return IN_KEYWORD;           }
    [Ii][Ss]                          { return IS_KEYWORD;           }
    [Ll][Ii][Kk][Ee]                  { return LIKE_KEYWORD;         }
    [Ll][Ii][Mm][Ii][Tt]              { return LIMIT_KEYWORD;        }
    [Mm][Aa][Tt][Cc][Hh]              { return MATCH_KEYWORD;        }
    [Mm][Aa][Xx][Ii][Ff]              { return MAXIF_KEYWORD;        }
    [Mm][Aa][Xx]                      { return MAX_KEYWORD;          }
    [Mm][Ii][Nn][Ii][Ff]              { return MINIF_KEYWORD;        }
    [Mm][Ii][Nn]                      { return MIN_KEYWORD;          }
    [Nn][Oo][Dd][Ee][Ss]              { return NODES_KEYWORD;        }
    [Nn][Oo][Tt]                      { return NOT_KEYWORD;          }
    [Nn][Uu][Ll][Ll]                  { return NULL_KEYWORD;         }
    [Oo][Pp][Tt][Ii][Oo][Nn][Aa][Ll]  { return OPTIONAL_KEYWORD;     }
    [Oo][Rr]                          { return OR_KEYWORD;           }
    [Pp][Aa][Tt][Hh]                  { return PATH_KEYWORD;         }
    [Pp][Ee][Rr][_][Nn][Oo][Dd][Ee][_][Ll][Ii][Mm][Ii][Tt] {
        return PER_NODE_LIMIT_KEYWORD;
    }
    [Pp][Rr][Ii][Oo][Rr][Ii][Tt][Yy]  { return PRIORITY_KEYWORD;     }
    [Rr][Ee][Dd][Uu][Cc][Ee]          { return REDUCE_KEYWORD;       }
    [Rr][Ee][Pp][Ee][Aa][Tt]          { return REPEAT_KEYWORD;       }
    [Rr][Ee][Tt][Uu][Rr][Nn]          { return RETURN_KEYWORD;       }
    [Rr][Ll][Ii][Kk][Ee]              { return RLIKE_KEYWORD;        }
    [Ss][Ll][Ii][Cc][Ee]              { return SLICE_KEYWORD;        }
    [Ss][Qq][Ll]                      { return SQL_KEYWORD;          }
    [Ss][Uu][Mm][Ii][Ff]              { return SUMIF_KEYWORD;        }
    [Ss][Uu][Mm]                      { return SUM_KEYWORD;          }
    [Tt][Aa][Ii][Ll]                  { return TAIL_KEYWORD;         }
    [Tt][Rr][Uu][Ee]                  { return TRUE_KEYWORD;         }
    [Ww][Hh][Ee][Rr][Ee]              { return WHERE_KEYWORD;        }
    [Xx][Oo][Rr]                      { return XOR_KEYWORD;          }

    {IDENTIFIER}  { return UNESCAPED_SYMBOLIC_NAME; }

    "=="     { return EQEQ; }
    "!="     { return NE; }
    "||"     { return OROR; }

    "<-["   { return LEFT_ARROW_BRACKET;  }
    "<-"    { return LEFT_ARROW; }
    "->"    { return RIGHT_ARROW; }
    "<->"   { return BOTH_ARROW; }
    "=>"    { return LAMBDA_ARROW; }

    ">="    { return GE; }
    "<="    { return LE; }
    "<>"    { return NE; }

    "&&"    { return ANDAND; }

    "("     { return LPARENTH; }
    ")"     { return RPARENTH; }
    "{"     { return LBRACE; }
    "}"     { return RBRACE; }
    "["     { return LBRACKET; }
    "]"     { return RBRACKET; }
    ";"     { return SEMICOLON; }
    ","     { return COMMA; }
    "."     { return DOT; }

    "="     { return EQ; }
    "!"     { return EXCL; }
    "?"     { return QUEST; }
    ":"     { return COLON; }
    "+"     { return PLUS; }
    "-"     { return MINUS; }
    "*"     { return ASTERISK; }
    "/"     { return DIV; }
    "|"     { return VBAR; }
    "%"     { return PERC; }

    ">"     { return GT; }
    "<"     { return LT; }


//    "$"     { return DOLLAR_SYMBOL; }
//    "\""    { return DOUBLE_QUOTE; }

    [^] {
          return TokenType.BAD_CHARACTER;
      }
}
