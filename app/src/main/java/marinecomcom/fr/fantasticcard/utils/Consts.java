package marinecomcom.fr.fantasticcard.utils;

/**
 * Created by Technique on 07/02/16.
 */
public final class Consts  {

    public static final String INSIDE_PAGE  = "inside";
    public static final String OUTSIDE_PAGE = "outside";
    public static final int    NB_PHOTOS    = 6;

    // PRIVATE //

    /**
     The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private Consts(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}