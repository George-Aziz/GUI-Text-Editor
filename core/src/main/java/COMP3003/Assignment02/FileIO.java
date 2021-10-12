package COMP3003.Assignment02;

import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.text.Normalizer;
import java.util.stream.Collectors;

/*******************************************************************************************************
 * Author: George Aziz
 * Purpose: Performs the reading/parsing and writing of the files containing general text for text area
 * Date Last Modified: 10/10/2021
 ******************************************************************************************************/
public class FileIO
{
    //Loads a file from a file to load into text area of program
    public String  load(File file, Charset encoding) throws FileLoadingException
    {
        String lines;
        //Decoder for file reading
        CharsetDecoder decoder = encoding.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        try
        {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), decoder);
            try (BufferedReader br = new BufferedReader(isr)) //Reads file with user chosen encoding
            {
                lines = br.lines().collect(Collectors.joining("\n"));
            }
        }
        catch (IOException | UncheckedIOException ex) //Catches for UncheckedIOException which is thrown from br.lines.collect()
        {
            throw new FileLoadingException(ex.getMessage(), ex);
        }

        return Normalizer.normalize(lines, Normalizer.Form.NFKC);
    }

    // Writes a text area text to a given file in a specified encoding UTF-8, UTF-16 or UTF-32
    public void save(File file, Charset encoding, TextArea textArea) throws FileLoadingException
    {
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
             BufferedWriter writer = new BufferedWriter(osw)) {

            writer.write(Normalizer.normalize(textArea.getText(), Normalizer.Form.NFKC));
        }
        catch (IOException ex)
        {
            throw new FileLoadingException(ex.getMessage(), ex);
        }
    }
}
