package com.example.xmlacdat.pojo;

import java.io.File;

/**
 * When something is OK!
 */

public class SuccessEvent {

    public final String msg;
    public final File file;

    public SuccessEvent(String m, File f)
    {
        this.msg = m;
        this.file = f;
    }

}
