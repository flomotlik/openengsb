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
package org.openengsb.core.common.descriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openengsb.core.common.Domain;
import org.openengsb.core.common.util.BundleStrings;

import com.google.common.base.Preconditions;

public class ServiceDescriptor {
    private String id;
    private Class<? extends Domain> serviceType;
    private String name;
    private String description;
    private Class<? extends Domain> implementationType;
    private final List<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();

    public ServiceDescriptor() {
    }

    /**
     * Returns an id that uniquely identifies this managed service.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the service interface id this service implements.
     */
    public Class<? extends Domain> getServiceType() {
        return serviceType;
    }

    public void setServiceType(Class<? extends Domain> serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Returns the Class that implements this service
     */
    public Class<? extends Domain> getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(Class<? extends Domain> implementationType) {
        this.implementationType = implementationType;
    }

    /**
     * Returns a localized name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a localized description.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a list of attributes the described service supports.
     */
    public List<AttributeDefinition> getAttributes() {
        return attributes;
    }

    public void addAttribute(AttributeDefinition attribute) {
        attributes.add(attribute);
    }

    public static Builder builder(Locale locale, BundleStrings strings) {
        return new Builder(locale, strings);
    }

    public static class Builder {
        private final ServiceDescriptor desc;
        private final BundleStrings strings;
        private final Locale locale;

        public Builder(Locale locale, BundleStrings strings) {
            this.locale = locale;
            this.strings = strings;
            desc = new ServiceDescriptor();
        }

        public Builder id(String id) {
            desc.id = id;
            return this;
        }

        public Builder serviceType(Class<? extends Domain> serviceType) {
            desc.serviceType = serviceType;
            return this;
        }

        public Builder implementationType(Class<? extends Domain> type) {
            desc.implementationType = type;
            return this;
        }

        public Builder name(String key) {
            desc.name = strings.getString(key, locale);
            return this;
        }

        public Builder description(String key) {
            desc.description = strings.getString(key, locale);
            return this;
        }

        public Builder attribute(AttributeDefinition ad) {
            desc.attributes.add(ad);
            return this;
        }

        public ServiceDescriptor build() {
            Preconditions.checkState(desc.id != null && !desc.id.trim().isEmpty(), "id has not been set");
            Preconditions.checkState(desc.serviceType != null, "service type has not been set");
            Preconditions.checkState(desc.implementationType != null, "implementation type has not been set");
            Preconditions.checkState(desc.serviceType.isAssignableFrom(desc.implementationType),
                    "implementatio type is not compatible to service type");
            Preconditions.checkState(desc.name != null && !desc.name.trim().isEmpty(), "service name has not been set");
            Preconditions.checkState(desc.description != null && !desc.description.trim().isEmpty(),
                    "service description has not been set");
            return desc;
        }

        public AttributeDefinition.Builder newAttribute() {
            return AttributeDefinition.builder(locale, strings);
        }
    }
}