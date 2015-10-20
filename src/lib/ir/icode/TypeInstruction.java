package lib.ir.icode;


public enum TypeInstruction {
    DECLINTARRAY,
    DECLFLOATARRAY,
    DECLBOOLEANARRAY,
    DECLINT,
    DECLFLOAT,
    DECLBOOLEAN,
    MINUSINT,
    MINUSFLOAT,
    NEGATION,
    METHODDECL,
    METHODDECLEXTERN,
    LABEL,
    ASSMNT,
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
    RETURN,
    PUSH,
    CALL,
    JUMP,
    JUMPTRUE,
    JUMPFALSE,
    INC,
    DEC;  
}
