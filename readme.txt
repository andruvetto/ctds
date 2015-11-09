Taller de Diseño de Software - UNRC

Compiler for a simple programming language.

Andruvetto Daniel - Palacios Lucas

for compile source:
    ./compile.sh
    
running:
    ./ctds.sh [options] inputfile [externalsFiles]
        options:
            -o outputName       
            (if the option -o is not specified, the default output is "a").

            -target scan        (Lexical Analyzer)
                    parse       (syntax analyzer)
                    semantic    (Sematic analizer)
                    interpreter (Run interpreter)
		            icode	    (intermediate code)
		            asm 	    (Generate Assembly outputName.s)
            (if the option -target is not specified, it creates the executable outputName.out).
    
runing tests:
    ./tests.sh
    
