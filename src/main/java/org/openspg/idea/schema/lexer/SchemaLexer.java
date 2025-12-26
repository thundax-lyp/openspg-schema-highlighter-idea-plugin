package org.openspg.idea.schema.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import java.util.LinkedList;

import static com.intellij.psi.TokenType.NEW_LINE_INDENT;
import static org.openspg.idea.schema.psi.SchemaTypes.INDENT;

public class SchemaLexer implements FlexLexer {

    private final _SchemaLexer myDelegate;

    private final LinkedList<SchemaToken> myTokenQueue = new LinkedList<>();
    private int myTokenStart = 0;
    private int myTokenEnd = 0;

    private int virtualState = 0;

    public SchemaLexer(java.io.Reader in) {
        myDelegate = new _SchemaLexer(in);
    }

    @Override
    public void yybegin(int state) {
        throw new IllegalArgumentException("SchemaLexer not implemented yet");
    }

    @Override
    public int yystate() {
        return this.virtualState++;
    }

    @Override
    public int getTokenStart() {
        return myTokenStart;
    }

    @Override
    public int getTokenEnd() {
        return myTokenEnd;
    }

    @Override
    public IElementType advance() throws java.io.IOException {
        IElementType nextToken = this.emit();
        if (nextToken != null) {
            return nextToken;
        }

        nextToken = myDelegate.advance();
        int markStart = myDelegate.getTokenStart();

        while (nextToken == NEW_LINE_INDENT) {
            myTokenQueue.add(new SchemaToken(nextToken, myDelegate.getTokenStart(), myDelegate.getTokenEnd()));
            nextToken = myDelegate.advance();
        }

        if (nextToken == null) {
            myDelegate.flushIndent();
        }

        IElementType queuedToken = myDelegate.pollQueuedToken();
        while (queuedToken != null) {
            if (queuedToken == INDENT) {
                myTokenQueue.add(new SchemaToken(queuedToken, myDelegate.getTokenEnd(), myDelegate.getTokenEnd()));
            } else {
                myTokenQueue.addFirst(new SchemaToken(queuedToken, markStart, markStart));
            }
            queuedToken = myDelegate.pollQueuedToken();
        }

        if (nextToken != null) {
            myTokenQueue.add(new SchemaToken(nextToken, myDelegate.getTokenStart(), myDelegate.getTokenEnd()));
        }

        return this.emit();
    }

    @Override
    public void reset(CharSequence buf, int start, int end, int initialState) {
        myDelegate.reset(buf, start, end, initialState);
    }

    private IElementType emit() {
        SchemaToken token = myTokenQueue.poll();
        if (token == null) {
            myTokenStart = myTokenEnd;
            return null;
        } else {
            myTokenStart = token.tokenStart;
            myTokenEnd = token.tokenEnd;
            return token.elementType;
        }
    }

    public static class SchemaToken {
        protected IElementType elementType;
        protected int tokenStart;
        protected int tokenEnd;

        public SchemaToken(IElementType elementType, int tokenStart, int tokenEnd) {
            this.tokenStart = tokenStart;
            this.tokenEnd = tokenEnd;
            this.elementType = elementType;
        }
    }

}
