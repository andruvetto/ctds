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
    MULTFLOAT,
    DIVIDEINT,
    DIVIDEFLOAT,
    MOD,
    LESS,
    LESS_EQ,
    GTR,
    GTR_EQ,
    EQ,
    NOT_EQ,
    AND,
    OR,
    MULTINT,
    SUBINT,
    SUBFLOAT,
    
    //Return
    RETURN,
    
    //Push parameters for methods calls
    PUSH,
    
    //Methods Calls
    CALL,
    
    //Jumps
    JUMP,
    JUMPTRUE,
    JUMPFALSE,
    
    //Increment and decrement
    INC,
    DEC,
    
    //Array Access
    INTARRAYACCESS,
    FLOATARRAYACCESS,
    BOOLARRAYACCESS,
    
    //Array Assignement
    INTARRAYASSMNT,
    FLOATARRAYASSMNT,
    BOOLARRAYASSMNT;
}
