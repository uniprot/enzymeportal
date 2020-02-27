package uk.ac.ebi.ep.xml.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Slf4j
public class XmlFileUtils {
    private static final String DEFAULT_PERMISSION ="rwxr-xr-x";
    private XmlFileUtils() {
    }

    /**
     * creates directory with default permission rwxr-x---
     *
     * @param directory file directory
     *
     */
    public static void createDirectoryWithDefaultPermission(String directory) {
        //String permission = "rwxr-xr-x";
//        try {
            createDirectory(directory, DEFAULT_PERMISSION);
//        } catch (IOException ex) {
//            log.error("IOException while creating directory " + directory, ex);
//        }

    }

    /**
     *
     * @param directory file directory
     * @param permission directory permission e.g. rwxr-x---
     */
    public static void createDirectory(String directory, String permission) {
         try {
        Set<PosixFilePermission> perms
                = PosixFilePermissions.fromString(permission);
        FileAttribute<Set<PosixFilePermission>> attr
                = PosixFilePermissions.asFileAttribute(perms);
        Path path = Paths.get(directory);
        if (!path.toFile().exists()) {
            Files.createDirectories(path, attr);
        }
                } catch (IOException ex) {
            log.error("IOException while creating directory " + directory, ex);
        }

    }

}
