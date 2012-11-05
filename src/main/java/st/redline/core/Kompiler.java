/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.core;

import java.io.FileOutputStream;
import java.io.IOException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import st.redline.compiler.*;

// Called Kompiler rather than Compiler because we have a Smalltalk class Compiler
// in same namespace.

public class Kompiler {

    private final SourceFile sourceFile;
    private final boolean verbose;
    private boolean ignoreCompilerErrors;

    public Kompiler(SourceFile sourceFile, boolean verbose, boolean ignoreCompilerErrors) {
        this.sourceFile = sourceFile;
        this.verbose = verbose;
        this.ignoreCompilerErrors = ignoreCompilerErrors;
    }

    static int filesCount = 0;
    protected byte[] compile() {
    	 byte[] rtn =  compileSource(preprocessSource());
    	 try{
            String outputFileName = "C:\\Users\\7\\redline-smalltalk\\f"+filesCount +".chad.class"; filesCount++;
            FileOutputStream f = new FileOutputStream(outputFileName); 
            f.write(rtn);
            f.close();
            System.out.println("wrote " + outputFileName);
            }
          catch (IOException e ) { throw new RedlineException(e);}
        return rtn;
    }

    private byte[] compileSource(Source source) {
        Program program = parse(source.source());
        Analyser analyser = analyse(program);
        return analyser.classBytes();
    }

    private Analyser analyse(Program program) {
        Analyser analyser = analyser();
        program.accept(analyser);
        return analyser;
    }

    private Analyser analyser() {
        return new Analyser(sourceFile.shortName(), sourceFile.packageName(), verbose);
    }

    private Program parse(String sourceCode) {
        SmalltalkLexer smalltalkLexer = lexorOn(sourceCode);
        SmalltalkParser smalltalkParser = parserUsing(smalltalkLexer);
        try {
            Program program = smalltalkParser.program();
            if (!ignoreCompilerErrors && smalltalkParser.getNumberOfSyntaxErrors() > 0)
                throw RedlineException.withMessage("Syntax error(s) detected");
            return program;
        } catch (RecognitionException e) {
            throw RedlineException.withCause(e);
        }
    }

    private SmalltalkParser parserUsing(SmalltalkLexer smalltalkLexer) {
        return new SmalltalkParser(new CommonTokenStream(smalltalkLexer));
    }

    private SmalltalkLexer lexorOn(String sourceCode) {
        return new SmalltalkLexer(new ANTLRStringStream(sourceCode));
    }

    private Source preprocessSource() {
        return preprocessor().parse(sourceFile);
    }

    private Preprocessor preprocessor() {
        return new Preprocessor();
    }
}
