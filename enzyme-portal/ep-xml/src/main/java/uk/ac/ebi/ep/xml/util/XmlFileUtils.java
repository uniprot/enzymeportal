package uk.ac.ebi.ep.xml.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class XmlFileUtils {

    protected static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(XmlFileUtils.class);

    private XmlFileUtils() {
    }

    /**
     * creates directory with default permission rwxr-x---
     *
     * @param directory file directory
     *
     */
    public static void createDirectory(String directory) {
        String permission = "rwxr-x---";
        try {
            createDirectory(directory, permission);
        } catch (IOException ex) {
            logger.error("IOException while creating directory " + directory, ex);
        }

    }

    /**
     *
     * @param directory file directory
     * @param permission directory permission e.g. rwxr-x---
     * @throws IOException
     */
    public static void createDirectory(String directory, String permission) throws IOException {

        Set<PosixFilePermission> perms
                = PosixFilePermissions.fromString(permission);
        FileAttribute<Set<PosixFilePermission>> attr
                = PosixFilePermissions.asFileAttribute(perms);
        boolean fileExist = Files.exists(Paths.get(directory), LinkOption.NOFOLLOW_LINKS);
        if (!fileExist) {
            //Files.createDirectories(Paths.get(directory), attr);
            Files.createDirectory(Paths.get(directory), attr);
        }

    }

}
