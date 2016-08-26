/*
 * The MIT License
 *
 * Copyright 2016 David Ehnert (Saladan).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.graphicloader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.swing.ImageIcon;

/**
 * GraphicLoader is used to get ImageIcons or other texture data from .png files
 * wich are located at assets.textures.
 *
 * @author Saladan
 * @version v1.1.0
 */
public class GraphicLoader {

    private static final Logger LOG = getLogger(GraphicLoader.class.getName());
    private static final Map<String, ImageIcon> ICONS = new HashMap<>();
    private static File GLOBAL_LOCATION = new File("assets/textures");

    /**
     * Returns the texture at the given location as {@link ImageIcon}. If the
     * icon was already loaded, this icon is returned, without creating a new
     * one.
     *
     * The global location for textures is "assets/textures". Here is added the
     * folder and the name of the texture file. The ".png" is automatically
     * added, if not already there.
     *
     * @param location the location of texture in form "folder:[folder:]name"
     * @return the {@link ImageIcon} at this location
     * @see ImageIcon
     * @see createIcon(String, boolean)
     */
    public static ImageIcon createIcon(String location) {
        return createIcon(location, false);
    }

    /**
     * Returns the texture at the given location as {@link ImageIcon}. If
     * {@code forced == false} and if the icon was already loaded, this icon is
     * returned, without creating a new one, if {@code forced == true}, the
     * routine creates a new icon, no matter if it already exists. If the icon
     * was not already loaded {@code forced == true} then two icons are created.
     *
     * The global location for textures is "assets/textures". Here is added the
     * folder and the name of the texture file. The ".png" is automatically
     * added, if not already there.
     *
     * @param location the location of texture in form "folder:[folder:]name"
     * @param force force the creation of an {@link ImageIcon}
     * @return the {@link ImageIcon} at this location
     * @see ImageIcon
     * @see createIcon(String)
     */
    public static ImageIcon createIcon(String location, boolean force) {
        if (!ICONS.containsKey(location) || force) {
            String[] loc = location.split(":");
            String path = GLOBAL_LOCATION.getPath();
            for (String s : loc) {
                path += File.separator + s;
            }
            if (!path.endsWith(".png")) {
                path += ".png";
            }
            File file = new File(path);
            if (force) {
                return createIcon(file);
            } else {
                ICONS.putIfAbsent(location, createIcon(file));
            }
        }
        return ICONS.get(location);
    }

    /**
     * Sets the global location for graphic loading. Checks before, whether the
     * given argument is a directory and exists. When it is not, a
     * {@link IllegalArgumentException} is thrown to specify the error.
     *
     * @param location the new global location of graphic data
     * @throws IllegalArgumentException if the location doesn't exist or if it
     * isn't a directory
     */
    public static void setGlobalLocation(File location) {
        if (location.exists() && location.isDirectory()) {
            GLOBAL_LOCATION = location;
        } else if (!location.exists()) {
            throw new IllegalArgumentException("The directory " + location.getPath() + " doesn't exist.");
        } else if (!location.isDirectory()) {
            throw new IllegalArgumentException("The file " + location.getPath() + " isn't a directory.");
        }
    }

    /**
     * Returns the global location for graphic loading.
     * 
     * @return the current global graphic location
     */
    public static File getGlobalLocation() {
        return GLOBAL_LOCATION;
    }

    private GraphicLoader() {
    }

    private static ImageIcon createIcon(File file) {
        if (file.isAbsolute()) {
            return new ImageIcon(file.getPath());
        } else {
            return new ImageIcon(GraphicLoader.class.getClassLoader().getResource(file.getPath()));
        }
    }

}
