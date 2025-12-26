package org.openspg.idea.schema.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.util.*;

/* Auto generated File */
%%

%class _SchemaLexer
%implements com.intellij.lexer.FlexLexer, org.openspg.idea.schema.psi.SchemaTypes
%unicode
%public
%column

%function advance
%type IElementType

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////// USER CODE //////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

%{
    private final static int TAB_SIZE = 4;

    private final List<Integer> indentList = new ArrayList<>();
    private final Queue<IElementType> tokenQueue = new LinkedList<>();

    protected IElementType pollQueuedToken() {
        return this.tokenQueue.poll();
    }

    protected void flushIndent() {
        for (int i = 0; i < this.indentList.size(); i++) {
            this.tokenQueue.add(DEDENT);
        }
        this.indentList.clear();
    }

    /** @param offset offset from currently matched token start (could be negative) */
    private char getCharAtOffset(final int offset) {
        final int loc = getTokenStart() + offset;
        return 0 <= loc && loc < zzBuffer.length() ? zzBuffer.charAt(loc) : (char) -1;
    }

    private boolean isAfter(String tag) {
        int end = getTokenStart();
        int start = end - tag.length();
        return 0 <= start && end < zzBuffer.length() && zzBuffer.subSequence(start, end).toString().equals(tag);
    }

    private boolean isBefore(String tag) {
        int start = getTokenEnd();
        int end = start + tag.length();
        return 0 <= start && end < zzBuffer.length() && zzBuffer.subSequence(start, end).toString().equals(tag);
    }

    private boolean isAfterEol() {
        return isAfter("\n");
    }

    private int getIndent() {
        assert isAfterEol();
        int indent = yylength();
        for (int i = 0; i < yylength(); i += 1) {
            if (getCharAtOffset(i) == '\t') {
                indent = Math.floorDiv(indent + TAB_SIZE, TAB_SIZE) * TAB_SIZE;
            }
        }
        return indent;
    }

    private IElementType processIndent() {
        int currIndent = getIndent();
        int lastIndent = this.indentList.isEmpty() ? 0 : this.indentList.get(this.indentList.size() - 1);
        if (currIndent > lastIndent) {
            this.indentList.add(currIndent);
            this.tokenQueue.add(INDENT);
            return TokenType.WHITE_SPACE;
        } else if (currIndent == lastIndent) {
            return TokenType.WHITE_SPACE;
        }

        for (int level = 0; level < this.indentList.size(); level ++) {
            if (this.indentList.get(level) == currIndent) {
                while (this.indentList.size() > level + 1) {
                    this.indentList.remove(this.indentList.size() - 1);
                    this.tokenQueue.add(DEDENT);
                }
                return TokenType.WHITE_SPACE;
            }
        }
        return null;
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
ANY_CHAR = [^]

IDENTIFIER = [:jletter:] [:jletterdigit:]*
LETTERDIGIT = [:jletterdigit:]+

DOUBLE_QUOTED_STRING = \"([^\\\"\r\n]|\\[^\r\n])*\"?
SINGLE_QUOTED_STRING = '([^\\'\r\n]|\\[^\r\n])*'?
GRAVE_QUOTED_STRING = \`([^\\`\r\n]|\\[^\r\n])*\`?

PLAIN_TEXT_BLOCK = "[[" {ANY_CHAR}* "]]"

WHITE_SPACE_CHAR = [ \t]
WHITE_SPACE = {WHITE_SPACE_CHAR}+
EOL = "\n"

BLANK_LINE = {WHITE_SPACE_CHAR}*{EOL}
LINE = [^\n]*
COMMENT = "#"{LINE}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////// STATES DECLARATIONS //////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Main states
%xstate LINE_START_STATE, INDENT_STATE

// Small technical one-token states
%xstate NAMESPACE_STATE, LINE_COMMENT_STATE, ERROR_STATE

%xstate DEFINITION_STATE, KV_STATE, WAITING_VALUE_STATE, WAITING_BLOCK_VALUE_STATE
%%
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////// RULES declarations ////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//-------------------------------------------------------------------------------------------------------------------
// State in the start of new line in block mode
<YYINITIAL, LINE_START_STATE> {
    // It is a text, go next state and process it there
    "namespace" {
          this.flushIndent();
          yybegin(NAMESPACE_STATE);
          yypushback(yylength());
      }

    {BLANK_LINE} {
          return TokenType.NEW_LINE_INDENT;
      }

    {WHITE_SPACE}* {COMMENT} {
          yybegin(LINE_COMMENT_STATE);
          yypushback(yylength());
      }

    {WHITE_SPACE} {
          IElementType token = this.processIndent();
          if (token != null) {
              yybegin(INDENT_STATE);
              return token;
          }
          yybegin(ERROR_STATE);
          yypushback(yylength());
      }

    {ANY_CHAR} {
          this.flushIndent();
          yybegin(INDENT_STATE);
          yypushback(yylength());
      }
}
//-------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------
// indent: after whitespac
<INDENT_STATE> {
    {IDENTIFIER} ":" {
          yybegin(KV_STATE);
          yypushback(yylength());
      }

    {ANY_CHAR} {
          yybegin(DEFINITION_STATE);
          yypushback(yylength());
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// common: white-space, eol, comment
<NAMESPACE_STATE, ERROR_STATE, DEFINITION_STATE, KV_STATE, WAITING_VALUE_STATE> {
    {EOL} {
          yybegin(LINE_START_STATE);
          return TokenType.NEW_LINE_INDENT;
      }

    {WHITE_SPACE} {COMMENT} {
          yybegin(LINE_COMMENT_STATE);
          yypushback(yylength());
      }

    {WHITE_SPACE} {
          return TokenType.WHITE_SPACE;
      }
}

<LINE_COMMENT_STATE> {
    {EOL} {
          yybegin(LINE_START_STATE);
          return TokenType.NEW_LINE_INDENT;
      }

    {WHITE_SPACE} {
          return TokenType.WHITE_SPACE;
      }

    {COMMENT} {
          return LINE_COMMENT;
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// namespace
<NAMESPACE_STATE> {
    "namespace" {
        return NAMESPACE_KEYWORD;
    }

    {IDENTIFIER} {
          return IDENTIFIER;
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// common error line
<ERROR_STATE> {
    {ANY_CHAR} {
          return TokenType.BAD_CHARACTER;
      }
}
//-------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------
// definition-state
// name (alias-name) : type
<DEFINITION_STATE> {
    {DOUBLE_QUOTED_STRING}   { return STRING_LITERAL;  }
    {SINGLE_QUOTED_STRING}   { return STRING_LITERAL;  }
    {GRAVE_QUOTED_STRING}    { return STRING_LITERAL;  }

    [Ee][Nn][Tt][Ii][Tt][Yy][Tt][Yy][Pp][Ee]      { return ENTITY_TYPE_KEYWORD; }
    [Cc][Oo][Nn][Cc][Ee][Pp][Tt][Tt][Yy][Pp][Ee]  { return CONCEPT_TYPE_KEYWORD; }
    [Ee][Vv][Ee][Nn][Tt][Tt][Yy][Pp][Ee]          { return EVENT_TYPE_KEYWORD; }
    [Ii][Nn][Dd][Ee][Xx][Tt][Yy][Pp][Ee]          { return INDEX_TYPE_KEYWORD; }
    [Ss][Tt][Aa][Nn][Dd][Aa][Rr][Dd][Tt][Yy][Pp][Ee] { return STANDARD_TYPE_KEYWORD; }
    [Bb][Aa][Ss][Ii][Cc][Tt][Yy][Pp][Ee]          { return BASIC_TYPE_KEYWORD; }
    [Ii][Nn][Tt][Ee][Gg][Ee][Rr]                  { return INTEGER_KEYWORD; }
    [Ff][Ll][Oo][Aa][Tt]  { return FLOAT_KEYWORD; }
    [Tt][Ee][Xx][Tt]      { return TEXT_KEYWORD; }

    {IDENTIFIER} { return IDENTIFIER; }
    {LETTERDIGIT} { return TEXT; }

    "->"    { return RIGHT_ARROW; }
    "("     { return LPARENTH; }
    ")"     { return RPARENTH; }
    ","     { return COMMA; }
    ":"     { return COLON; }
    "#"     { return HASH; }
    "."     { return DOT; }

    {ANY_CHAR} {
          return TokenType.BAD_CHARACTER;
      }
}
//-------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------
// key-value-state
<KV_STATE> {
    [Dd][Ee][Ss][Cc] { return DESC_KEYWORD; }
    [Pp][Rr][Oo][Pp][Ee][Rr][Tt][Ii][Ee][Ss] { return PROPERTIES_KEYWORD; }
    [Rr][Ee][Ll][Aa][Tt][Ii][Oo][Nn][Ss]     { return RELATIONS_KEYWORD; }
    [Hh][Yy][Pp][Ee][Rr][Nn][Yy][Mm][Pp][Rr][Ee][Dd][Ii][Cc][Aa][Tt][Ee] { return HYPERNYMP_PREDICATE_KEYWORD; }
    [Rr][Ee][Gg][Uu][Ll][Aa][Rr] { return REGULAR_KEYWORD; }
    [Ss][Pp][Rr][Ee][Aa][Dd][Aa][Bb][Ll][Ee] { return SPREADABLE_KEYWORD; }
    [Aa][Uu][Tt][Oo][Rr][Ee][Ll][Aa][Tt][Ee] { return AUTORELATE_KEYWORD; }
    [Cc][Oo][Nn][Ss][Tt][Rr][Aa][Ii][Nn][Tt] { return CONSTRAINT_KEYWORD; }
    [Rr][Uu][Ll][Ee] { return RULE_KEYWORD; }
    [Ii][Nn][Dd][Ee][Xx] { return INDEX_KEYWORD; }

    {IDENTIFIER} { return IDENTIFIER; }

    "#" {
          return HASH;
      }

    ":" {
          yybegin(WAITING_VALUE_STATE);
          return COLON;
      }

    {ANY_CHAR} {
          return TokenType.BAD_CHARACTER;
      }
}
//-------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------
// waiting value
<WAITING_VALUE_STATE> {
    {PLAIN_TEXT_BLOCK} {
          yybegin(WAITING_BLOCK_VALUE_STATE);
          yypushback(yylength());
      }

    [Ii][Ss][Aa] {
          return IS_A_KEYWORD;
      }
    [Ll][Oo][Cc][Aa][Tt][Ee][Aa][Tt] {
          return LOCATE_AT_KEYWORD;
      }
    [Mm][Aa][Nn][Nn][Ee][Rr][Oo][ff] {
          return MANNER_OF_KEYWORD;
      }
    [Tt][Ee][Xx][Tt] {
          return TEXT_KEYWORD;
      }
    [Vv][Ee][Cc][Tt][Oo][Rr] {
          return VECTOR_KEYWORD;
      }
    [Ss][Pp][Aa][Rr][Ss][Ee][Vv][Ee][Cc][Tt][Oo][Rr] {
          return SPARSE_VECTOR_KEYWORD;
      }
    [Tt][Ee][Xx][Tt][Aa][Nn][Dd][Vv][Ee][Cc][Tt][Oo][Rr] {
          return TEXT_AND_VECTOR_KEYWORD;
      }
    [Tt][Ee][Xx][Tt][Aa][Nn][Dd][Ss][Pp][Aa][Rr][Ss][Ee][Vv][Ee][Cc][Tt][Oo][Rr] {
          return TEXT_AND_SPARSE_VECTOR_KEYWORD;
      }
    [Nn][Oo][Tt][Nn][Uu][Ll][Ll] {
          return NOT_NULL_KEYWORD;
      }
    [Mm][Uu][Ll][Tt][Ii][Vv][Aa][Ll][Uu][Ee] {
          return MULTI_VALUE_KEYWORD;
      }

    [^ \n]+ {
          return TEXT;
      }
}
//-------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------
// plain-text-block-state
<WAITING_BLOCK_VALUE_STATE> {
    "[[" {
          return DOUBLE_LBRACKET;
      }

    "]]" {
          yybegin(WAITING_VALUE_STATE);
          return DOUBLE_RBRACKET;
      }

    "[" {
          return PLAIN_TEXT;
      }

    "]" {
          return PLAIN_TEXT;
      }

    [ \t\n]+ {
          return isAfter("[[") || isBefore("]]") ? TokenType.WHITE_SPACE : PLAIN_TEXT;
      }

    [^ \t\n\[\]]+ {
          return PLAIN_TEXT;
      }
}
//-------------------------------------------------------------------------------------------------------------------
