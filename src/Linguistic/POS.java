package Linguistic;


/**
 * Created by RobbinNi on 2/9/16.
 */
public enum POS {
    CC,
    CD,
    DT,
    EX,
    FW,
    IN,
    JJ,
    JJR,
    JJS,
    LS,
    MD,
    NN,
    NNS,
    NNP,
    NNPS,
    PDT,
    POS,
    PRP,
    PRP$,
    RB,
    RBR,
    RBS,
    RP,
    SYM,
    SYMFS,
    SYMPE,
    SYMQM,
    TO,
    UH,
    VB,
    VBD,
    VBG,
    VBN,
    VBP,
    VBZ,
    WDT,
    WP,
    WP$,
    WRB,
    OTHER;

    static public POS getPOSTag(String tag) {
        POS ret;
        try {
            if (tag.intern().equals(".") || tag.intern().equals("!")) {
                ret = SYMFS;
            } else if (tag.intern().equals(",")) {
                ret = SYMPE;
            } else if (tag.intern().equals("?")) {
                ret = SYMQM;
            } else if (tag.intern().equals(":") || tag.intern().equals(";") || tag.intern().equals("(") || tag.intern().equals(")") || tag.intern().equals("\"") || tag.intern().equals("$") || tag.intern().equals("#")) {
                ret = SYM;
            } else {
                ret = valueOf(tag);
            }
        } catch (IllegalArgumentException ie) {
            System.out.println("Unknown POS tag : \"" + tag + "\"");
            ret = OTHER;
        }
        return ret;
    }
}
