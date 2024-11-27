/*

Written by Andy Duvernweau
This file is part of the Free Software Foundation (FSS) under one or more contributor
license agreements. See the NOTICE file distributed  with this work for
information on how to obtain a copy of   the License at http

 */

package org.example;

import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.util.DateTime;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;


public class Main {
    public static void main(String[] args) throws IOException {

        Storage.Builder builder = new Storage.Builder(new HttpTransport() {
            @Override
            protected LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new LowLevelHttpRequest() {
                    @Override
                    public void addHeader(String name, String value) throws IOException {

                    }

                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        return null;
                    }
                };
            }
        }, new JsonFactory() {
            @Override
            public JsonParser createJsonParser(InputStream in) throws IOException {
                return null;
            }

            @Override
            public JsonParser createJsonParser(InputStream in, Charset charset) throws IOException {
                return null;
            }

            @Override
            public JsonParser createJsonParser(String value) throws IOException {
                return null;
            }

            @Override
            public JsonParser createJsonParser(Reader reader) throws IOException {

                return null;
            }

            @Override
            public JsonGenerator createJsonGenerator(OutputStream out, Charset enc) throws IOException {
                return null;
            }

            @Override
            public JsonGenerator createJsonGenerator(Writer writer) throws IOException {
                return null;
            }
        }, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        });

        // this line creates the storage by chaining multiple calls to create and initialize the storage.
        Storage storage = builder.setApplicationName("Storage API Sample").
                setRootUrl("?").build();
        Folder folder =  storage.folders().get("?","?").execute();

        if(folder != null){
            folder.setCreateTime(new DateTime(new Date()));
            DateTime dateTime =  folder.getCreateTime();

            if(dateTime != null){


                System.out.println("Folder created successfully with creation time: " + dateTime.toStringRfc3339());
            }
            folder.setUpdateTime(new DateTime(new Date()));

            Bucket bucket = storage.buckets().get(folder.getBucket()).execute();
            if(bucket!= null){
                System.out.println("Bucket: " + bucket.getName());
                System.out.println("Bucket location: " + bucket.getLocation());
                System.out.println(bucket.getEtag());
            }

            GoogleLongrunningOperation googleLongrunningOperation = storage.operations()
                    .get(folder.getName(), folder.getId()).execute();

            if(googleLongrunningOperation != null){

                System.out.println("Operation name: " + googleLongrunningOperation.getName());
                GoogleRpcStatus googleRpcStatus =  googleLongrunningOperation.getError();
                if(googleRpcStatus !=  null){

                    System.out.println("Error message: " + googleRpcStatus.getMessage());
                }

            }

        }
    }
}