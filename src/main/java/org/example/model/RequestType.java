package org.example.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface RequestType {
    @Retention(RetentionPolicy.RUNTIME)//Because of @Retention(RetentionPolicy.RUNTIME),
    // your server code can scan this method while the app is running, find the @Get annotation, and register this method
    // for /api/hello.
    @Target(ElementType.METHOD)  //ElementType.TYPE – for classes // ElementType.FIELD – for variables
    // ElementType.METHOD – for methods (what we want!)// ElementType.PARAMETER, etc.
    public @interface Get {
        String value();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Put {
        String value();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Post {
        String value();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Delete {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface RequestParam {
        String value();
    }


}
