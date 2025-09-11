package org.libpag;

public class PAG {
    /**
     * Get SDK version information.
     */
    public static native String SDKVersion();

    static {
        LibraryLoadUtils.loadPag4j();
    }
}
