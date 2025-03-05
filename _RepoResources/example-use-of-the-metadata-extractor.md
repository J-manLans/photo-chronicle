```java
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

// Loads the image from the resources folder as an InputStream, allowing it to be bundled in the JAR.
try (InputStream fileInputStream = getClass().getClassLoader().getResourceAsStream("image.jpg")) {
    if (fileInputStream == null) {
        throw new FileNotFoundException("Resource not found");
    }

    // Reads the image meta data from the input stream.
    Metadata metadata = ImageMetadataReader.readMetadata(fileInputStream);
    // Extracts the EXIF-specific metadata, which contains the image-related data needed.
    ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

    if (directory != null) {
        // Extracts the original date the image was taken.
        Date date = directory.getDateOriginal();
        // Format the date to a standardized format for easy parsing or display.
        // The 'T' in the format distinguishes the date and time when iterating over the string.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = sdf.format(date);
        // Print out the date the image was taken.
        System.out.println("Image taken: " + formattedDate);
    } else {
        System.out.println("EXIF-data are missing.");
    }

} catch (IOException e) {
    System.out.println("Error reading stream.");
}
```