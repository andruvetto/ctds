/*-***
 *
 * This file defines a stand-alone lexical analyzer for a subset of the Pascal
 * programming language.  This is the same lexer that will later be integrated
 * with a CUP-based parser.  Here the lexer is driven by the simple Java test
 * program in ./PascalLexerTest.java, q.v.  See 330 Lecture Notes 2 and the
 * Assignment 2 writeup for further discussion.
 *
 */



import java_cup.runtime.*;

%%
/*-*
 * LEXICAL FUNCTIONS:
 */

%cup
//%standalone
%line
%column
%unicode
%class CtdsLexer


/*-*
 * PATTERN DEFINITIONS:
 */
alpha =             [A-Za-z]
digit =             [0-9]
alpha_num =         {alpha}|{digit}|[_]
id =                {alpha}({alpha_num})*
int_literal =       {digit}{digit}*
float_literal =     {int_literal}\.{int_literal}
leftbrace =         \{
rightbrace =        \}
lineterminator =    \r|\n|\r\n
inputcharacter =    [^\r\n]
whitespace     =    {lineterminator} | [ \t\f]

/* comments */
comment =           {traditionalcomment} | {endoflinecomment} 
traditionalcomment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
endoflinecomment     = "//" {inputcharacter}* {lineterminator}?


%%
/**
 * LEXICAL RULES:
 */

else            {return new Symbol(sym.ELSE,yyline,yycolumn,yytext());}
if              {return new Symbol(sym.IF,yyline,yycolumn,yytext());}
true            {return new Symbol(sym.TRUE,yyline,yycolumn,yytext());}
false           {return new Symbol(sym.FALSE,yyline,yycolumn,yytext());}
boolean         {return new Symbol(sym.BOOLEAN,yyline,yycolumn,yytext());}
break           {return new Symbol(sym.BREAK,yyline,yycolumn,yytext());}
class           {return new Symbol(sym.CLASS,yyline,yycolumn,yytext());}
continue        {return new Symbol(sym.CONTINUE,yyline,yycolumn,yytext());}
float           {return new Symbol(sym.FLOAT,yyline,yycolumn,yytext());}
for             {return new Symbol(sym.FOR,yyline,yycolumn,yytext());}
int             {return new Symbol(sym.INT,yyline,yycolumn,yytext());}
return          {return new Symbol(sym.RETURN,yyline,yycolumn,yytext());}
void            {return new Symbol(sym.VOID,yyline,yycolumn,yytext());}
while           {return new Symbol(sym.WHILE,yyline,yycolumn,yytext());}
extern          {return new Symbol(sym.EXTERN,yyline,yycolumn,yytext());}

"*"             {return new Symbol(sym.TIMES,yyline,yycolumn,yytext());}
"+"             {return new Symbol(sym.PLUS,yyline,yycolumn,yytext());}
"-"             {return new Symbol(sym.MINUS,yyline,yycolumn,yytext());}
"/"             {return new Symbol(sym.DIVIDE,yyline,yycolumn,yytext());}
"%"             {return new Symbol(sym.MOD,yyline,yycolumn,yytext());}

"&&"            {return new Symbol(sym.AND,yyline,yycolumn,yytext());}
"||"            {return new Symbol(sym.OR,yyline,yycolumn,yytext());}
"!"             {return new Symbol(sym.LOGIC_NEGATION,yyline,yycolumn,yytext());}

"("             {return new Symbol(sym.LEFT_PAREN,yyline,yycolumn,yytext());}
")"             {return new Symbol(sym.RT_PAREN,yyline,yycolumn,yytext());}
"["             {return new Symbol(sym.LEFT_BRKT,yyline,yycolumn,yytext());}
"]"             {return new Symbol(sym.RT_BRKT,yyline,yycolumn,yytext());}

";"             {return new Symbol(sym.SEMI,yyline,yycolumn,yytext());}
"."             {return new Symbol(sym.POINT,yyline,yycolumn,yytext());}
","             {return new Symbol(sym.COMMA,yyline,yycolumn,yytext());}

"=="            {return new Symbol(sym.EQ,yyline,yycolumn,yytext());}
"<"             {return new Symbol(sym.GTR,yyline,yycolumn,yytext());}
">"             {return new Symbol(sym.LESS,yyline,yycolumn,yytext());}
"<="            {return new Symbol(sym.LESS_EQ,yyline,yycolumn,yytext());}
">="            {return new Symbol(sym.GTR_EQ,yyline,yycolumn,yytext());}
"!="            {return new Symbol(sym.NOT_EQ,yyline,yycolumn,yytext());}

"="             {return new Symbol(sym.ASSMNT,yyline,yycolumn,yytext());}
"+="            {return new Symbol(sym.ASSMNT_INC,yyline,yycolumn,yytext());}
"-="            {return new Symbol(sym.ASSMNT_DEC,yyline,yycolumn,yytext());}

{id}            {return new Symbol(sym.ID,yyline,yycolumn,yytext());}

//{int_literal}   {System.out.println("INT_LITERAL"); return new Symbol(sym.INT_LITERAL, new Integer(yytext()));}
//{float_literal} {System.out.println("FLOAT_LITERAL");return new Symbol(sym.FLOAT_LITERAL, new Double(yytext())); }

{int_literal}   {return new Symbol(sym.INT_LITERAL,yyline,yycolumn,yytext());}
{float_literal} {return new Symbol(sym.FLOAT_LITERAL,yyline,yycolumn,yytext()); }


{leftbrace}     {return new Symbol(sym.LEFTBRACE,yyline,yycolumn,yytext());}
{rightbrace}    {return new Symbol(sym.RIGHTBRACE,yyline,yycolumn,yytext());}


{comment}       {/* Ignore comments*/}
{whitespace}    { /* Ignore whitespace. */ }
.               {return new Symbol(sym.ERROR,yyline,yycolumn,yytext());}
                
