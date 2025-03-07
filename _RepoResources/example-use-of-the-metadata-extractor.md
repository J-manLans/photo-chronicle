```java
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

try {
    // Loads the image.
    File imageFile = new File("path/to/image.jpg");
    // Reads the image meta data from the file.
    Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
    // Extracts the EXIF-specific metadata, which contains the image-related data needed.
    ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

    if (directory != null) {
        // Extracts the original date the image was taken.
        Date date = directory.getDate(0);
        // Format the date to a standardized format for easy parsing or display.
        // The 'T' in the format distinguishes the date and time when iterating over the string.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = sdf.format(date);
        // Print out the date the image was taken.
        System.out.println("Image taken: " + formattedDate);
    } else {
        System.out.println("EXIF-data are missing.");
    }
} catch (ImageProcessingException | IOException e) {
    System.err.println("An unexpected error occurred: " + e.getMessage());
}
```