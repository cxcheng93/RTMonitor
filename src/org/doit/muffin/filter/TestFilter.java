package org.doit.muffin.filter;
import java.lang.Object;
import org.doit.muffin.*;

import org.doit.io.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.FileWriter;
import java.io.IOException;


public class TestFilter implements RequestFilter, ReplyFilter {
    Prefs prefs;
    Test factory;
    Reply reply;
    Request request;

    long startTime = 0;
    long receiveTime = 0;
    private String url;

    public TestFilter(Test factory) {
        this.factory = factory;
    }

    public void setPrefs(Prefs prefs) {
        this.prefs = prefs;
    }

    public void filter(Request r) throws FilterException {
        startTime = System.currentTimeMillis();
        url = r.getURL();        
    }

    public void filter(Reply r) throws FilterException {
       receiveTime = System.currentTimeMillis();
        System.out.println(url + "\nStart: " + startTime + " Received: " + receiveTime);
        System.out.println("\nServed in " + (receiveTime - startTime) + " ms\n");
    }

    public void close() {
    }
}

