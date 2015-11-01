Taller de Diseño de Software - UNRC

Compiler for a simple programming language.

Andruvetto Daniel - Palacios Lucas

for compile source:
    ./compile.sh
    
running:
    ./ctds.sh [options] inputfile
        options:
            -target scan    (Lexical Analyzer)
                    parse   (syntax analyzer)
                    semantic  (Sematic analizer)
                    interpreter (Run interpreter)
		    		icode	(intermediate code)
		    		asm 	(Generate Assembly)
                    (Default is asm)
                    
    
runing tests:
    ./tests.sh
    