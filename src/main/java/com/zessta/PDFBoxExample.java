package com.zessta;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDStream;

/**
 * PDFBoxExample
 */
public class PDFBoxExample {

    public static PDDocument replaceText(PDDocument document, String searchString, String replacement)
            throws IOException {
        if (org.apache.commons.lang3.StringUtils.isEmpty(searchString)
                || org.apache.commons.lang3.StringUtils.isEmpty(replacement)) {
            return document;
        }
        PDPageTree pages = document.getDocumentCatalog().getPages();
        for (PDPage page : pages) {
            PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            java.util.List<Object> tokens = parser.getTokens();
            for (int j = 0; j < tokens.size(); j++) {
                Object next = tokens.get(j);
                if (next instanceof org.apache.pdfbox.contentstream.operator.Operator) {
                    org.apache.pdfbox.contentstream.operator.Operator op = (org.apache.pdfbox.contentstream.operator.Operator) next;
                    // Tj and TJ are the two operators that display strings in a PDF
                    if (op.getName().equals("Tj")) {
                        // Tj takes one operator and that is the string to display so lets update that
                        // operator
                        COSString previous = (COSString) tokens.get(j - 1);
                        String string = previous.getString();
                        string = string.replaceFirst(searchString, replacement);
                        previous.setValue(string.getBytes());
                    } else if (op.getName().equals("TJ")) {
                        COSArray previous = (COSArray) tokens.get(j - 1);
                        for (int k = 0; k < previous.size(); k++) {
                            Object arrElement = previous.getObject(k);
                            if (arrElement instanceof COSString) {
                                COSString cosString = (COSString) arrElement;
                                String string = cosString.getString();
                                string = org.apache.commons.lang3.StringUtils.replaceOnce(string, searchString,
                                        replacement);
                                cosString.setValue(string.getBytes());
                            }
                        }
                    }
                }
            }
            // now that the tokens are updated we will replace the page content stream.
            PDStream updatedStream = new PDStream(document);
            OutputStream out = updatedStream.createOutputStream();
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokens);
            page.setContents(updatedStream);
            out.close();
        }
        return document;
    }

    public static void main(String[] args) {
        try {
           replaceText(
                    PDDocument.load(new File(
                            "D://Downloads//pdf-redactor-master//pdf-redactor-master//tests//test-ssns.pdf")),
                    "522", "XXX").save("lol.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}