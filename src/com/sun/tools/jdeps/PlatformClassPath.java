/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.tools.jdeps;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * ClassPath for Java SE and JDK
 */
class PlatformClassPath {
    /*
     * Profiles for Java SE
     *
     * This is a temporary workaround until a common API is defined for langtools
     * to determine which profile a given classname belongs to.  The list of
     * packages and profile names are hardcoded in jdk.properties and
     * split packages are not supported.
     */
    static class Profile {
        final String name;
        final Set<String> packages;

        Profile(String name) {
            this.name = name;
            this.packages = new HashSet<String>();
        }
    }

    private final static String JAVAFX = "javafx";
    private final static Map<String,Profile> map = getProfilePackages();
    static String getProfileName(String packageName) {
        Profile profile = map.get(packageName);
        if (packageName.startsWith(JAVAFX + ".")) {
            profile = map.get(JAVAFX);
        }
        return profile != null ? profile.name : "";
    }

    private final static List<Archive> javaHomeArchives = init();
    static List<Archive> getArchives() {
        return javaHomeArchives;
    }

    static boolean contains(Archive archive) {
        return javaHomeArchives.contains(archive);
    }

    private static List<Archive> init() {
        List<Archive> result = new ArrayList<Archive>();
        String javaHome = System.getProperty("java.home");
        List<File> files = new ArrayList<File>();
        File jre = new File(javaHome, "jre");
        File lib = new File(javaHome, "lib");

        try {
            if (jre.exists() && jre.isDirectory()) {
                result.addAll(addJarFiles(new File(jre, "lib")));
                result.addAll(addJarFiles(lib));
            } else if (lib.exists() && lib.isDirectory()) {
                // either a JRE or a jdk build image
                File classes = new File(javaHome, "classes");
                if (classes.exists() && classes.isDirectory()) {
                    // jdk build outputdir
                    result.add(new Archive(classes, ClassFileReader.newInstance(classes)));
                }
                // add other JAR files
                result.addAll(addJarFiles(lib));
            } else {
                throw new RuntimeException("\"" + javaHome + "\" not a JDK home");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // add a JavaFX profile if there is jfxrt.jar
        for (Archive archive : result) {
            if (archive.getFileName().equals("jfxrt.jar")) {
                map.put(JAVAFX, new Profile("jfxrt.jar"));
            }
        }
        return result;
    }

    private static List<Archive> addJarFiles(File f) throws IOException {
        final List<Archive> result = new ArrayList<Archive>();
        final Path root = f.toPath();
        final Path ext = root.resolve("ext");
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                if (dir.equals(root) || dir.equals(ext)) {
                    return FileVisitResult.CONTINUE;
                } else {
                    // skip other cobundled JAR files
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException
            {
                File f = file.toFile();
                String fn = f.getName();
                if (fn.endsWith(".jar") && !fn.equals("alt-rt.jar")) {
                    result.add(new Archive(f, ClassFileReader.newInstance(f)));
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return result;
    }

    private static Map<String,Profile> getProfilePackages() {
        Map<String,Profile> map = new HashMap<String,Profile>();

        // read the properties as a ResourceBundle as the build compiles
        // the properties file into Java class.  Another alternative is
        // to load it as Properties and fix the build to exclude this file.
        ResourceBundle profileBundle =
            ResourceBundle.getBundle("com.sun.tools.jdeps.resources.jdk");

        int i=1;
        String key;
        while (profileBundle.containsKey((key = "profile." + i + ".name"))) {
            Profile profile = new Profile(profileBundle.getString(key));
            String n = profileBundle.getString("profile." + i + ".packages");
            String[] pkgs = n.split("\\s+");
            for (String p : pkgs) {
                if (p.isEmpty()) continue;
                assert(map.containsKey(p) == false);
                profile.packages.add(p);
                map.put(p, profile);
            }
            i++;
        }
        return map;
    }
}
