package parserlexer;
import java_cup.runtime.*;
import java.util.ArrayList;
import java.util.List;
%%

%public
%class Lexer
%cup
%line
%column
%full
%char

/* States definitions */
%state LITERALS
%state TYPES
%state ARRAYS
%state OPERATORS
%state BLOCKS
%state CONTROL_STRUCTURES
%state IO_OPERATORS

%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
    
    //Lista de errores controlados
    public List<Symbol> lexErrorList = new ArrayList<>();
    
    // Agrega errores controlados a la lista
    public void logError(int column,int line,String text) {
        Symbol symbol = new Symbol(0, column, line, text);
        lexErrorList.add(symbol);
    }
%}

/* Macros for reusable regular expression patterns */
// Literals
integerLiteral       = [-]?0|[-]?[1-9][0-9]*
floatLiteral         = {integerLiteral}"."[0-9]+
boolLiteral          = "true" | "false"
characterLiteral     = '[^']*'
stringLiteral        = \"([^\"\\]|\\.)*\"

// Comments
LineTerminator       = \r|\n|\r\n
WhiteSpace           = {LineTerminator} | [ \t\f]
InputCharacter       = [^\r\n]
Comment              = {EndOfLineComment} | {DocumentationComment}
EndOfLineComment     = "#" {InputCharacter}* {LineTerminator}?
DocumentationComment = "\\_" {CommentContent} "_"+ "/"
CommentContent       = [^\\]*

// Others
BlankSpace           = [ \t\r]
newLine              = [\n]
%%

/* keywords */

<YYINITIAL> {
    /* Identifier Main */
    "_verano_"                      { return new Symbol(sym.Main, yycolumn, yyline, yytext()); }

    /* Identifiers */
    _[a-zA-Z0-9]+_                  { return new Symbol(sym.Identifier, yycolumn, yyline, yytext()); }

    /* Comments */
    {Comment}                       { /* Ignorará comentarios*/ }
    
    /* New line */
    {newLine}                       { /* Ignorará nuevas lineas */ }

    /* Blank spaces */
    {BlankSpace}+                   { /* Ignorará espacios en blanco */ }
}

<YYINITIAL, LITERALS> {
    /* Integer literal */
    {integerLiteral}                { return new Symbol(sym.IntegerLiteral, yycolumn, yyline, yytext()); }

    /* Float literal */
    {floatLiteral}                  { return new Symbol(sym.FloatLiteral, yycolumn, yyline, yytext()); }

    /* Boolean literal */
    {boolLiteral}                   { return new Symbol(sym.BoolLiteral, yycolumn, yyline, yytext()); }

    /* Character literal */
    {characterLiteral}              { return new Symbol(sym.CharacterLiteral, yycolumn, yyline, yytext()); }

    /* String literal */
    {stringLiteral}                 { return new Symbol(sym.StringLiteral, yycolumn, yyline, yytext()); }

}

<YYINITIAL, TYPES> {
    "rodolfo"                       { return new Symbol(sym.Integer, yycolumn, yyline, yytext()); }
    "bromista"                      { return new Symbol(sym.Float, yycolumn, yyline, yytext()); }
    "trueno"                        { return new Symbol(sym.Bool, yycolumn, yyline, yytext()); }
    "cupido"                        { return new Symbol(sym.Char, yycolumn, yyline, yytext()); }
    "cometa"                        { return new Symbol(sym.String, yycolumn, yyline, yytext()); }
}

<YYINITIAL, ARRAYS> {
    /* Unidimensional array for integer or char */
    "abreempaque"                   { return new Symbol(sym.BracketOpening, yycolumn, yyline, yytext()); }
    "cierraempaque"                 { return new Symbol(sym.BracketClosure, yycolumn, yyline, yytext()); }
}

<YYINITIAL, OPERATORS> {
    /* Assignment sign */
    "entrega"                       { return new Symbol(sym.AssignmentSign, yycolumn, yyline, yytext()); }

    /* Parenthesis */
    "abreregalo"                    { return new Symbol(sym.ParenthesisOpening, yycolumn, yyline, yytext()); }
    "cierraregalo"                  { return new Symbol(sym.ParenthesisClosure, yycolumn, yyline, yytext()); }

    /* Binary arithmetic expressions */
    "navidad"                       { return new Symbol(sym.Sum, yycolumn, yyline, yytext()); }
    "intercambio"                   { return new Symbol(sym.Subtraction, yycolumn, yyline, yytext()); }
    "reyes"                         { return new Symbol(sym.Division, yycolumn, yyline, yytext()); }
    "nochebuena"                    { return new Symbol(sym.Multiplication, yycolumn, yyline, yytext()); }
    "magos"                         { return new Symbol(sym.Module, yycolumn, yyline, yytext()); }
    "adviento"                      { return new Symbol(sym.Power, yycolumn, yyline, yytext()); }

    /* Unary arithmetic expressions */
    "quien"                         { return new Symbol(sym.Increment, yycolumn, yyline, yytext()); }
    "grinch"                        { return new Symbol(sym.Decrement, yycolumn, yyline, yytext()); }
    "-"                             { return new Symbol(sym.Negative, yycolumn, yyline, yytext()); } 

    /* Relational expressions */
    "snowball"                      { return new Symbol(sym.Less, yycolumn, yyline, yytext()); }
    "evergreen"                     { return new Symbol(sym.LessEqual, yycolumn, yyline, yytext()); }
    "minstix"                       { return new Symbol(sym.Greater, yycolumn, yyline, yytext()); }
    "upatree"                       { return new Symbol(sym.GreaterEqual, yycolumn, yyline, yytext()); }
    "mary"                          { return new Symbol(sym.Equal, yycolumn, yyline, yytext()); }
    "openslae"                      { return new Symbol(sym.NotEqual, yycolumn, yyline, yytext()); }

    /* Logical expressions */
    "melchor"                       { return new Symbol(sym.Conjunction, yycolumn, yyline, yytext()); }
    "gaspar"                        { return new Symbol(sym.Disjunction, yycolumn, yyline, yytext()); }
    "baltazar"                      { return new Symbol(sym.Negation, yycolumn, yyline, yytext()); }
}

<YYINITIAL, BLOCKS> {
    /* Block opening  */
    "abrecuento"                    { return new Symbol(sym.BlockOpening, yycolumn, yyline, yytext()); }
    
    /* Block closing */
    "cierracuento"                  { return new Symbol(sym.BlockClosure, yycolumn, yyline, yytext()); }

    /* End sentence */
    "finregalo"                     { return new Symbol(sym.EndSentence, yycolumn, yyline, yytext()); }
}

<YYINITIAL, CONTROL_STRUCTURES> {
    "elfo"                          { return new Symbol(sym.If, yycolumn, yyline, yytext()); }
    "hada"                          { return new Symbol(sym.Else, yycolumn, yyline, yytext()); }
    "envuelve"                      { return new Symbol(sym.While, yycolumn, yyline, yytext()); }
    "duende"                        { return new Symbol(sym.For, yycolumn, yyline, yytext()); }
    "varios"                        { return new Symbol(sym.Switch, yycolumn, yyline, yytext()); }
    "historia"                      { return new Symbol(sym.Case, yycolumn, yyline, yytext()); }
    "ultimo"                        { return new Symbol(sym.Default, yycolumn, yyline, yytext()); }
    "corta"                         { return new Symbol(sym.Break, yycolumn, yyline, yytext()); }
    "envia"                         { return new Symbol(sym.Return, yycolumn, yyline, yytext()); }
    "sigue"                         { return new Symbol(sym.Colon, yycolumn, yyline, yytext()); }
    ","                             { return new Symbol(sym.Comma, yycolumn, yyline, yytext()); }
}

<YYINITIAL, IO_OPERATORS> {
    "narra"                         { return new Symbol(sym.Print, yycolumn, yyline, yytext()); }
    "escucha"                       { return new Symbol(sym.Read, yycolumn, yyline, yytext()); }

}

    /* Error de analisis */
    .                               { logError(yycolumn, yyline, yytext());}