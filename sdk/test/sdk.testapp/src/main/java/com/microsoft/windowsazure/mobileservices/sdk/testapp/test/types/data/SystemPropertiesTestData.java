/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
Apache 2.0 License
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
See the Apache Version 2.0 License for specific language governing permissions and limitations under the License.
 */
package com.microsoft.windowsazure.mobileservices.sdk.testapp.test.types.data;

import android.util.Pair;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceSystemProperty;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class SystemPropertiesTestData {
    public static List<EnumSet<MobileServiceSystemProperty>> AllSystemProperties;

    static {
        List<MobileServiceSystemProperty> col;
        EnumSet<MobileServiceSystemProperty> set;
        AllSystemProperties = new ArrayList<EnumSet<MobileServiceSystemProperty>>();

        AllSystemProperties.add(EnumSet.noneOf(MobileServiceSystemProperty.class));

        AllSystemProperties.add(EnumSet.allOf(MobileServiceSystemProperty.class));

        col = new ArrayList<MobileServiceSystemProperty>();
        col.add(MobileServiceSystemProperty.CreatedAt);
        col.add(MobileServiceSystemProperty.UpdatedAt);
        set = EnumSet.noneOf(MobileServiceSystemProperty.class);
        set.addAll(col);
        AllSystemProperties.add(set);

        col = new ArrayList<MobileServiceSystemProperty>();
        col.add(MobileServiceSystemProperty.CreatedAt);
        col.add(MobileServiceSystemProperty.Version);
        set = EnumSet.noneOf(MobileServiceSystemProperty.class);
        set.addAll(col);
        AllSystemProperties.add(set);

        col = new ArrayList<MobileServiceSystemProperty>();
        col.add(MobileServiceSystemProperty.UpdatedAt);
        col.add(MobileServiceSystemProperty.Version);
        set = EnumSet.noneOf(MobileServiceSystemProperty.class);
        set.addAll(col);
        AllSystemProperties.add(set);

        col = new ArrayList<MobileServiceSystemProperty>();
        col.add(MobileServiceSystemProperty.CreatedAt);
        set = EnumSet.noneOf(MobileServiceSystemProperty.class);
        set.addAll(col);
        AllSystemProperties.add(set);

        col = new ArrayList<MobileServiceSystemProperty>();
        col.add(MobileServiceSystemProperty.UpdatedAt);
        set = EnumSet.noneOf(MobileServiceSystemProperty.class);
        set.addAll(col);
        AllSystemProperties.add(set);

        col = new ArrayList<MobileServiceSystemProperty>();
        col.add(MobileServiceSystemProperty.Version);
        set = EnumSet.noneOf(MobileServiceSystemProperty.class);
        set.addAll(col);
        AllSystemProperties.add(set);
    }

    ;

    public static String[] ValidSystemProperties = new String[]{"createdAt", "updatedAt", "version", "createdAt"};

    public static String[] NonSystemProperties = new String[]{"someProperty", "__createdAt", "__updatedAt", "__version", "_createdAt", "_updatedAt", "_version",
            "X__createdAt"};

    public static List<Pair<String, String>> VersionsSerialize;

    static {
        VersionsSerialize = new ArrayList<Pair<String, String>>();
        VersionsSerialize.add(new Pair<String, String>("AAAAAAAAH2o=", "\"AAAAAAAAH2o=\""));
        VersionsSerialize.add(new Pair<String, String>("a version", "\"a version\""));
        VersionsSerialize.add(new Pair<String, String>("a version with a \" quote", "\"a version with a \\\" quote\""));
        VersionsSerialize.add(new Pair<String, String>("a version with an already escaped \\\" quote", "\"a version with an already escaped \\\" quote\""));
        VersionsSerialize.add(new Pair<String, String>("\"a version with a quote at the start", "\"\\\"a version with a quote at the start\""));
        VersionsSerialize.add(new Pair<String, String>("a version with a quote at the end\"", "\"a version with a quote at the end\\\"\""));
        VersionsSerialize.add(new Pair<String, String>("datetime'2013-10-08T04%3A12%3A36.96Z'", "\"datetime'2013-10-08T04%3A12%3A36.96Z'\""));
    }

    public static List<Pair<String, String>> VersionsDeserialize;

    static {
        VersionsDeserialize = new ArrayList<Pair<String, String>>();
        VersionsDeserialize.add(new Pair<String, String>("AAAAAAAAH2o=", "\"AAAAAAAAH2o=\""));
        VersionsDeserialize.add(new Pair<String, String>("a version", "\"a version\""));
        VersionsDeserialize.add(new Pair<String, String>("a version with a \" quote", "\"a version with a \\\" quote\""));
        VersionsDeserialize.add(new Pair<String, String>("a version with an already escaped \" quote", "\"a version with an already escaped \\\" quote\""));
        VersionsDeserialize.add(new Pair<String, String>("\"a version with a quote at the start", "\"\\\"a version with a quote at the start\""));
        VersionsDeserialize.add(new Pair<String, String>("a version with a quote at the end\"", "\"a version with a quote at the end\\\"\""));
        VersionsDeserialize.add(new Pair<String, String>("datetime'2013-10-08T04%3A12%3A36.96Z'", "\"datetime'2013-10-08T04%3A12%3A36.96Z'\""));
    }
}