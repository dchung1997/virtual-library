package com.example.virtuallibrary.config.Listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;

import com.example.virtuallibrary.models.Book;

public class bookDataSkipListener implements SkipListener<Book, Book> {
    
    public bookDataSkipListener() {}

    @Override
    public void onSkipInRead(Throwable throwable) {
        if (throwable instanceof FlatFileParseException exception) {
            String rawLine = exception.getInput();
            int lineNumber = exception.getLineNumber();
            String skippedLine = lineNumber + "|" + rawLine + System.lineSeparator();

            try {
                Files.writeString(Paths.get("target/skippedFiles.log"), skippedLine, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write skipped item " + skippedLine);
            }

        }
    }

}
