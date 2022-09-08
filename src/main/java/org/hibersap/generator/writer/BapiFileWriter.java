package org.hibersap.generator.writer;

import org.hibersap.generator.base.AbstractBaseGenerator;
import org.hibersap.generator.base.BapiConstants;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Class to write Java sourcecode to disk
 */
public class BapiFileWriter extends AbstractBaseGenerator {

    public static void write(String name, JavaClassSource javaClassSource) {
        write(name, javaClassSource, false);
    }

    public static void write(String name, JavaClassSource javaClassSource, boolean structure) {
        final String PACKAGE = (structure ? BapiConstants.TARGET_JAVA_PACKAGE_STRUCTURE : BapiConstants.TARGET_JAVA_PACKAGE);
        File targetFolder = Paths.get(BapiConstants.OUTPUT_FOLDER, PACKAGE.replaceAll("\\.", "/")).toFile();
        javaClassSource.setPackage(PACKAGE);
        if (!targetFolder.exists()) {
            LOG.info("Creating target folder " + targetFolder.getAbsolutePath());
            targetFolder.mkdirs();
        }
        try {
            Path target = Paths.get(targetFolder.getAbsolutePath(), name + ".java");
            if (target.toFile().exists()) {
                LOG.warning("Deleting pre-existing source file " + target.toFile().getAbsolutePath());
                target.toFile().delete();
            }
            Files.writeString(target, javaClassSource.toString(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
