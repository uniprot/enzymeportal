package uk.ac.ebi.ep.parser.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author joseph
 */
@Slf4j
public final class FileUtil {

    public static void downloadFile(String webPage, Path path) throws MalformedURLException, IOException {

        URI u = URI.create(webPage);
        try (InputStream in = u.toURL().openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }

    }

    private static FileAttribute<Set<PosixFilePermission>> fileAttributes(String permission) {
        Set<PosixFilePermission> perms
                = PosixFilePermissions.fromString(permission);

        return PosixFilePermissions.asFileAttribute(perms);
    }

    public static Path createDirectoriesWithPermission(Path directories, String permission) throws IOException {

        FileAttribute<Set<PosixFilePermission>> attr = fileAttributes(permission);

        return Files.createDirectories(directories, attr);
    }
}
