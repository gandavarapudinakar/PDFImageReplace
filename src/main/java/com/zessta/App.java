package com.zessta;


// import com.aspose.pdf.Color;
import com.aspose.pdf.Document;
// import com.aspose.pdf.FontRepository;
import com.aspose.pdf.TextFragment;
import com.aspose.pdf.TextFragmentAbsorber;
import com.aspose.pdf.TextFragmentCollection;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Document pdfDocument = new Document("D://Downloads//Test_New_Ad_Hoc_Jun 21, 2019 at 12-00 PM.pdf");

        // Create TextAbsorber object to find all instances of the input search phrase
        TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber("HIGH");
        
        // Accept the absorber for first page of document
        pdfDocument.getPages().accept(textFragmentAbsorber);
        
        // Get the extracted text fragments into collection
        TextFragmentCollection textFragmentCollection = textFragmentAbsorber.getTextFragments();
        
        // Loop through the fragments
        for (TextFragment textFragment : (Iterable<TextFragment>) textFragmentCollection) {
            // Update text and other properties
            textFragment.setText("XXX");
            // textFragment.getTextState().setFont(FontRepository.findFont("Verdana"));
            // textFragment.getTextState().setFontSize(22);
            // textFragment.getTextState().setForegroundColor(Color.getBlue());
            // textFragment.getTextState().setBackgroundColor(Color.getGray());
        }
        // Save the updated PDF file
        pdfDocument.save("Updated_Text1.pdf");
       
    }
}
