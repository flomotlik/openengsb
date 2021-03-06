/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.core.config.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.LocaleUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

/**
 * Localization helper to lookup string resources from the bundle's localization
 * entries.
 */
public class BundleStrings {

    private Bundle bundle;
    private HashMap<String, URL> entries;

    public BundleStrings() {
    }

    public BundleStrings(Bundle bundle) {
        setBundle(bundle);
    }

    public String getString(String key) {
        return getString(key, Locale.getDefault());
    }

    public String getString(String key, Locale locale) {
        @SuppressWarnings("unchecked")
        List<Locale> locales = LocaleUtils.localeLookupList(locale, new Locale(""));
        for (Locale l : locales) {
            URL url = entries.get(buildEntryFilename(l));
            if (url == null) {
                continue;
            }
            InputStream in = null;
            try {
                in = url.openStream();
                Properties p = new Properties();
                p.load(in);
                if (p.containsKey(key)) {
                    return p.getProperty(key);
                }
            } catch (IOException e) {
                // nop
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // nop
                    }
                }
            }
        }
        return null;
    }

    private String buildEntryFilename(Locale locale) {
        String name = "";
        if (!locale.getLanguage().isEmpty()) {
            name += '_' + locale.getLanguage();
            if (!locale.getCountry().isEmpty()) {
                name += '_' + locale.getCountry();
                if (!locale.getVariant().isEmpty()) {
                    name += '_' + locale.getVariant();
                }
            }
        }
        return name;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        String path = (String) bundle.getHeaders().get(Constants.BUNDLE_LOCALIZATION);
        if (path == null) {
            path = Constants.BUNDLE_LOCALIZATION_DEFAULT_BASENAME;
        }
        path = path.trim();
        int idx = path.lastIndexOf('/');
        if (idx == -1) {
            buildEntriesMap("/", path);
        } else if (idx == 0) {
            buildEntriesMap("/", path.substring(1));
        } else {
            buildEntriesMap(path.substring(0, idx), path.substring(idx + 1));
        }
    }

    private void buildEntriesMap(String directory, String basename) {
        @SuppressWarnings("unchecked")
        Enumeration<URL> resources = bundle.findEntries(directory, basename + "*.properties", false);
        entries = new HashMap<String, URL>();
        while (resources != null && resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String name = new File(url.toString()).getName().substring(basename.length());
            name = name.substring(0, name.length() - ".properties".length());
            entries.put(name, url);
        }
    }
}
