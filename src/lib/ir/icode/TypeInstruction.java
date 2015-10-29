package lib.ir.icode;


public enum TypeInstruction {
    
    //Global Declarations
    DECLINTARRAY,
    DECLFLOATARRAY,
    DECLBOOLEANARRAY,
    DECLINT,
    DECLFLOAT,
    DECLBOOLEAN,
    
    //Unoperations
    MINUSINT,
    MINUSFLOAT,
    NEGATION,
    
    //Methods Declaraions
    METHODDECL,
    METHODDECLEXTERN,
    
    //Label
    LABEL,
    
    //Assignement
    ASSMNT,
    
    //BinOperations
    SUMINT,
    SUMFLOAT,
    SUBINT,
    SUBFLOAT,
    MULTINT,
    MULTFLOAT,
    DIVIDEINT,
    DIVIDEFLOAT,
    MOD,
    EQ,
    NOT_EQ,
    LESS,
    LESS_EQ,
    GTR,
    GTR_EQ,
    AND,
    OR,
    

    
    //Return
    RETURN,
    
    //Push parameters for methods calls
    PUSH,
    
    //Methods Calls
    CALL,
    
    //Jumps
    JUMP,
    //JUMPTRUE,
    JUMPFALSE,
    
    //Increment and decrement
    INC,
    //DEC,
    
    //Array Access
    ARRAYACCESS,
    //FLOATARRAYACCESS,
    //BOOLARRAYACCESS,
    
    //Array Assignement
    ARRAYASSMNT;
    //INTARRAYASSMNT,
    //FLOATARRAYASSMNT,
    //BOOLARRAYASSMNT;
}
